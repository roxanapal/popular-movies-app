package movies.popular.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by roxan on 4/16/2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "movies.popular.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies";

        // COLUMNS
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "title";
    }
}
