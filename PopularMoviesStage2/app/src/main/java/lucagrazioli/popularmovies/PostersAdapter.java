package lucagrazioli.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PostersAdapter extends CursorAdapter {

    //private Context mContext;
    //private List<Poster> posterList;
    private static final String LOG_TAG = "Provider";
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";
    public PostersAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        //this.posterList = posterList;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster_layout, parent, false);

        Log.d(LOG_TAG, "Called new view");

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.poster_image_view);

        //ImageButton imageView = (ImageButton) view.findViewById(R.id.poster_image_view);

        String image_url = cursor.getString(MainActivityFragment.POSTER_IMAGE_URL_COL);

        Picasso.with(context)
                .load(BASE_IMAGE_URL+image_url)
                .into(imageView);

        Log.d(LOG_TAG, "Called bind view with "+image_url+" image url");
    }

}
