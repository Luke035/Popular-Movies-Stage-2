package lucagrazioli.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by lucagrazioli on 15/10/15.
 */
public class MovieContract {

    //Poster contract inner class
    public static final class PosterEntry implements BaseColumns{
        public static final String TABLE_NAME = "poster";

        public static final String COL_TITLE = "title";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_DURATION = "duration";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "image_url";
        public static final String COL_VOTE_AVERAGE = "vote_average";

    }

    //Trailer contract inner class
    public static final class TrailerEntry implements BaseColumns{
        public static final String TABLE_NAME = "trailer";

        public static final String COL_NAME = "name";
        public static final String COL_SITE = "site";
        public static final String COL_TRAILER_KEY = "key"; //The unique trailer's id on the player website
        public static final String COL_SIZE = "size";
        public static final String COL_TYPE = "type";

        //Movie foreign key
        public static final String COL_MOVIE_ID = "movie";
    }
}
