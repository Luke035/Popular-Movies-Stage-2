package lucagrazioli.popularmovies.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import lucagrazioli.popularmovies.data.MovieContract;
import lucagrazioli.popularmovies.data.MovieDBHelper;

/**
 * Created by lucagrazioli on 16/10/15.
 */
public class TestDB extends AndroidTestCase {

    public static final String LOG_TAG = TestDB.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDBHelper.DB_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(MovieDBHelper.DB_NAME);


        //For both DB tables
        SQLiteDatabase db = new MovieDBHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        //Cursor posterCursor = db.rawQuery("PRAGMA table_info(" + MovieContract.PosterEntry.TABLE_NAME + ")",
               // null);

        Cursor posterCursor = db.rawQuery("SELECT * FROM "+ MovieContract.PosterEntry.TABLE_NAME,null);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> posterColumnsHashSet = new HashSet<String>();
        posterColumnsHashSet.add(MovieContract.PosterEntry._ID);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_TITLE);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_DESCRIPTION);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_DURATION);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_IMAGE_URL);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_RELEASE_DATE);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_VOTE_AVERAGE);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_POPULARITY);
        posterColumnsHashSet.add(MovieContract.PosterEntry.COL_VOTE_COUNT);

        posterCursor.moveToNext(); //Necessary for start iterating the cursor

        assertEquals(10, posterCursor.getColumnCount());

        posterCursor = db.rawQuery("PRAGMA table_info(" + MovieContract.PosterEntry.TABLE_NAME + ")",
                 null);
        posterCursor.moveToNext();

        int columnNameIndex = posterCursor.getColumnIndex("name");
        do {
            String columnName = posterCursor.getString(columnNameIndex);
            posterColumnsHashSet.remove(columnName);
        } while(posterCursor.moveToNext());
        //
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                posterColumnsHashSet.isEmpty());

        Cursor trailerCursor = db.rawQuery("PRAGMA table_info(" + MovieContract.TrailerEntry.TABLE_NAME + ")",
                null);

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> trailerColumnHashSet = new HashSet<String>();
        trailerColumnHashSet.add(MovieContract.TrailerEntry._ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_NAME);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_SITE);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_TRAILER_KEY);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_SIZE);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_TYPE);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COL_MOVIE_ID);

        trailerCursor.moveToNext(); //Necessary for start iterating the cursor

        assertEquals(6, trailerCursor.getColumnCount());

        int trailerColumnNameIndex = trailerCursor.getColumnIndex("name");
        do {
            String columnName = trailerCursor.getString(trailerColumnNameIndex);
            trailerColumnHashSet.remove(columnName);
        } while(trailerCursor.moveToNext());
        //
        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                trailerColumnHashSet.isEmpty());

        db.close();
   }

    //    static ContentValues createNorthPoleLocationValues() {
//        // Create a new map of values, where column names are the keys
//        ContentValues testValues = new ContentValues();
//        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, TEST_LOCATION);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, "North Pole");
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, 64.7488);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, -147.353);
//
//        return testValues;
//    }

    static ContentValues createPosterValues(){
        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.PosterEntry.COL_MOVIE_ID, "66745565");
        testValues.put(MovieContract.PosterEntry.COL_TITLE, "Minions");
        testValues.put(MovieContract.PosterEntry.COL_DESCRIPTION, "ok!");
        testValues.put(MovieContract.PosterEntry.COL_DURATION, 120);
        testValues.put(MovieContract.PosterEntry.COL_IMAGE_URL, "http://");
        testValues.put(MovieContract.PosterEntry.COL_RELEASE_DATE, "2015");
        testValues.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, 8.5);
        testValues.put(MovieContract.PosterEntry.COL_POPULARITY, 34.75);
        testValues.put(MovieContract.PosterEntry.COL_VOTE_COUNT, 135);

        return testValues;
    }

    static ContentValues[] createMultiplePosterValues(){
        ContentValues testValues1 = new ContentValues();

        testValues1.put(MovieContract.PosterEntry.COL_MOVIE_ID, "38482173");
        testValues1.put(MovieContract.PosterEntry.COL_TITLE, "Men In Black 3");
        testValues1.put(MovieContract.PosterEntry.COL_DESCRIPTION, "Last episode of MIB");
        testValues1.put(MovieContract.PosterEntry.COL_DURATION, 120);
        testValues1.put(MovieContract.PosterEntry.COL_IMAGE_URL, "http://");
        testValues1.put(MovieContract.PosterEntry.COL_RELEASE_DATE, "2013");
        testValues1.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, 8.5);
        testValues1.put(MovieContract.PosterEntry.COL_POPULARITY, 34.75);
        testValues1.put(MovieContract.PosterEntry.COL_VOTE_COUNT, 135);

        ContentValues testValues2 = new ContentValues();

        testValues2.put(MovieContract.PosterEntry.COL_MOVIE_ID, "34555465");
        testValues2.put(MovieContract.PosterEntry.COL_TITLE, "The martian");
        testValues2.put(MovieContract.PosterEntry.COL_DESCRIPTION, "Matt Damon Superstar");
        testValues2.put(MovieContract.PosterEntry.COL_DURATION, 120);
        testValues2.put(MovieContract.PosterEntry.COL_IMAGE_URL, "http://");
        testValues2.put(MovieContract.PosterEntry.COL_RELEASE_DATE, "2015");
        testValues2.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, 8.5);
        testValues2.put(MovieContract.PosterEntry.COL_POPULARITY, 34.75);
        testValues2.put(MovieContract.PosterEntry.COL_VOTE_COUNT, 135);

        ContentValues [] values = {testValues1, testValues2};

        return values;
    }

    static ContentValues createTrailerValues(){
        ContentValues testValues = new ContentValues();

        testValues.put(MovieContract.TrailerEntry.COL_NAME, "Trailer 3");
        testValues.put(MovieContract.TrailerEntry.COL_SITE, "You Tube");
        testValues.put(MovieContract.TrailerEntry.COL_TRAILER_KEY, "dlsjfhlsdfhdv34");
        testValues.put(MovieContract.TrailerEntry.COL_SIZE, "720");
        testValues.put(MovieContract.TrailerEntry.COL_TYPE, "teaser");
        testValues.put(MovieContract.TrailerEntry.COL_MOVIE_ID, 1);
        return testValues;
    }

    public void testInsertValues(){
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = db.insert(MovieContract.PosterEntry.TABLE_NAME, null, createPosterValues());

        //RowID must be greater than -1
        assertTrue("ERROR: poster values not correctly inserted", rowId != -1);

        long trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, createTrailerValues());

        assertTrue("ERROR: trailer values not correctly inserted", trailerRowId != -1);

        db.close();

    }

    public void testDBQuery(){
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        //Re-insert values
        ContentValues posterValues = createPosterValues();
        long rowId = db.insert(MovieContract.PosterEntry.TABLE_NAME, null, posterValues);

        //RowID must be greater than -1
        assertTrue("ERROR: poster values not correctly inserted", rowId != -1);

        ContentValues trailerValues = createTrailerValues();
        long trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, trailerValues);

        assertTrue("ERROR: trailer values not correctly inserted", trailerRowId != -1);


        //Query the db
        String [] desired_columns = {MovieContract.PosterEntry.COL_TITLE, MovieContract.PosterEntry.COL_DESCRIPTION};
        Cursor posterCursor = db.query(MovieContract.PosterEntry.TABLE_NAME,
                desired_columns,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(posterCursor.moveToNext()){
            int index_col_title = posterCursor.getColumnIndex(MovieContract.PosterEntry.COL_TITLE);
            assertEquals("ERROR: different poster titles",posterCursor.getString(index_col_title), posterValues.getAsString(MovieContract.PosterEntry.COL_TITLE));

            int index_col_description = posterCursor.getColumnIndex(MovieContract.PosterEntry.COL_DESCRIPTION);
            assertEquals("ERROR: different poster descriptions", posterCursor.getString(index_col_description), posterValues.getAsString(MovieContract.PosterEntry.COL_DESCRIPTION));
        }



    }

}
