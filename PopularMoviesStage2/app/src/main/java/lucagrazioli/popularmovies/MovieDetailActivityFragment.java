package lucagrazioli.popularmovies;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import lucagrazioli.popularmovies.data.MovieContract;

public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final int DETAIL_LOADER = 0;
    public static final int TRAILER_LOADER = 1;
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342";

    public static final String YOU_TUBE_BASE_URL = "https://www.youtube.com/watch";
    public static final String YOU_TUBE_KEY = "v";

    public static String DETAIL_URI = "URI";
    private Uri mUri;

    private LinearLayout trailerContainer;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, new LoaderTrailerCallbacks());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DETAIL_URI);
        }

        Log.d("Two pane mode", "onCreateView fragment");
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

       trailerContainer = (LinearLayout) rootView.findViewById(R.id.trailer_container);

        return rootView;
    }

    private String extractReleaseYear(String date){
        //To retrieve the release year, the original json string is splitted using the - character

        String [] splittedString = date.split("-");
        return splittedString[0];
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /*Intent intent = getActivity().getIntent();

        if(intent == null)
            return null;*/

        return new CursorLoader(
                getActivity(),
                mUri,
                //intent.getData(),
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

        Log.d("Deatil_log", "Title retrieved: " + data.getString(MainActivityFragment.POSTER_TITLE_COL));

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

        final Button markAsFavourite = (Button) getView().findViewById(R.id.mark_as_favourite_button);

        String isFavorite = data.getString(MainActivityFragment.POSTER_FAVOURITE_COL);
        if(isFavorite.equalsIgnoreCase("true")){
            markAsFavourite.setText(getString(R.string.is_favourite));
        }

        markAsFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c  = getActivity().getContentResolver().query(mUri,
                        null,
                        null,
                        null,
                        null);


                if(c.moveToNext()) {
                    ContentValues updatedValue = new ContentValues();
                    updatedValue.put(MovieContract.PosterEntry.COL_TITLE, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_TITLE)));
                    updatedValue.put(MovieContract.PosterEntry.COL_DESCRIPTION, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_DESCRIPTION)));
                    updatedValue.put(MovieContract.PosterEntry.COL_VOTE_COUNT, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_VOTE_COUNT)));
                    updatedValue.put(MovieContract.PosterEntry.COL_POPULARITY, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_POPULARITY)));
                    updatedValue.put(MovieContract.PosterEntry.COL_DURATION, 120);
                    updatedValue.put(MovieContract.PosterEntry.COL_IMAGE_URL, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_IMAGE_URL)));
                    updatedValue.put(MovieContract.PosterEntry.COL_RELEASE_DATE, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_RELEASE_DATE)));
                    updatedValue.put(MovieContract.PosterEntry.COL_VOTE_AVERAGE, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_VOTE_AVERAGE)));
                    updatedValue.put(MovieContract.PosterEntry.COL_MOVIE_ID, c.getString(c.getColumnIndex(MovieContract.PosterEntry.COL_MOVIE_ID)));
                    updatedValue.put(MovieContract.PosterEntry.COL_FAVOURITE, "TRUE");

                    /*int updatedRows = getActivity().getContentResolver().insert(
                            MovieContract.PosterEntry.CONTENT_URI,
                            updatedValue,
                            null,
                            null
                    );*/

                    Uri uri = getActivity().getContentResolver().insert(
                            MovieContract.PosterEntry.CONTENT_URI,
                            updatedValue
                    );

                    if(uri != null){
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.marked_as_favourite), Toast.LENGTH_SHORT);
                        toast.show();
                        markAsFavourite.setText(getString(R.string.is_favourite));
                    }
                }
            }
        });

        Log.d("Two pane mode", "Cursor load finished");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void onPreferenceChanged(){

    }

    public class LoaderTrailerCallbacks implements LoaderManager.LoaderCallbacks<Cursor>{
        public final String mTrailerUri = null;


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            //First need to construct trailer URI

            String movieID = ""+MovieContract.PosterEntry.getIdFromUri(mUri);
            String selection = MovieContract.TrailerEntry.COL_MOVIE_ID+" = ?";
            String [] selectionArgs = {movieID}; //Poster id

            return new CursorLoader(
                    getActivity(),
                    MovieContract.TrailerEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if(data.getCount() == 0){
                View noTrailerLayout = getActivity().getLayoutInflater().inflate(R.layout.no_trailers_layout, null);
                trailerContainer.addView(noTrailerLayout);
            }else {

                while (data.moveToNext()) {
                    View trailerLayout = getActivity().getLayoutInflater().inflate(R.layout.trailer_entry_layout, null);

                    String trailerName = data.getString(data.getColumnIndex(MovieContract.TrailerEntry.COL_NAME));
                    TextView trailerLabel = (TextView) trailerLayout.findViewById(R.id.trailer_label);
                    trailerLabel.setText(trailerName);

                    ImageView trailerIcon = (ImageView) trailerLayout.findViewById(R.id.trailer_icon);
                    final String trailerKey = data.getString(data.getColumnIndex(MovieContract.TrailerEntry.COL_TRAILER_KEY));
                    trailerIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri trailerURI = Uri.parse(YOU_TUBE_BASE_URL).buildUpon().appendQueryParameter(YOU_TUBE_KEY, trailerKey).build();

                            Intent intent = new Intent(Intent.ACTION_VIEW, trailerURI);
                            startActivity(intent);
                        }
                    });

                    trailerContainer.addView(trailerLayout);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
