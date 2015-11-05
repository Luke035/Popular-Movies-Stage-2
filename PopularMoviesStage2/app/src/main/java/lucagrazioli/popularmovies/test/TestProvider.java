package lucagrazioli.popularmovies.test;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import lucagrazioli.popularmovies.data.MovieContract;
import lucagrazioli.popularmovies.data.MovieProvider;

/**
 * Created by lucagrazioli on 23/10/15.
 */
public class TestProvider extends AndroidTestCase {

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieProvider registered with authority: " + providerInfo.authority +
                    " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    /*public void testSimpleQuery(){
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues posterValues = TestDB.createPosterValues();

        long rowId = sqLiteDatabase.insert(MovieContract.PosterEntry.TABLE_NAME, null, posterValues);

        //RowID must be greater than -1
        assertTrue("ERROR: poster values not correctly inserted", rowId != -1);


        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.PosterEntry.buildPosterWithSortingAndMinumVotes("","100"),
                null,
                null,
                null,
                null
        );


        assertTrue("Poster values are not as expected", validateCursor(posterValues, movieCursor));

    }*/

    public boolean validateCursor(ContentValues testValues, Cursor returnedCursor){
        returnedCursor.moveToNext();

        int title_index = returnedCursor.getColumnIndex(MovieContract.PosterEntry.COL_TITLE);

        if(!testValues.getAsString(MovieContract.PosterEntry.COL_TITLE).equals(
                returnedCursor.getString(title_index))){
            return false;
        }

        return true;
    }

    /*public void testInsert2(){
        ContentValues value = new ContentValues();
        value.put(MovieContract.PosterEntry.COL_TITLE, "Toy story 2");
        value.put(MovieContract.PosterEntry.COL_MOVIE_ID, 283763);
        value.put(MovieContract.PosterEntry.COL_RELEASE_DATE, "2003");
        value.put(MovieContract.PosterEntry.COL_DURATION, 123);
        value.put(MovieContract.PosterEntry.COL_DESCRIPTION, "dskjadklslkdsn");
        value.put(MovieContract.PosterEntry.COL_IMAGE_URL, "http://");
        value.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, 8.5);
        value.put(MovieContract.PosterEntry.COL_POPULARITY, 34.5);
        value.put(MovieContract.PosterEntry.COL_VOTE_COUNT, 100);

        //Uri posterUri = mContext.getContentResolver().insert(MovieContract.PosterEntry.CONTENT_URI, value);

        //long row_id = ContentUris.parseId(posterUri);

        //assertTrue(row_id != -1);

        ContentValues[] values = {value};

       int inserted_rows =  mContext.getContentResolver().bulkInsert(MovieContract.PosterEntry.CONTENT_URI, values);



        assertEquals(1, inserted_rows);
        /*
        public static final String COL_TITLE = "title";
        public static final String COL_MOVIE_ID = "movie_id";
        public static final String COL_RELEASE_DATE = "release_date";
        public static final String COL_DURATION = "duration";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_IMAGE_URL = "image_url";
        public static final String COL_VOTE_AVERAGE = "vote_average";
        public static final String COL_POPULARITY = "popularity";
        public static final String COL_VOTE_COUNT = "vote_count";

    }*/

    public void testInsert() {
        ContentValues [] testValues = TestDB.createMultiplePosterValues();

        Utils.TestContentObserver tco = Utils.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.PosterEntry.CONTENT_URI, true, tco);


        for(ContentValues value : testValues) {
            Uri posterUri = mContext.getContentResolver().insert(MovieContract.PosterEntry.CONTENT_URI, value);

            //Content observer get called?
            tco.waitForNotificationOrFail();
            mContext.getContentResolver().unregisterContentObserver(tco);

            //Parse obtained id from posterUri
            long row_id = ContentUris.parseId(posterUri);
            assertTrue(row_id != -1);
        }

    }

    public void testBulkInsert(){
        ContentValues [] values = TestDB.createMultiplePosterValues();

        int inserted = mContext.getContentResolver().bulkInsert(MovieContract.PosterEntry.CONTENT_URI,
                values);

        assertEquals(values.length, inserted);

        //Try inserting the same values

        inserted = mContext.getContentResolver().bulkInsert(MovieContract.PosterEntry.CONTENT_URI,
                values);

        assertEquals(values.length, inserted);
    }

    public void testIdQuery(){
        ContentValues [] testValues = TestDB.createMultiplePosterValues();
        List<Integer> ids = new ArrayList<Integer>();

        Utils.TestContentObserver tco = Utils.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.PosterEntry.CONTENT_URI, true, tco);


        for(ContentValues value : testValues) {
            Uri posterUri = mContext.getContentResolver().insert(MovieContract.PosterEntry.CONTENT_URI, value);

            //Content observer get called?
            tco.waitForNotificationOrFail();
            mContext.getContentResolver().unregisterContentObserver(tco);

            //Parse obtained id from posterUri
            long row_id = ContentUris.parseId(posterUri);
            ids.add(value.getAsInteger(MovieContract.PosterEntry.COL_MOVIE_ID));
            assertTrue(row_id != -1);
        }

        Uri id_uri = MovieContract.PosterEntry.buildPosterUri(ids.get(0));


        Cursor obtained_cursor = mContext.getContentResolver().query(id_uri,null,null,null,null);

        //assertTrue("Different number of obtained values", obtained_cursor.getCount()==1);

        assertEquals("Wrong record number form URI: "+id_uri,1, obtained_cursor.getCount());

    }


}
