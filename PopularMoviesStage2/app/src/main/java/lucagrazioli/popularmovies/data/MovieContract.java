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
}
