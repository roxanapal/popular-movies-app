package movies.popular.popularmoviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.popular.popularmoviesapp.models.Movie;

public class DetailsActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";
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
        }

        Movie movie = intent.getParcelableExtra(EXTRA_MOVIE);
        if(movie.getId() == DEFAULT_ID){
            errorMessage();
            return;
        }

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

    private void errorMessage(){
        Toast.makeText(this, R.string.details_data_error, Toast.LENGTH_SHORT).show();
    }
}
