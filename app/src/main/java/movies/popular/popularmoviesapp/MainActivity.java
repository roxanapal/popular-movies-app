package movies.popular.popularmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.popular.popularmoviesapp.models.Movie;
import movies.popular.popularmoviesapp.models.MovieListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rv)
    RecyclerView recyclerView;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecyclerView();
        getAllMovies();
    }

    private void initRecyclerView() {
        adapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void getAllMovies() {
        MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);

        Call<MovieListResponse> movieCall = service.getMovies(BuildConfig.MOVIE_API_KEY);
        movieCall.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response == null)
                    return;

                MovieListResponse movieListResponse = response.body();
                adapter.setMovieList(movieListResponse.getResults());
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading movies.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
