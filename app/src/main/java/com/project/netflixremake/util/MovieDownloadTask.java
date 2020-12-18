package com.project.netflixremake.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.project.netflixremake.model.Movie;

import org.apache.http.params.HttpConnectionParams;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MovieDownloadTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageView;
    private HttpURLConnection urlConnection = null;


    public MovieDownloadTask(ImageView imageView) {
        this.imageView = new WeakReference(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];

        try {
            URL requestUrl = new URL(url);

            urlConnection = (HttpURLConnection) requestUrl.openConnection();

            int respondeCode = urlConnection.getResponseCode();

            if (respondeCode != 200)
                return null;

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null)
                return BitmapFactory.decodeStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled())
            bitmap = null;

        ImageView imageView = this.imageView.get();
        if (imageView != null && bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }

    }
}
