package lucagrazioli.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class PostersAdapter extends BaseAdapter {

    private Context mContext;
    private List<Poster> posterList;

    public PostersAdapter(Context mContext, List<Poster> posterList) {
        this.mContext = mContext;
        this.posterList = posterList;
    }

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

    }
}
