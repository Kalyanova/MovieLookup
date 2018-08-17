package by.paranoidandroid.movielookup.service;

import by.paranoidandroid.movielookup.model.entities.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OmdbService {

    @GET(".")
    Call<MovieResponse> getMovies(@Query("s") String search);

}
