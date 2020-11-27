package com.example.popularmovie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String movieId;
    private final Map<String, String> movieDetail = new HashMap<String, String>();
    private static String url = "https://api.themoviedb.org/3/movie/popular?api_key=0dde3e9896a8c299d142e214fcb636f8&language=en-US&page=1";
    private static String url_img = "https://image.tmdb.org/t/p/w500";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        HashMap<String,String> movieDetail = (HashMap<String, String>) intent.getSerializableExtra("mapDetail");
        this.movieId = id;


        TextView title = findViewById(R.id.title);
        TextView original_title = findViewById(R.id.original_title);
        TextView overview = findViewById(R.id.overview);
        TextView rating = findViewById(R.id.rating);
        TextView vote = findViewById(R.id.vote);
        TextView release = findViewById(R.id.release);
        ImageView movieBackdrop = findViewById(R.id.movie_backdrop);
        ImageView moviePoster = findViewById(R.id.movie_poster);

        // compare String; jika title == original_titla, maka tidak perlu tempilkan original_title
        String titleStr = movieDetail.get("title");
        String oriTitleStr = movieDetail.get("original_title");
        if(titleStr.equals(oriTitleStr)){
            original_title.setVisibility(TextView.GONE);
        }

        original_title.setText(movieDetail.get("original_title"));
        title.setText(movieDetail.get("title"));
        rating.setText(movieDetail.get("rating"));
        vote.setText(movieDetail.get("votes"));
        release.setText(movieDetail.get("date"));
        overview.setText(movieDetail.get("overview"));

        String urlBackdrop = movieDetail.get("backdrop");
        String urlPoster = movieDetail.get("poster");
        Glide.with(movieBackdrop.getContext())
                .load(urlBackdrop)
                .into(movieBackdrop);
        Glide.with(moviePoster.getContext())
                .load(urlPoster)
                .into(moviePoster);
    }


}



