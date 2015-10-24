package lucagrazioli.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by lucagrazioli on 15/10/15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "lucagrazioli.popularmovies.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_POSTER = PosterEntry.TABLE_NAME;
    public static final String PATH_TRAILER = TrailerEntry.TABLE_NAME;

    //Poster contract inner class
    public static final class PosterEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_POSTER).build();

        //Base URIs Strings useful for appending query parameters, and then buiding the URI
        //String example:  CONTENT_TYPE: vnd.android.cursor.dir/lucagrazioli.popularmovies.app/poster
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+
                "/"+PATH_POSTER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+
                "/"+PATH_POSTER;

        public static final String TABLE_NAME = "poster";

        public static final String COL_TITLE = "title";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_DURATION = "duration";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "image_url";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";

        /*
        URI contructur returning the URI for a specific poster entry
         */
        public static Uri buildPosterUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //Used appendPath instead of appendQueryParam because there's no sorting order column
        public static Uri buildPosterWithSorting(String sort_order){
            return CONTENT_URI.buildUpon().appendPath(sort_order).build();
        }

        public static Uri buildPosterWithSortingAndMinumVotes(String sort_order, String min_votes){
            return CONTENT_URI.buildUpon().appendPath(sort_order).appendPath(min_votes).build();
        }
    }

    //Trailer contract inner class
    public static final class TrailerEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().
                appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+
                "/"+PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+
                "/"+PATH_TRAILER;

        public static final String TABLE_NAME = "trailer";

        public static final String COL_NAME = "name";
        public static final String COL_SITE = "site";
        public static final String COL_TRAILER_KEY = "key"; //The unique trailer's id on the player website
        public static final String COL_SIZE = "size";
        public static final String COL_TYPE = "type";

        //Movie foreign key
        public static final String COL_MOVIE_ID = "movie";

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
