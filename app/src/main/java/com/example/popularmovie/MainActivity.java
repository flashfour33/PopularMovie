package com.example.popularmovie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tvToday;
    String hariIni;
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private RecyclerView mRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    //private ListView lv;

    private MovieAdapter movieAdapter;

    private static String url = "https://api.themoviedb.org/3/movie/popular?api_key=0dde3e9896a8c299d142e214fcb636f8&language=en-US&page=1";
    private static String url_img = "https://image.tmdb.org/t/p/w500";

    private final List<Map<String, String>> movieList  = new ArrayList<Map<String, String>>();
    public static final String MOVIE_ID = "movie_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.list);

        new GetMovies().execute();

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedLIstener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(),Favorite.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.alarm:
                        startActivity(new Intent(getApplicationContext(), Alarm.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        tvToday = findViewById(R.id.tvDate);

        //get Time Now
        Date dateNow = Calendar.getInstance().getTime();
        hariIni = (String) DateFormat.format("EEEE", dateNow);
        getToday();

    }

    private void getToday() {
        Date date = Calendar.getInstance().getTime();
        String tanggal = (String) DateFormat.format("d MMMM yyyy", date);
        String formatFix = hariIni + ", " + tanggal;
        tvToday.setText(formatFix);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetMovies extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray movies = jsonObj.getJSONArray("results");

                    // looping through All Movies
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);

                        String id = c.getString("id");
                        String original_title = c.getString("original_title");
                        String title = c.getString("title");
                        String overview = c.getString("overview");
                        String backdrop_path = c.getString("backdrop_path");
                        String poster_path = c.getString("poster_path");
                        String backdrop = url_img + backdrop_path;
                        String poster = url_img + poster_path;
                        String rating = c.getString("vote_average") + "/10";
                        String votes = c.getString("vote_count");
                        String release = c.getString("release_date");

                        // change date format
                        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
                        Date dateFormated = df.parse(release);
                        df.applyPattern("dd M yyyy");
                        String date = df.format(dateFormated);

                        HashMap<String, String> movie = new HashMap<>();

                        // adding each child node to HashMap key => value
                        movie.put("id", id);
                        movie.put("original_title", original_title);
                        movie.put("title", title);
                        movie.put("overview", overview);
                        movie.put("backdrop",backdrop);
                        movie.put("poster",poster);
                        movie.put("rating",rating);
                        movie.put("votes",votes);
                        movie.put("date",date);


                        movieList.add(movie);
                    }
                } catch (final JSONException | ParseException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            movieAdapter = new MovieAdapter(MainActivity.this, movieList);

            mRecyclerView.setAdapter(movieAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        }

    }
}