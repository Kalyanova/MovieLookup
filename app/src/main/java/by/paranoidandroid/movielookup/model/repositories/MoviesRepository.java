package by.paranoidandroid.movielookup.model.repositories;

import java.util.List;

import by.paranoidandroid.movielookup.common.MyApplication;
import by.paranoidandroid.movielookup.model.entities.Movie;
import by.paranoidandroid.movielookup.model.entities.MovieResponse;
import by.paranoidandroid.movielookup.utils.FailureProcessor;
import by.paranoidandroid.movielookup.utils.SuccessProcessor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {
    private final String QUERY_KEY_PAGE = "page";
    public boolean isLastPage;
    public String errorRs;
    public int errorCode;

    public void getMovies(String search,
                          SuccessProcessor successProcessor,
                          FailureProcessor failureProcessor) {
        isLastPage = false;
        errorRs = null;
        errorCode = 0;

        Call<MovieResponse> call = MyApplication.getApi().getMovies(search);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();
                    if (movieResponse != null) {
                        if (movieResponse.getResponse() && movieResponse.getTotalResults() > 0) {
                            List<Movie> movies = movieResponse.getSearch();
                            isLastPage = isLastPage(movieResponse.getTotalResults(),
                                    Integer.valueOf(response.raw().request().url().queryParameter(QUERY_KEY_PAGE)));
                            successProcessor.process(movies);
                        } else {
                            errorRs = movieResponse.getError();
                            failureProcessor.process();
                        }
                    }
                } else {
                    errorCode = response.code();
                    failureProcessor.process();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                failureProcessor.process();
            }
        });
    }

    private boolean isLastPage(int totalResults, int page) {
        return (totalResults % 10 == 0 && totalResults / 10 == page)
                || (totalResults / 10 + 1 == page);
    }
}