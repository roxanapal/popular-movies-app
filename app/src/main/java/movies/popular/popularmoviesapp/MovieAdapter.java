package movies.popular.popularmoviesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.midi.MidiOutputPort;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import movies.popular.popularmoviesapp.data.MovieContract;
import movies.popular.popularmoviesapp.models.Movie;

/**
 * Created by roxan on 3/10/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Listener listener;
    private List<Movie> movieList = new ArrayList<>();

    public MovieAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        Picasso.get()
                .load(movie.getPosterUrl())
                .into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(movie.getOriginalTitle());
        holder.ivMoviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.openDetailsActivity(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void addMovieToList(Movie movie) {
        synchronized (this) {
            movieList.add(movie);
            notifyDataSetChanged();
        }
    }

    public void clearList() {
        movieList.clear();
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_item_movie)
        ImageView ivMoviePoster;

        @BindView(R.id.tv_item_movie_title)
        TextView tvMovieTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Listener {
        void openDetailsActivity(Movie movie);
    }
}
