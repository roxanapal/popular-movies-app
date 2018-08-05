package movies.popular.popularmoviesapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import movies.popular.popularmoviesapp.data.MovieContract;
import movies.popular.popularmoviesapp.models.Constants;
import movies.popular.popularmoviesapp.models.Movie;
import movies.popular.popularmoviesapp.models.ReviewListResponse;
import movies.popular.popularmoviesapp.models.VideoListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements Constants {

    private static final int DEFAULT_ID = -1;

    @BindView(R.id.details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.details_title)
    TextView tvTitle;

    @BindView(R.id.backdrop_image)
    ImageView ivBackdrop;

    @BindView(R.id.poster_image)
    ImageView ivPoster;

    @BindView(R.id.details_overview)
    TextView tvOverview;

    @BindView(R.id.details_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.details_vote_average)
    TextView tvVoteAverage;

    @BindView(R.id.details_videos_label)
    TextView tvVideosLabel;

    @BindView(R.id.details_recycler_view_videos)
    RecyclerView rvVideos;

    @BindView(R.id.details_recycler_view_reviews)
    RecyclerView rvReviews;

    @BindView(R.id.details_btn_favorite_star)
    ImageView ivStar;

    private VideoAdapter videoAdapter;
    private ReviewAdapter reviewAdapter;
    private Movie movie;

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            errorMessage();
            return;
        }

        if (intent.hasExtra(EXTRA_MOVIE)){
            movie = intent.getParcelableExtra(EXTRA_MOVIE);
        }
        else if (savedInstanceState != null && savedInstanceState.getParcelable(EXTRA_MOVIE) != null){
            movie = savedInstanceState.getParcelable(EXTRA_MOVIE);
        }

        if (movie == null || movie.getId() == DEFAULT_ID) {
            errorMessage();
            return;
        }

        checkIfIsFavorite(movie);

        setMovieInfo(movie);

        initRecyclerViewVideos();
        initRecyclerViewReviews();

        getVideos(movie.getId());
        getReviews(movie.getId());
    }

/*
    @Override
    public void onBackPressed() {
        finish();
    }
*/

    private void checkIfIsFavorite(Movie movie) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI .buildUpon().appendPath(String.valueOf(movie.getId())).build();
        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if(cursor.getCount() == 0) {
            isFavorite = false;
        } else {
            isFavorite = true;
            ivStar.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.star_selected));
        }
    }


    private void errorMessage() {
        Toast.makeText(this, R.string.details_data_error, Toast.LENGTH_SHORT).show();
    }

    private void initRecyclerViewVideos() {
        videoAdapter = new VideoAdapter(this);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));
        rvVideos.setAdapter(videoAdapter);
    }

    private void initRecyclerViewReviews() {
        reviewAdapter = new ReviewAdapter(this);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        rvReviews.setAdapter(reviewAdapter);
    }

    private void getVideos(long id) {
        MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);
        Call<VideoListResponse> videoCall = service.getVideosFromAMovie(id, BuildConfig.MOVIE_API_KEY);

        videoCall.enqueue(new Callback<VideoListResponse>() {
            @Override
            public void onResponse(Call<VideoListResponse> call, Response<VideoListResponse> response) {
                if (response == null)
                    return;

                VideoListResponse videoCallResponse = response.body();
                videoAdapter.setVideoList(videoCallResponse.getResults());
                if(videoAdapter.getVideoList().isEmpty()){
                    tvVideosLabel.setVisibility(View.GONE);
                }
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<VideoListResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Error loading videos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReviews(long id){
        MovieService service = NetworkUtils.getMovieClient().create(MovieService.class);
        final Call<ReviewListResponse> reviewCall = service.getReviewsFromAMovie(id, BuildConfig.MOVIE_API_KEY);
        reviewCall.enqueue(new Callback<ReviewListResponse>() {
            @Override
            public void onResponse(Call<ReviewListResponse> call, Response<ReviewListResponse> response) {
                if(response == null){
                    return;
                }
                ReviewListResponse reviewListResponse = response.body();
                reviewAdapter.setReviewList(reviewListResponse.getResults());
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ReviewListResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Error loading reviews.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMovieInfo(Movie movie) {
        tvTitle.setText(movie.getOriginalTitle());
        Picasso.get()
                .load(movie.getBackdropPathUrl())
                .into(ivBackdrop);
        tvOverview.setText(movie.getOverview());
        Picasso.get()
                .load(movie.getPosterUrl())
                .into(ivPoster);
        tvReleaseDate.setText(movie.getReleaseDate());
        tvVoteAverage.setText(movie.getVoteAverage());
    }

    @OnClick(R.id.details_btn_favorite_star)
    public void addOrDeleteFavoriteMovie(ImageView imageView) {
        if(!isFavorite) {
            imageView.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.star_selected));
            ContentValues contentValues = new ContentValues();

            contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());

            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            if(uri != null) {
                Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
            }

        } else {
            imageView.setImageDrawable(ActivityCompat.getDrawable(this, R.drawable.star));
            String stringId = Long.toString(movie.getId());
            Uri uri = MovieContract.MovieEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();

            getContentResolver().delete(uri, null, null);
        }
        isFavorite = !isFavorite;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MOVIE, movie);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movie = savedInstanceState.getParcelable(EXTRA_MOVIE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
