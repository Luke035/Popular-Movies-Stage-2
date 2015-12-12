package lucagrazioli.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucagrazioli on 12/12/15.
 */
public class ReviewDataParser {

    private static final String RESULT_KEY = "results";
    private static final String AUTHOR_KEY = "author";
    private static final String CONTENT_KEY = "content";
    private static final String ID_KEY = "id";

    public static String getIDByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(ID_KEY);
        } else {
            return null;
        }
    }


    public static String getAuthorByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(AUTHOR_KEY);
        } else {
            return null;
        }
    }

    public static String getContentByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(CONTENT_KEY);
        } else {
            return null;
        }
    }
}
