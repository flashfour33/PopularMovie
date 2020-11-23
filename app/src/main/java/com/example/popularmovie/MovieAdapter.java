package com.example.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class MovieAdapter extends
        RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private LayoutInflater mInflater;
    private final List<Map<String, String>> mMovieList;
    //private final Map<String,String> mMovieDetail;

    public MovieAdapter(Context context, List<Map<String, String>> movieList) {
        mInflater = LayoutInflater.from(context);
        this.mMovieList = movieList;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public final TextView movieTitle;
        public final TextView movieOriTitle;
        public final TextView movieOverview;
        public final ImageView movieBackdrop;
        final MovieAdapter mAdapter;

        public MovieViewHolder(View itemView, MovieAdapter adapter) {
            super(itemView);
            //movieItemView = itemView.findViewById(R.id.list);
            movieTitle = itemView.findViewById(R.id.title);
            movieOriTitle = itemView.findViewById(R.id.original_title);
            movieOverview = itemView.findViewById(R.id.overview);
            movieBackdrop = itemView.findViewById(R.id.movie_backdrop);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mMovieList.
            Map<String, String> data = mMovieList.get(mPosition);

            HashMap<String,String> detailMovie = new HashMap<String,String>();

            detailMovie.put("title",data.get("title"));
            detailMovie.put("original_title",data.get("original_title"));
            detailMovie.put("overview",data.get("overview"));
            detailMovie.put("poster",data.get("poster"));
            detailMovie.put("backdrop",data.get("backdrop"));
            detailMovie.put("rating",data.get("rating"));
            detailMovie.put("votes",data.get("votes"));
            detailMovie.put("date",data.get("date"));

            String id = (String) data.get("id");
            Log.d("check","id : "+ id);

            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            //String message = mMessageEditText.getText().toString();
            intent.putExtra("id",id);
            intent.putExtra("mapDetail", detailMovie);
            view.getContext().startActivity(intent);
        }

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Map<String, ?> mCurrent = mMovieList.get(position);
        Log.d("questionIsBranchedOrNot","position : " + position + "title : " + mCurrent.get("title"));

        String oriTitle = (String) mCurrent.get("original_title");
        String title = (String) mCurrent.get("title");
        String urlBackdrop = (String) mCurrent.get("backdrop");

        if(title.equals(oriTitle)){
            oriTitle = "";
        }

        holder.movieTitle.setText(title);
        holder.movieOriTitle.setText(oriTitle);
        Glide.with(holder.movieBackdrop.getContext())
                .load(urlBackdrop)
                .into(holder.movieBackdrop);
        //holder.movieBackdrop.setImageResource(Integer.parseInt(urlBackdrop));

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
}
