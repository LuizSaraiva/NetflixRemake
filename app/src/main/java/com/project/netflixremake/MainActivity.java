package com.project.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.netflixremake.model.Category;
import com.project.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_main;
    private RecyclerView.LayoutManager lm_main;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = new Category();
            category.setName("cat: "+i);
            categories.add(category);

            List<Movie> listMovie = new ArrayList<>();
            for (int j = 0; j < 30; j++) {
                Movie movie = new Movie();
                movie.setCoverUrl(R.drawable.placeholderitem_bg);
                listMovie.add(movie);
            }

            category.setMovie(listMovie);
            categories.add(category);
        }

        rv_main = findViewById(R.id.rv_main);
        lm_main = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mainAdapter = new MainAdapter(categories);

        rv_main.setLayoutManager(lm_main);
        rv_main.setAdapter(mainAdapter);


    }


    private class MainAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

        List<Category> ListCategories;

        MainAdapter(List<Category> category) {
            this.ListCategories = category;
        }

        @NonNull
        @Override
        public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryViewHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
            Category category = ListCategories.get(position);
            holder.txViewTitle.setText(category.getName());
            holder.rv_category.setAdapter(new MovieAdapter(category.getMovie()));
            holder.rv_category.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        public int getItemCount() {
            return ListCategories.size();
        }
    }

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView txViewTitle;
        RecyclerView rv_category;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txViewTitle = itemView.findViewById(R.id.tx_category);
            rv_category = itemView.findViewById(R.id.rv_category);

        }
    }


    private class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

        List<Movie> movies;

        MovieAdapter(List<Movie> movie) {
            this.movies = movie;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieViewHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            Movie movie = movies.get(position);
            holder.img_movie.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }


    private static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView img_movie;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            img_movie = itemView.findViewById(R.id.img_item);
        }
    }


}