package movies.popular.popularmoviesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roxan on 4/9/2018.
 */

public class Video implements Parcelable {
    private final String VIDEO_BASE_URL = "http://www.youtube.com/watch?v=";

    private String key;
    private String name;

    public Video() {
    }

    public Video(String key, String name) {
        this.key = key;
        this.name = name;
    }

    protected Video(Parcel in) {
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyUrl() {
        return VIDEO_BASE_URL + key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(VIDEO_BASE_URL);
        dest.writeString(key);
        dest.writeString(name);
    }
}
