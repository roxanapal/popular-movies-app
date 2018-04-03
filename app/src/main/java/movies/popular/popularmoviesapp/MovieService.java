package movies.popular.popularmoviesapp;

import java.util.List;

import movies.popular.popularmoviesapp.models.Movie;
import movies.popular.popularmoviesapp.models.MovieListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by roxan on 3/10/2018.
 */

public interface MovieService {
    @GET("3/movie/popular")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String myApiKey);

    @GET("3/movie/top_rated")
    Call<MovieListResponse> getTopRatedMovies(@Query("api_key") String myApiKey);

    @GET("3/movie/{id}")
    Call<Movie> getMovieById(@Path("id") long id, @Query("api_key") String myApiKey);
}
