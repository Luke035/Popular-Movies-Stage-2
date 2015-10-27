package lucagrazioli.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
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

    private static final SQLiteQueryBuilder sPosterQueryBuilder;

    static {
        sPosterQueryBuilder = new SQLiteQueryBuilder();

        //This is the simple poster query builder, without trailer informations
        sPosterQueryBuilder.setTables(MovieContract.PosterEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder sPosterAndTrailerQueryBuilder;

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
                        MovieContract.PosterEntry.TABLE_NAME+"."+ MovieContract.PosterEntry._ID
        );
    }


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

    private Cursor getTrailersFromMovie(
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

            case POSTER_WITH_SORTING:{
                cursor = getPosters(uri, projection, sortOrder);
            }break;

            case POSTER_WITH_SORTING_AND_MIN_VOTES:{
                cursor = getPostersWithMinVotes(uri, projection, sortOrder);
            }break;

            case TRAILER:{
                cursor = getTrailersFromMovie(uri,projection,sortOrder);
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
