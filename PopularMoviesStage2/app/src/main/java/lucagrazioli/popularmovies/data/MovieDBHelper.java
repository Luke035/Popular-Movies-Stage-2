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

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE "+MovieContract.TrailerEntry.TABLE_NAME+ "("+
                MovieContract.TrailerEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+

                MovieContract.TrailerEntry.COL_NAME+" TEXT NOT NULL, "+
                MovieContract.TrailerEntry.COL_SITE+" TEXT NOT NULL, "+
                MovieContract.TrailerEntry.COL_TRAILER_KEY+" TEXT NOT NULL, "+
                MovieContract.TrailerEntry.COL_SIZE+" TEXT NOT NULL, "+
                MovieContract.TrailerEntry.COL_TYPE+" TEXT NOT NULL, "+
                MovieContract.TrailerEntry.COL_MOVIE_ID+" INTEGER NOT NULL, "+

                "FOREIGN KEY ("+ MovieContract.TrailerEntry.COL_MOVIE_ID+") REFERENCES "+
                MovieContract.PosterEntry.TABLE_NAME+" ("+ MovieContract.PosterEntry._ID+")"

                +");";

        db.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop already existing tables
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.PosterEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.TrailerEntry.TABLE_NAME);

        //Recreate the db
        onCreate(db);
    }
}
