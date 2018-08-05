package movies.popular.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by roxan on 4/9/2018.
 */

public class VideoListResponse implements Parcelable {
    private List<Video> results;

    private VideoListResponse(Parcel in) {
        results = in.createTypedArrayList(Video.CREATOR);
    }

    public static final Creator<VideoListResponse> CREATOR = new Creator<VideoListResponse>() {
        @Override
        public VideoListResponse createFromParcel(Parcel in) {
            return new VideoListResponse(in);
        }

        @Override
        public VideoListResponse[] newArray(int size) {
            return new VideoListResponse[size];
        }
    };

    public List<Video> getResults(){
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
