package lucagrazioli.popularmovies.test;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import lucagrazioli.popularmovies.data.MovieContract;
import lucagrazioli.popularmovies.data.MovieDBHelper;
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


    public void testSimpleQuery(){
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

    }

    public boolean validateCursor(ContentValues testValues, Cursor returnedCursor){
        returnedCursor.moveToNext();

        int title_index = returnedCursor.getColumnIndex(MovieContract.PosterEntry.COL_TITLE);

        if(!testValues.getAsString(MovieContract.PosterEntry.COL_TITLE).equals(
                returnedCursor.getString(title_index))){
            return false;
        }

        return true;
    }
}
