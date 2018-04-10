package movies.popular.popularmoviesapp;

import movies.popular.popularmoviesapp.models.MovieListResponse;
import movies.popular.popularmoviesapp.models.ReviewListResponse;
import movies.popular.popularmoviesapp.models.VideoListResponse;
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

    @GET("3/movie/{id}/videos")
    Call<VideoListResponse> getVideosFromAMovie(@Path("id") long id, @Query("api_key") String myApiKey);

    @GET("3/movie/{id}/reviews")
    Call<ReviewListResponse> getReviewsFromAMovie(@Path("id") long id, @Query("api_key") String myApiKey);
}
