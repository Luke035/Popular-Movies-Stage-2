package lucagrazioli.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucagrazioli on 07/08/15.
 */
public class MoviesDataParser {
    private static final String RESULT_KEY = "results";
    private static final String TITLE_KEY = "original_title";
    private static final String OVERVIEW_KEY = "overview";
    private static final String POSTER_URL_KEY = "poster_path";
    private static final String VOTE_KEY = "vote_average";
    private static final String RELEASE_DATE_KEY = "release_date";
    private static final String POPULARITY_KEY = "popularity";
    private static final String VOTE_COUNT_KEY = "vote_count";
    private static final String ID_KEY = "id";

    public static int getNumberOfMovies(String moviesJsonString) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        return movies.length();
    }

    public static String getReleaseDateByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(RELEASE_DATE_KEY);
        } else {
            return null;
        }
    }

    public static String getTitleByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(TITLE_KEY);
        } else {
            return null;
        }
    }

    public static String getOverviewByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(OVERVIEW_KEY);
        } else {
            return null;
        }
    }

    public static double getPopularutyByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return Double.parseDouble(desiredMovie.getString(POPULARITY_KEY));
        } else {
            return 0.0;
        }
    }

    public static int getVoteCountByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return Integer.parseInt(desiredMovie.getString(VOTE_COUNT_KEY));
        } else {
            return -1;
        }
    }

    public static int getMovieIdByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return Integer.parseInt(desiredMovie.getString(ID_KEY));
        } else {
            return -1;
        }
    }

    public static String getPosterPathByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(POSTER_URL_KEY);
        } else {
            return null;
        }
    }

    public static double getVoteByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getDouble(VOTE_KEY);
        } else {
            return 0.0;
        }
    }
}