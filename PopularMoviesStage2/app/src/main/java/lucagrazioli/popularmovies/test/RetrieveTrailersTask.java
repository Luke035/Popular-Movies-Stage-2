package lucagrazioli.popularmovies.test;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lucagrazioli.popularmovies.NullJSONStringException;
import lucagrazioli.popularmovies.R;
import lucagrazioli.popularmovies.TrailersDataParser;
import lucagrazioli.popularmovies.data.MovieContract;

/**
 * Created by lucagrazioli on 03/12/15.
 */
public class RetrieveTrailersTask extends AsyncTask<String, Void, Void> {

    private static final String PARAM_API_KEY = "api_key";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String VIDEOS_PARAM = "videos";

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String LOG_TAG = "Trailer task";

    private final Context mContext;

    public RetrieveTrailersTask(Context mContext, SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mContext = mContext;
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }

    private void extractTrailers(String jsonString, String movieID) throws NullJSONStringException, JSONException {
        Vector<ContentValues> cvVector = new Vector<ContentValues>();
        try {
            int i = 0;
            String title = TrailersDataParser.getNameByIndex(jsonString, i);

            while (title != null) {
                String site = TrailersDataParser.getSiteByIndex(jsonString, i);
                String key = TrailersDataParser.getKeyByIndex(jsonString, i);
                String size = TrailersDataParser.getSizeByIndex(jsonString, i);
                String type = TrailersDataParser.getTypeByIndex(jsonString, i);
                String id = TrailersDataParser.getTrailerIDByIndex(jsonString, i);

                ContentValues value = new ContentValues();
                value.put(MovieContract.TrailerEntry.COL_NAME, title);
                value.put(MovieContract.TrailerEntry.COL_SITE, site);
                value.put(MovieContract.TrailerEntry.COL_TRAILER_KEY, key);
                value.put(MovieContract.TrailerEntry.COL_SIZE, size);
                value.put(MovieContract.TrailerEntry.COL_TYPE, type);
                value.put(MovieContract.TrailerEntry.COL_MOVIE_ID, movieID);
                value.put(MovieContract.TrailerEntry.COL_TRAILER_ID, id);

                cvVector.add(value);

                i++;

                title = TrailersDataParser.getNameByIndex(jsonString, i);

                //bulk insert call
                int inserted = 0;
                if(cvVector.size() > 0){
                    ContentValues [] cvArray = new ContentValues[cvVector.size()];
                    cvVector.toArray(cvArray);

                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI,
                            cvArray);

                    Log.d("", "Inserted "+inserted+" record in the DB");
                }

            }
        }catch(NullJSONStringException e){
            throw e;
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        //Params contains the ids of moives to be retrieved
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        for(String movieId : params){
            try {

                if(movieId == null || movieId.equals("")){
                    break;
                }

                Uri buildtUri = Uri.parse(BASE_URL)
                        .buildUpon()
                        .appendPath(movieId)
                        .appendPath(VIDEOS_PARAM)
                        .appendQueryParameter(PARAM_API_KEY, mContext.getString(R.string.api_key))
                        .build();
                Log.d("Trailer task", buildtUri.toString());

                URL url = new URL(buildtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                List<Byte> data = new ArrayList<Byte>();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Trailer task", "Error closing stream", e);
                    }
                }
            }

            try {
                extractTrailers(moviesJsonStr, movieId);
            } catch (NullJSONStringException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            moviesJsonStr = "";
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
