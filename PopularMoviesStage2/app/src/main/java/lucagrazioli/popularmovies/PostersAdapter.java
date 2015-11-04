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
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.
        ImageView imageView = (ImageView) view.findViewById(R.id.poster_image_view);

        //int image_col_index = cursor.getColumnIndex(MovieContract.PosterEntry.COL_IMAGE_URL);
        String image_url = cursor.getString(MainActivityFragment.POSTER_IMAGE_URL_COL);

        Picasso.with(context)
                .load(BASE_IMAGE_URL+image_url)
                .into(imageView);

        Log.d(LOG_TAG, "Called bind view with "+image_url+" image url");
    }

    /*
    @Override
    public int getCount() {
        return posterList.size();
    }

    @Override
    public Object getItem(int position) {
        return posterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.poster_layout,null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.poster_image_view);

        Poster poster = (Poster) getItem(position);

        Picasso.with(mContext)
                .load(poster.getImage_url())
                .into(imageView);

        return convertView;
    }

    public void clear() {
        this.posterList.clear();
        notifyDataSetChanged();
    }

    public void add(Poster p) {
        Log.v("Adapter","Added poster");
        this.posterList.add(p);

    }*/
}
