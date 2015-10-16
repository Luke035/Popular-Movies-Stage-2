package lucagrazioli.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lucagrazioli on 16/10/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public static final String DB_NAME = "movie.db";

    public MovieDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_POSTER_TABLE = "CREATE TABLE "+ MovieContract.PosterEntry.TABLE_NAME+ " ("+

                MovieContract.PosterEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+

                MovieContract.PosterEntry.COL_TITLE+" TEXT NOT NULL, "+
                MovieContract.PosterEntry.COL_RELEASE_DATE+" TEXT NOT NULL, "+
                MovieContract.PosterEntry.COL_DURATION+" INTEGER NOT NULL, "+
                MovieContract.PosterEntry.COL_DESCRIPTION+" TEXT NOT NULL, "+
                MovieContract.PosterEntry.COL_IMAGE_URL+" TEXT NOT NULL, "+
                MovieContract.PosterEntry.COL_VOTE_AVERAGE+" REAL NOT NULL"+

                ");";

        db.execSQL(SQL_CREATE_POSTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
