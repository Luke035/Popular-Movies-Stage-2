package lucagrazioli.popularmovies;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int DETAIL_LOADER = 0;
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        /*Intent intent = getActivity().getIntent();
        Poster poster = (Poster) intent.getSerializableExtra(Intent.EXTRA_TEXT);

        TextView title_text_view = (TextView) rootView.findViewById(R.id.movie_title_text_view);
        title_text_view.setText(poster.getTitle());

        TextView release_date_text_view = (TextView) rootView.findViewById(R.id.release_date_text_view);
        release_date_text_view.setText(extractReleaseYear(poster.getReleaseDate()));

        TextView user_rating_text_view = (TextView) rootView.findViewById(R.id.user_rating_text_view);
        user_rating_text_view.setText(poster.getVoteAverage() + "/10");

        TextView overview_text_view = (TextView) rootView.findViewById(R.id.overview_text_view);
        overview_text_view.setText(poster.getOverview());


        ImageView imageView = (ImageView) rootView.findViewById(R.id.poster_image_view_detail);
        Picasso.with(getActivity())
                .load(poster.getImage_url())
                .into(imageView); */

        return rootView;
    }

    private String extractReleaseYear(String date){
        //To retrieve the release year, the original json string is splitted using the - character

        String [] splittedString = date.split("-");
        return splittedString[0];
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Intent intent = getActivity().getIntent();

        if(intent == null)
            return null;

        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MainActivityFragment.POSTER_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(!data.moveToFirst()){
            Log.d("Detail_log", "Empty cursor");
            return;
        }

        Log.d("Deatil_log", "Title retrieved: "+data.getString(MainActivityFragment.POSTER_TITLE_COL));

        TextView title_text_view = (TextView) getView().findViewById(R.id.movie_title_text_view);
        title_text_view.setText(data.getString(MainActivityFragment.POSTER_TITLE_COL));

        TextView release_date_text_view = (TextView) getView().findViewById(R.id.release_date_text_view);
        release_date_text_view.setText(extractReleaseYear(data.getString(MainActivityFragment.POSTER_RELESE_DATE_COL)));

        TextView user_rating_text_view = (TextView) getView().findViewById(R.id.user_rating_text_view);
        user_rating_text_view.setText(data.getDouble(MainActivityFragment.POSTER_VOTE_AVERAGE_COL) + "/10");

        TextView overview_text_view = (TextView) getView().findViewById(R.id.overview_text_view);
        overview_text_view.setText(data.getString(MainActivityFragment.POSTER_DESCRIPTION_COL));


        ImageView imageView = (ImageView) getView().findViewById(R.id.poster_image_view_detail);
        Picasso.with(getActivity())
                .load(BASE_IMAGE_URL+data.getString(MainActivityFragment.POSTER_IMAGE_URL_COL))
                .into(imageView);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
