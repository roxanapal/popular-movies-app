package movies.popular.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import movies.popular.popularmoviesapp.data.MovieContract.MovieEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID                      + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE             + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);

    }

    // When the database version is incremented
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
