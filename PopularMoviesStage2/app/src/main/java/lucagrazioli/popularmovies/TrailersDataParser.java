package lucagrazioli.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucagrazioli on 03/12/15.
 */
public class TrailersDataParser {

    private static final String RESULT_KEY = "results";
    private static final String NAME_KEY = "name";
    private static final String SITE_KEY = "site";
    private static final String KEY_KEY = "key";
    private static final String SIZE_KEY = "size";
    private static final String TYPE_KEY = "type";
    private static final String ID_KEY = "id";

    public static String getNameByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(NAME_KEY);
        } else {
            return null;
        }
    }

    public static String getTrailerIDByIndex (String moviesJsonString, int index) throws JSONException, NullJSONStringException {
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

    public static String getSiteByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(SITE_KEY);
        } else {
            return null;
        }
    }

    public static String getKeyByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(KEY_KEY);
        } else {
            return null;
        }
    }

    public static String getSizeByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(SIZE_KEY);
        } else {
            return null;
        }
    }

    public static String getTypeByIndex(String moviesJsonString, int index) throws JSONException, NullJSONStringException {
        if(moviesJsonString == null || moviesJsonString.equals("")){
            throw new NullJSONStringException("Empty or null JSON String");
        }

        JSONObject jsonObject = new JSONObject(moviesJsonString);
        JSONArray movies = jsonObject.getJSONArray(RESULT_KEY);

        if (index < movies.length()) {
            JSONObject desiredMovie = movies.getJSONObject(index);
            return desiredMovie.getString(TYPE_KEY);
        } else {
            return null;
        }
    }

}
