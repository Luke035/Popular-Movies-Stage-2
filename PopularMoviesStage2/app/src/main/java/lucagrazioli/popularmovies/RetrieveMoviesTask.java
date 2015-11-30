package lucagrazioli.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lucagrazioli.popularmovies.data.MovieContract;

/**
 * Created by lucagrazioli on 02/11/15.
 */
public class RetrieveMoviesTask extends AsyncTask <String, Void, Void> {

    private static final String PARAM_SORT = "sort_by";
    private static final String PARAM_API_KEY = "api_key";
    private static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";
    private static final String PARAM_VOTE_COUNT_GTE = "vote_count.gte";

    private static final String VALUE_PARAM_VOTE_COUNT = "100";
    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private PostersAdapter mPosterAdapter;
    private final Context mContext;
    private GridView postersGrid;
    private int landscape_columns;
    private int portrait_columns;

    public RetrieveMoviesTask(PostersAdapter mPosterAdapter, Context mContext, GridView postersGrid, int landscape_columns, int portrait_columns) {
        this.mPosterAdapter = mPosterAdapter;
        this.mContext = mContext;
        this.postersGrid = postersGrid;
        this.landscape_columns = landscape_columns;
        this.portrait_columns = portrait_columns;
    }


    private void extractPosters(String jsonString) throws JSONException, NullJSONStringException {
        try {


            Vector<ContentValues> cvVector = new Vector<ContentValues>();
            int i = 0;
            String title = MoviesDataParser.getTitleByIndex(jsonString, i);
            while (title != null) {
                String posterPath = MoviesDataParser.getPosterPathByIndex(jsonString, i);
                double voteAverage = MoviesDataParser.getVoteByIndex(jsonString, i);
                String overview = MoviesDataParser.getOverviewByIndex(jsonString, i);
                String releaseDate = MoviesDataParser.getReleaseDateByIndex(jsonString, i);
                double popularity = MoviesDataParser.getPopularutyByIndex(jsonString, i);
                int vote_count = MoviesDataParser.getVoteCountByIndex(jsonString, i);
                int movie_id = MoviesDataParser.getMovieIdByIndex(jsonString, i);


                ContentValues value = new ContentValues();
                value.put(MovieContract.PosterEntry.COL_TITLE, title);
                value.put(MovieContract.PosterEntry.COL_DESCRIPTION, overview);
                value.put(MovieContract.PosterEntry.COL_VOTE_COUNT, vote_count);
                value.put(MovieContract.PosterEntry.COL_POPULARITY, popularity);
                value.put(MovieContract.PosterEntry.COL_DURATION, 120);
                value.put(MovieContract.PosterEntry.COL_IMAGE_URL, posterPath);
                value.put(MovieContract.PosterEntry.COL_RELEASE_DATE, releaseDate);
                value.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, voteAverage);
                value.put(MovieContract.PosterEntry.COL_MOVIE_ID, movie_id);
                cvVector.add(value);

                i++;


                title = MoviesDataParser.getTitleByIndex(jsonString, i);
            }

            //bulk insert call
            int inserted = 0;
            if(cvVector.size() > 0){
                ContentValues [] cvArray = new ContentValues[cvVector.size()];
                cvVector.toArray(cvArray);

                inserted = mContext.getContentResolver().bulkInsert(MovieContract.PosterEntry.CONTENT_URI,
                        cvArray);

                Log.d(LOG_TAG, "Inserted "+inserted+" record in the DB");
            }

        }catch(NullJSONStringException e){
            throw e;
        }
    }

    @Override
    protected Void doInBackground(String... sortingParam) {
        String sorting_param = sortingParam[0];
        Log.d(LOG_TAG, "task started");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {
            Uri builtUri;
            if(sorting_param.equals(mContext.getString(R.string.pref_sorting_vote_key))) {
                //Add a vote_count.gte useful for avoiding movies with a low number of votes
                //vote_count.gte parameter is set to 100 by default
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(PARAM_VOTE_COUNT_GTE, VALUE_PARAM_VOTE_COUNT)
                        .appendQueryParameter(PARAM_SORT, sorting_param)
                        .appendQueryParameter(PARAM_API_KEY, mContext.getString(R.string.api_key))
                        .build();
            }else{
                builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(PARAM_SORT, sorting_param)
                        .appendQueryParameter(PARAM_API_KEY, mContext.getString(R.string.api_key))
                        .build();
            }


            URL url = new URL(builtUri.toString());
            Log.d(LOG_TAG, url.toString());

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
            Log.d(LOG_TAG,moviesJsonStr);
        }catch(ProtocolException e){
            e.printStackTrace();
            Log.e(LOG_TAG,"Error",e);
        }catch (IOException e){
            e.printStackTrace();
            Log.e(LOG_TAG,"Error",e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            extractPosters(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage());
            e.printStackTrace();
        } catch (NullJSONStringException e ){
            Log.e(LOG_TAG,e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
