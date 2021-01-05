package com.project.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.project.netflixremake.model.Movie;
import com.project.netflixremake.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

    //private List<MovieDetail> movieDetails;
    private final WeakReference<Context> context; //Referencia para ser usada no construtor
    private MovieDetailLoader movieDetailLoader; //Interface
    private ProgressDialog dialog; //Dialog

    //Construtor com WeakReference
    public MovieDetailTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    //Imprementa a interface
    public void MovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Context context = this.context.get();
        if (context != null) {
            dialog = ProgressDialog.show(context, "Carregando", "", true);
        }
    }

    @Override
    protected MovieDetail doInBackground(String... params) { //movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/"+ id);
        String urlString = params[0];

        try {
            URL url = new URL(urlString);

            //Abrindo conexao
            HttpsURLConnection urlRequest = (HttpsURLConnection) url.openConnection();

            //Resposta da conexao
            int responseCode = urlRequest.getResponseCode();

            //Verifica retorno da conexao, caso falha sai do fluxo
            if (responseCode < 200) {
                throw new IOException("Falha ao conectar");
            }

            InputStream inputStream = urlRequest.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(inputStream);

            //Converte BufferedInputStream em String
            String jsonAsString = toString(bis);

            MovieDetail movieDetails = getMovieDetail(new JSONObject(jsonAsString));
            bis.close();

            return movieDetails;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {

        int id = json.getInt("id");
        String title = json.getString("title");
        String cast = json.getString("cast");
        String desc = json.getString("desc");
        String coverUrl = json.getString("cover_url");

        List<Movie> movies = new ArrayList();
        JSONArray movieArray = json.getJSONArray("movie");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            String cap = movie.getString("cover_url");
            int idSimilar = movie.getInt("id");

            Movie similar = new Movie();
            similar.setId(idSimilar);
            similar.setCoverUrl(cap);

            movies.add(similar);
        }

        Movie movie = new Movie();
        movie.setId(id);
        movie.setCoverUrl(coverUrl);
        movie.setCast(cast);
        movie.setDesc(desc);
        movie.setTitle(title);

        return new MovieDetail(movie, movies);
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;

        while ((lidos = is.read(bytes)) > 0) {
            System.out.println(is.read());
            baos.write(bytes, 0, lidos);

        }

        return new String(baos.toByteArray());
    }


    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);

        dialog.dismiss();

        if (movieDetailLoader != null) {
            movieDetailLoader.onResult(movieDetail);
        }
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
    }
}
