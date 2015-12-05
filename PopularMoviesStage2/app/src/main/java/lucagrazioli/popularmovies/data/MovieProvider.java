package lucagrazioli.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by lucagrazioli on 23/10/15.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int POSTER = 100;
    public static final int POSTER_WITH_SORTING = 101;
    public static final int POSTER_WITH_SORTING_AND_MIN_VOTES = 102;
    public static final int POSTER_WITH_ID = 103;
    public static final int TRAILER = 200;

    private MovieDBHelper mMovieDbHelper;

    private static final String LOG_TAG = "Provider";

    public static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //content://lucagrazioli.popularmovies.app/poster/84

        matcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_POSTER+"/#",
                POSTER_WITH_ID);
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

    private static final SQLiteQueryBuilder sPosterQueryBuilder;

    static {
        sPosterQueryBuilder = new SQLiteQueryBuilder();

        //This is the simple poster query builder, without trailer informations
        sPosterQueryBuilder.setTables(MovieContract.PosterEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder sTrailerQueryBuilder;

    static{
        sTrailerQueryBuilder = new SQLiteQueryBuilder();

        sTrailerQueryBuilder.setTables(MovieContract.TrailerEntry.TABLE_NAME);
    }

    /*private static final SQLiteQueryBuilder sPosterAndTrailerQueryBuilder;

    static{
        sPosterAndTrailerQueryBuilder = new SQLiteQueryBuilder();

        //This query builder must perform the join between the two tables.
        //Remember: for each trailer only one film is associated, but fora a single
        //film multiple trailers can be bounded
        sPosterAndTrailerQueryBuilder.setTables(
                MovieContract.TrailerEntry.TABLE_NAME+" INNER JOIN "+
                        MovieContract.PosterEntry.TABLE_NAME+
                        " ON "+ MovieContract.TrailerEntry.TABLE_NAME+"" +
                        "."+ MovieContract.TrailerEntry.COL_MOVIE_ID+" = "+
                        MovieContract.PosterEntry.TABLE_NAME+"."+ MovieContract.PosterEntry.COL_MOVIE_ID
        );
    }*/


    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDBHelper(getContext());
        return true;
    }


    //vote_count >= ?
    private static final String sMinVotesSelection = MovieContract.PosterEntry.TABLE_NAME+
            "."+ MovieContract.PosterEntry.COL_VOTE_COUNT+" = ?";

    //trailer.movie = ?
    private static final String sTrailerSelection = MovieContract.TrailerEntry.TABLE_NAME+
            "."+ MovieContract.TrailerEntry.COL_MOVIE_ID+" = ?";

    private static final String sIDSelection = MovieContract.PosterEntry.TABLE_NAME+
            "."+ MovieContract.PosterEntry.COL_MOVIE_ID+" = ?";

    //No specific selection must be applied
    private Cursor getPosters(
            Uri uri, String [] projection, String sortOrder){

        //String posterSortOrder = MovieContract.PosterEntry.getSortingOrder(uri);

        return sPosterQueryBuilder.query(
                mMovieDbHelper.getReadableDatabase(),
                projection,
                null, //no selections
                null,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getPostersWithId(
            Uri uri, String [] projection, String sortOrder){

        Log.d("Detail_log", "called get poster with id");

        Log.d("Detail_log", "movie_id: "+uri.getLastPathSegment());

        return mMovieDbHelper.getReadableDatabase().query(MovieContract.PosterEntry.TABLE_NAME,
                projection,
                MovieContract.PosterEntry.COL_MOVIE_ID + "= " + uri.getLastPathSegment(),
                null,
                null,
                null,
                null);

        /*return sPosterQueryBuilder.query(
                mMovieDbHelper.getReadableDatabase(),
                projection,
                sIDSelection,
                null,
                null,
                null,
                sortOrder
        );*/
    }

    private Cursor getPostersWithMinVotes(
            Uri uri, String [] projection, String sortOrder){
        int minVotes = MovieContract.PosterEntry.getVoteCount(uri);

        return sPosterQueryBuilder.query(
                mMovieDbHelper.getReadableDatabase(),
                projection,
                sMinVotesSelection,
                new String[]{Integer.toString(minVotes)},
                null,
                null,
                sortOrder
        );
    }


    /*private Cursor getTrailersFromMovie(
            Uri uri, String[] projection, String sortOrder){

        long id = MovieContract.TrailerEntry.getMovieId(uri);

        return  sPosterAndTrailerQueryBuilder.query(
          mMovieDbHelper.getReadableDatabase(),
                projection,
                sTrailerSelection,
                new String[]{Long.toString(id)},
                null,
                null,
                sortOrder
        );
    }*/

    private Cursor getTrailers(Uri uri, String [] projection, String sortOrder, String selection, String [] selectionArgs){
        return sTrailerQueryBuilder.query(
          mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);

        Cursor cursor = null;

        switch(match){
            case POSTER:{
                cursor = getPosters(uri, projection, sortOrder);
            }break;

            case POSTER_WITH_ID:{
                cursor = getPostersWithId(uri, projection, sortOrder);
            }break;

            case POSTER_WITH_SORTING:{
                cursor = getPosters(uri, projection, sortOrder);
            }break;

            case POSTER_WITH_SORTING_AND_MIN_VOTES:{
                cursor = getPostersWithMinVotes(uri, projection, sortOrder);
            }break;

            case TRAILER:{
                //cursor = getTrailersFromMovie(uri,projection,sortOrder);
                cursor = getTrailers(uri, projection, sortOrder, selection, selectionArgs);
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
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        Uri returnUri = null;

        switch(match){
            case POSTER:{
                long _id = db.insert(MovieContract.PosterEntry.TABLE_NAME, null, values);

                if(_id > 0){
                    //Inserted correctly
                    returnUri = MovieContract.PosterEntry.buildPosterUri(_id);
                }else{
                    //id = -1 could be due to a duplicate movie_id_col, update instead insert necessary

                    _id = db.replace(MovieContract.PosterEntry.TABLE_NAME, null, values);
                    if(_id > 0){
                        returnUri = MovieContract.PosterEntry.buildPosterUri(_id);
                    }else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
            }break;
            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);

                if(_id > 0){
                    //Inserted correctly
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into "+uri);
                }
            }break;
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);}
        }

        getContext().getContentResolver().notifyChange(uri,null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        int deletedRows = 0;

        switch (match){
            case POSTER:{
                deletedRows = db.delete(MovieContract.PosterEntry.TABLE_NAME, selection, selectionArgs);
            }break;

            case TRAILER:{
                deletedRows = db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
            }break;

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);}
        }

        if(deletedRows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        db.close();
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        int updatedRows = 0;

        switch (match){
            case POSTER:{
                updatedRows = db.update(MovieContract.PosterEntry.TABLE_NAME, values, selection, selectionArgs);
            }break;

            case TRAILER:{
                updatedRows = db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
            }break;

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);}
        }

        if(updatedRows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        db.close();
        return updatedRows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues [] values){
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;

        switch(match){
            case POSTER:{
                db.beginTransaction();

                try {
                    returnCount = 0;
                    for (ContentValues value : values) {

                        //Debug code
                        int movie_id = value.getAsInteger(MovieContract.PosterEntry.COL_MOVIE_ID);
                        Log.d(LOG_TAG, "Movie_id: "+movie_id);

                        long _id = db.insert(MovieContract.PosterEntry.TABLE_NAME, null, value);

                        if (_id != -1)
                            returnCount++;
                        else{

                            //id = -1 could be due to a duplicate movie_id_col, update instead insert necessary
                            //in this case the record is already insrted in the db
                            _id = db.replace(MovieContract.PosterEntry.TABLE_NAME, null, value);
                            if(_id != -1)
                                returnCount++;
                        }

                        Log.d(LOG_TAG, "Added id"+_id);
                    }

                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
            }break;

            case TRAILER: {
                db.beginTransaction();

                try {
                    returnCount = 0;
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);

                        if (_id != -1)
                            returnCount++;


                        Log.d(LOG_TAG, "Added id"+_id);
                    }

                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
            }break;

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);}
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }
}
