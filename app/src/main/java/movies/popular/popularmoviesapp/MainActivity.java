package movies.popular.popularmoviesapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.popular.popularmoviesapp.data.MovieContract;
import movies.popular.popularmoviesapp.models.Constants;
import movies.popular.popularmoviesapp.models.Movie;
import movies.popular.popularmoviesapp.models.MovieListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.Listener, Constants {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    private static final String SPINNER_POSITION_KEY = "SPINNER_POSITION_KEY";

    @BindView(R.id.main_rv)
    RecyclerView recyclerView;

    @BindView(R.id.spn_main_list_type)
    Spinner spnListType;

    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private MovieAdapter adapter;
    private boolean isLoaderFinished = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();
        setSpinnerAdapter();
        toolbar.setNavigationIcon(R.drawable.ic_movie_black_24px);

        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(SPINNER_POSITION_KEY, 0);
            spnListType.setSelection(position);
        } else {
            getPopularMovies();
        }
        Log.i(TAG, "onCreate");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SPINNER_POSITION_KEY, spnListType.getSelectedItemPosition());
    }

    private void initRecyclerView() {
        adapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private void setSpinnerAdapter() {
        ArrayAdapter<CharSequence> spnAdapter = ArrayAdapter.createFromResource(
                getApplicationContext(), R.array.list_type_values,
                android.R.layout.simple_spinner_item);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnListType.setAdapter(spnAdapter);

        spnListType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDataByPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getDataByPosition(int position) {
        if (position == 0) {
            getPopularMovies();
            isLoaderFinished = true;
        } else if (position == 1) {
            getTopRatedMovies();
            isLoaderFinished = true;
        } else if (position == 2) {
            getFavoritesMovies();
        }
    }

    private void getMovies(Call<MovieListResponse> movieCall) {
        movieCall.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                if (response == null || response.body() == null)
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

    public void getFavoritesMovies() {
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor movieData = null;

            @Override
            protected void onStartLoading() {
                if (movieData != null) {
                    deliverResult(movieData);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                             MovieContract.MovieEntry.COLUMN_TITLE +" ASC");

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                movieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (isLoaderFinished) {
            adapter.clearList();
            for (int i = 0; i < data.getCount(); i++) {
                data.moveToPosition(i);
                long movieId = data.getLong(data.getColumnIndex(MovieContract.MovieEntry._ID));

                MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);
                Call<Movie> movieCall = service.getMovieForId(movieId, BuildConfig.MOVIE_API_KEY);
                getMovieForId(movieCall);
            }
            isLoaderFinished = false;
        }
    }

    private void getMovieForId(Call<Movie> movieCall) {
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response == null)
                    return;

                Movie movie = response.body();
                adapter.addMovieToList(movie);
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error loading movies.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (spnListType != null && spnListType.getSelectedItemPosition() == 2) {
            isLoaderFinished = true;
            adapter.clearList();
        }
    }

    @Override
    public void openDetailsActivity(Movie movie) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (spnListType != null && spnListType.getSelectedItemPosition() == 2) {
            isLoaderFinished = true;
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isLoaderFinished = true;
        getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
