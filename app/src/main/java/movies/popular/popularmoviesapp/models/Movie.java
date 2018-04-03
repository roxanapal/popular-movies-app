package movies.popular.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by roxan on 3/10/2018.
 */

public class Movie implements Parcelable {
    private final String IMG_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private long id;
    @SerializedName("original_title")
    private String originalTitle;
    private String overview;
    @SerializedName("poster_path")
    private String poster;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private String voteAverage;

    public Movie() {
    }

    public Movie(long id, String originalTitle, String overview, String poster, String backdropPath, String releaseDate, String voteAverage) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.poster = poster;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readLong();
        originalTitle = in.readString();
        overview = in.readString();
        poster = in.readString();
        backdropPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(poster);
        dest.writeString(backdropPath);
        dest.writeString(releaseDate);
        dest.writeString(voteAverage);
    }

    public String getPosterUrl() {
        return IMG_BASE_URL + poster;
    }

    public String getBackdropPathUrl() {
        return IMG_BASE_URL + backdropPath;
    }
}
