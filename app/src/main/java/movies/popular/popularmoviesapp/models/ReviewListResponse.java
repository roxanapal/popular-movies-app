package movies.popular.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by roxan on 4/10/2018.
 */

public class ReviewListResponse implements Parcelable {
    private List<Review> results;

    private ReviewListResponse(Parcel in) {
        results = in.createTypedArrayList(Review.CREATOR);
    }

    public List<Review> getResults(){
        return results;
    }

    public static final Creator<ReviewListResponse> CREATOR = new Creator<ReviewListResponse>() {
        @Override
        public ReviewListResponse createFromParcel(Parcel in) {
            return new ReviewListResponse(in);
        }

        @Override
        public ReviewListResponse[] newArray(int size) {
            return new ReviewListResponse[size];
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
