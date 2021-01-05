package com.project.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.netflixremake.model.Category;
import com.project.netflixremake.model.Movie;
import com.project.netflixremake.util.CategoryTask;
import com.project.netflixremake.util.MovieDownloadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private RecyclerView rv_main;
    private RecyclerView.LayoutManager lm_main;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categories = new ArrayList<>();

        rv_main = findViewById(R.id.rv_main);
        lm_main = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mainAdapter = new MainAdapter(categories);

        rv_main.setLayoutManager(lm_main);
        rv_main.setAdapter(mainAdapter);

        //Recuperar dados JSON
        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.setCategoryLoader(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
    }

    @Override
    public void onResult(List<Category> categories) {
        mainAdapter.setCategory(categories);
        mainAdapter.notifyDataSetChanged();
    }


    private class MainAdapter extends RecyclerView.Adapter<CategoryViewHolder>{

        private List<Category> ListCategories;

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

        public void setCategory(List<Category> categories) {
            this.ListCategories = categories;
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


    private class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> implements onItemClickListener {

        private final List<Movie> movies;

        MovieAdapter(List<Movie> movie ) {
            this.movies = movie;
        }

        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item, parent, false);
            return new MovieViewHolder(view,this);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
            Movie movie = movies.get(position);
            new MovieDownloadTask(holder.img_movie).execute(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        @Override
        public void onClick(int position) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra("id", movies.get(position).getId());
            startActivity(intent);
        }
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView img_movie;

        public MovieViewHolder(@NonNull View itemView, onItemClickListener onItemClickListener) {
            super(itemView);
            img_movie = itemView.findViewById(R.id.img_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }

    interface onItemClickListener{
        void onClick(int position);
    }

}