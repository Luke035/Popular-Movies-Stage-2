package lucagrazioli.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by lucagrazioli on 23/10/15.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int POSTER = 100;
    public static final int POSTER_WITH_SORTING = 101;
    public static final int POSTER_WITH_SORTING_AND_MIN_VOTES = 102;
    public static final int TRAILER = 200;

    private MovieDBHelper mMovieDbHelper;

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_POSTER+"/*/*",
                POSTER_WITH_SORTING_AND_MIN_VOTES);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_POSTER+"/*",
                POSTER_WITH_SORTING);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_POSTER,
                POSTER);

        matcher.addURI(MovieContract.CONTENT_AUTHORITY,
               MovieContract.PATH_TRAILER,
                TRAILER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);

        Cursor cursor = null;

        switch(match){
            case POSTER:{ //TODO

            }break;

            case POSTER_WITH_SORTING:{
                //TODO
            }break;

            case POSTER_WITH_SORTING_AND_MIN_VOTES:{
                //TODO
            }break;

            case TRAILER:{
                //TODO
            }break;

            default:new UnsupportedOperationException("Unknown URI: "+uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        //Matching the uri
        int match = sUriMatcher.match(uri);

        //Switch-case on the matched variable
        switch(match){
            case POSTER: return MovieContract.PosterEntry.CONTENT_TYPE;
            case POSTER_WITH_SORTING: return MovieContract.PosterEntry.CONTENT_TYPE;
            case POSTER_WITH_SORTING_AND_MIN_VOTES: return MovieContract.PosterEntry.CONTENT_TYPE;
            case TRAILER: return MovieContract.TrailerEntry.CONTENT_TYPE;

            default: {
                throw new UnsupportedOperationException("Unknown URI: "+uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
