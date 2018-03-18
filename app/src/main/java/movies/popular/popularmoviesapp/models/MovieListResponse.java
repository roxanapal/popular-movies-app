package movies.popular.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by roxan on 3/10/2018.
 */

public class MovieListResponse implements Parcelable{
    private List<Movie> results;

    protected MovieListResponse(Parcel in) {
        results = in.createTypedArrayList(Movie.CREATOR);
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }


    public static final Creator<MovieListResponse> CREATOR = new Creator<MovieListResponse>() {
        @Override
        public MovieListResponse createFromParcel(Parcel in) {
            return new MovieListResponse(in);
        }

        @Override
        public MovieListResponse[] newArray(int size) {
            return new MovieListResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }
}
