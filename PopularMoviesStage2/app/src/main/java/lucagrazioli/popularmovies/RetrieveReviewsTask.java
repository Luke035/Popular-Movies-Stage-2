package lucagrazioli.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucagrazioli on 12/12/15.
 */
public class RetrieveReviewsTask extends AsyncTask <Void, Void, List<Review> > {

    private long movieId;
    private View child;
    private final Context mContext;

    private static final String PARAM_API_KEY = "api_key";
    private static final String BASE_URL = "http://api.themoviedb.org/3/movie";
    private static final String REVIEWS_PARAM = "reviews";

    public RetrieveReviewsTask(Context mContext, View child, long movieId) {
        this.mContext = mContext;
        this.child = child;
        this.movieId = movieId;
    }

    private List<Review> extractReviews(String jsonString){
        List<Review> reviews = new ArrayList<Review>();

        try {
            int i = 0;
            String id = ReviewDataParser.getIDByIndex(jsonString, i);

            while(id != null){
                String author = ReviewDataParser.getAuthorByIndex(jsonString, i);
                String content = ReviewDataParser.getContentByIndex(jsonString, i);

                reviews.add(new Review(author, content));

                i++;

                id = ReviewDataParser.getIDByIndex(jsonString, i);
            }

        }catch (NullJSONStringException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    protected List<Review> doInBackground(Void... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewsJsonStr = null;

        try {

            Uri buildtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendPath("" + movieId)
                    .appendPath(REVIEWS_PARAM)
                    .appendQueryParameter(PARAM_API_KEY, mContext.getString(R.string.api_key))
                    .build();

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
            reviewsJsonStr = buffer.toString();

        }catch (IOException e){
            e.printStackTrace();
        }finally {
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

        List<Review> reviews = extractReviews(reviewsJsonStr);

        return reviews;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        super.onPostExecute(reviews);

        if(reviews == null || reviews.size()==0){
            TextView textView = new TextView(mContext);
            textView.setText(mContext.getString(R.string.no_reviews));
            textView.setPadding(5,5,5,5);
            ((LinearLayout) child).addView(textView);
        }

        for(Review r : reviews) {

            LinearLayout singleReviewLayout = (LinearLayout)
                    ((Activity) mContext).getLayoutInflater().inflate(R.layout.single_review_layout, null);

            ((TextView)singleReviewLayout.findViewById(R.id.review_author)).setText(r.author);
            ((TextView)singleReviewLayout.findViewById(R.id.review_content)).setText(r.review_content);

            ((LinearLayout)child).addView(singleReviewLayout);
        }


    }
}
