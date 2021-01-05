package com.project.netflixremake.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaRouter;
import android.os.AsyncTask;
import android.util.Log;

import com.project.netflixremake.model.Category;
import com.project.netflixremake.model.Movie;

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
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {

    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private CategoryLoader categoryLoader;

    public void setCategoryLoader(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    //main-thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if (context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", false);
    }

    //thread - background
    @Override
    protected List<Category> doInBackground(String... params) {
        String url = params[0];

        try {
            URL requestUrl = new URL(url);

            HttpsURLConnection Urlconnection = (HttpsURLConnection) requestUrl.openConnection();
            Urlconnection.setReadTimeout(2000);
            Urlconnection.setConnectTimeout(2000);

            int responseCode = Urlconnection.getResponseCode();

            if (responseCode > 400) {
                throw new IOException("Erro na comunicaçao do servidor!");
            }

            InputStream inputStream = Urlconnection.getInputStream();

            BufferedInputStream in = new BufferedInputStream(inputStream);

            String jsonAsString = toString(in);

            List<Category> categories = getCategory(new JSONObject(jsonAsString));
            in.close();

            return categories;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            e.getMessage();
        }

        return null;

    }

    //main-thread
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);

        dialog.dismiss();


        if(categoryLoader != null)
            categoryLoader.onResult(categories);    ;
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

    public interface CategoryLoader{
        void onResult(List<Category> categories);
    }

    //Metodo que converte Json para JAVA
    private List<Category> getCategory(JSONObject json) throws JSONException {
        List<Category> categories = new ArrayList<>();

        JSONArray categoryArray = json.getJSONArray("category");

        for (int i = 0; i < categoryArray.length(); i++) {

            JSONObject category = categoryArray.getJSONObject(i);
            String title = category.getString("title");

            JSONArray moviesArray = category.getJSONArray("movie");
            List<Movie> movies = new ArrayList<>();
            for (int j = 0; j < moviesArray.length(); j++) {

                JSONObject movie = moviesArray.getJSONObject(j);
                String coverUrl = movie.getString("cover_url");
                int id = movie.getInt("id");

                Movie movieObj = new Movie();
                movieObj.setCoverUrl(coverUrl);
                movieObj.setId(id);

                movies.add(movieObj);
            }

            Category categoryObj = new Category();
            categoryObj.setName(title);
            categoryObj.setMovie(movies);
            categories.add(categoryObj);
        }

        return categories;
    }

}
