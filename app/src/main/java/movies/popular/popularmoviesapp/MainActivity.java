package movies.popular.popularmoviesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.popular.popularmoviesapp.models.MovieListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rv)
    RecyclerView recyclerView;

    @BindView(R.id.spn_main_list_type)
    Spinner spnListType;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();
        getPopularMovies();
        setSpinnerAdapter();
        toolbar.setNavigationIcon(R.drawable.ic_movie_black_24px);
    }

    private void initRecyclerView() {
        adapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }


    private void setSpinnerAdapter(){
        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(), R.array.list_type_values,
                android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnListType.setAdapter(spnAdapter);

        spnListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    getPopularMovies();
                } else if(position == 1){
                    getTopRatedMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getMovies(Call<MovieListResponse> movieCall){
        movieCall.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response == null)
                    return;

                MovieListResponse movieListResponse = response.body();
                adapter.setMovieList(movieListResponse.getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPopularMovies() {
        MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);
        Call<MovieListResponse> movieCall = service.getPopularMovies(BuildConfig.MOVIE_API_KEY);
        getMovies(movieCall);

    }

    private void getTopRatedMovies() {
        MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);
        Call<MovieListResponse> movieCall = service.getTopRatedMovies(BuildConfig.MOVIE_API_KEY);
        getMovies(movieCall);
    }
}
