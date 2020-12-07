package com.project.netflixremake;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.LayoutManager;


import com.project.netflixremake.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView rv_similar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.txt_title);
        txtDesc = findViewById(R.id.txt_desc);
        txtCast = findViewById(R.id.txt_cast);
        rv_similar = findViewById(R.id.rv_similar);

        Toolbar toobar = findViewById(R.id.tb_movie);
        setSupportActionBar(toobar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        txtTitle.setText("BATMAN");
        txtDesc.setText("Descricao");

        txtCast.setText(getString(R.string.cast     , "ator 1" + ",     ator 2"));

        List<Movie> listMovie = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Movie movie = new Movie();
            listMovie.add(movie);
        }

        rv_similar.setAdapter(new MovieAdapter());
        rv_similar.setLayoutManager(new GridLayoutManager(this, 3));

    }


    private class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {

//        private final List<Movie> movies;


        @NonNull
        @Override
        public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieViewHolder(getLayoutInflater().inflate(R.layout.item_movie_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
//            Movie movie = movies.get(position);
//            holder.img_movie.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }


    private class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView img_movie;
//        TextView tx;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            img_movie = itemView.findViewById(R.id.img_item_similar);
        }
    }

}