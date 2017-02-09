package com.applicationlist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.applicationlist.ListApplication;
import com.applicationlist.R;
import com.applicationlist.adapter.ContactsAdapter;
import com.applicationlist.adapter.ListItemTouchHelper;
import com.applicationlist.adapter.MoviesAdapter;
import com.applicationlist.pojo.MovieResponse;
import com.applicationlist.pojo.Result;
import com.applicationlist.rest.ApiClient;
import com.applicationlist.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleRetroFit extends AppCompatActivity {

    private List<Result> movies;
    MoviesAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_retro_fit);
        recyclerView = (RecyclerView) findViewById(R.id.retrofit_rv);
        pb = (ProgressBar) findViewById(R.id.pb);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getTopRatedMovie(ListApplication.API_KEY);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                pb.setVisibility(View.GONE);
                movies = response.body().getResults();
                adapter = new MoviesAdapter(SampleRetroFit.this,movies);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                pb.setVisibility(View.GONE);
                Toast.makeText(SampleRetroFit.this,t.toString(),Toast.LENGTH_SHORT).show();
                Log.e("Sample", t.toString());
            }
        });



    }
}
