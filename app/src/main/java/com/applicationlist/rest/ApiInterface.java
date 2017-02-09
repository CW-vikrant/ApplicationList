package com.applicationlist.rest;

import com.applicationlist.pojo.MovieDetails;
import com.applicationlist.pojo.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Vikrant Chauhan on 10/17/2016.
 */

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovie(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetails> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);

}
