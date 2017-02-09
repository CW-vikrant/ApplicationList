package com.applicationlist.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.applicationlist.ListApplication;
import com.applicationlist.R;
import com.applicationlist.adapter.MoviesAdapter;
import com.applicationlist.pojo.MovieDetails;
import com.applicationlist.pojo.MovieResponse;
import com.applicationlist.rest.ApiClient;
import com.applicationlist.rest.ApiInterface;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    ProgressBar pb;
    public static final String ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        pb = (ProgressBar) findViewById(R.id.pb);

        if (getIntent() != null) {
            int id = getIntent().getIntExtra(ID, 0);
            //requestData(id);
            new MyTask().execute(id);
        }

    }

    public MovieDetails requestDataHttpURLConnection(int id) {
        URL url;
        HttpURLConnection urlConnection = null;
        MovieDetails details = null;
        BufferedReader br = null;
        try {
            url = new URL("http://api.themoviedb.org/3/movie/" + id + "?api_key=" + ListApplication.API_KEY);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String data;
            StringBuilder builder = new StringBuilder();
            while ((data = br.readLine()) != null) {
                builder.append(data);
            }

            Gson gson = new Gson();
            details = gson.fromJson(builder.toString(), MovieDetails.class);

        } catch (Exception e) {
            Log.e("MovieDetailsActivity", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    Log.e("MovieDetailsActivity", "Error closing stream", e);
                }
            }
        }

        return details;
    }

    public void requestData(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieDetails> call = apiService.getMovieDetail(id, ListApplication.API_KEY);

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                setData(response.body());
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                pb.setVisibility(View.GONE);
            }
        });
    }

    public void setData(MovieDetails details) {
        pb.setVisibility(View.GONE);
        if (details != null) {
            TextView tv = (TextView) findViewById(R.id.tv);
            tv.setText(details.getOverview());
        }
    }

    public class MyTask extends AsyncTask<Integer, Void, MovieDetails> {

        @Override
        protected MovieDetails doInBackground(Integer... params) {

            return requestDataHttpURLConnection(params[0]);
        }

        protected void onPostExecute(MovieDetails details) {

            setData(details);
        }
    }
}
