package lucagrazioli.popularmovies;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import lucagrazioli.popularmovies.data.MovieContract;
import lucagrazioli.popularmovies.test.RetrieveTrailersTask;

public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private PostersAdapter mPosterAdapter;
    private GridView postersGrid;
    private static final int landscape_columns = 3;
    private static final int portrait_columns = 2;
    private String last_sorting_pref;

    static final String POSITION_TAG = "position";
    private int saved_position;

    private static final int POSTER_LOADER = 0;
    private static final String LOG_TAG = "Provider";

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri movieUri);
    }

    public static final String [] POSTER_COLUMNS = {
            MovieContract.PosterEntry.TABLE_NAME+"."+ MovieContract.PosterEntry._ID,
            MovieContract.PosterEntry.COL_TITLE,
            MovieContract.PosterEntry.COL_RELEASE_DATE,
            MovieContract.PosterEntry.COL_DURATION,
            MovieContract.PosterEntry.COL_DESCRIPTION,
            MovieContract.PosterEntry.COL_IMAGE_URL,
            MovieContract.PosterEntry.COL_VOTE_AVERAGE,
            MovieContract.PosterEntry.COL_POPULARITY,
            MovieContract.PosterEntry.COL_VOTE_COUNT,
            MovieContract.PosterEntry.COL_MOVIE_ID
    };

    static final int POSTER_ID_COL = 0;
    static final int POSTER_TITLE_COL = 1;
    static final int POSTER_RELESE_DATE_COL = 2;
    static final int POSTER_DURATION_COL = 3;
    static final int POSTER_DESCRIPTION_COL = 4;
    static final int POSTER_IMAGE_URL_COL = 5;
    static final int POSTER_VOTE_AVERAGE_COL = 6;
    static final int POSTER_POPULARITY_COL = 7;
    static final int POSTER_VOTE_COUNT_COL = 8;
    static final int POSTER_MOVIE_ID_COL = 9;

    public MainActivityFragment() {
    }

    // since we read the location when we create the loader, all we need to do is restart things
    void onPreferenceChanged( ) {
        updateMovies();
        getLoaderManager().restartLoader(POSTER_LOADER, null, this);
    }

    private String normalizeSortingOrder(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_pop_key));

        String sortOrder = "";
        if(sorting.equals(getString(R.string.pref_sorting_pop_key))){
            sortOrder = MovieContract.PosterEntry.COL_POPULARITY+" DESC";
        }else{
            sortOrder = MovieContract.PosterEntry.COL_VOTE_COUNT+" DESC";
        }

        return sortOrder;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_TAG, postersGrid.getSelectedItemPosition());

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null)
            saved_position = savedInstanceState.getInt(POSITION_TAG, -1);
        else
            saved_position = -1;

        Uri posterUri = MovieContract.PosterEntry.buildPosterWithSorting("");
        Cursor cursor = getActivity().getContentResolver().query(
                posterUri, null, null, null, normalizeSortingOrder()
        );

        //Log.d(LOG_TAG,"Number of elements retrieved: "+cursor.getCount());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mPosterAdapter = new PostersAdapter(getActivity(), cursor, 0);

        postersGrid = (GridView) rootView.findViewById(R.id.posters_grid);
        postersGrid.setAdapter(mPosterAdapter);


        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            postersGrid.setNumColumns(landscape_columns);
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            postersGrid.setNumColumns(portrait_columns);
        }

        postersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                if (cursor != null) {
                    ((Callback) getActivity()).onItemSelected(MovieContract.PosterEntry.buildPosterUri(cursor.getInt(POSTER_MOVIE_ID_COL)));
                    /*Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                            .setData(MovieContract.PosterEntry.buildPosterUri(cursor.getInt(POSTER_MOVIE_ID_COL)));

                    Log.d("Detail_log", "URI: " + MovieContract.PosterEntry.buildPosterUri(cursor.getInt(POSTER_MOVIE_ID_COL)));

                    startActivity(intent);*/

                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(POSTER_LOADER, null, this);
        updateMovies();
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();

        /*
        onStart method check whenever the sorting preference is changed, only when this condition
        it's verified, the method begins the update procedure. This check is useful to avoid useless
        update (example rotated screen).
         */

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString(getString(R.string.pref_sorting_key),getString(R.string.pref_sorting_pop_key));


        if(!sorting.equals(last_sorting_pref)) {
            this.last_sorting_pref = sorting;
            updateMovies();
        }
    }

    private void updateMovies(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString(getString(R.string.pref_sorting_key),getString(R.string.pref_sorting_pop_key));

        //new RetrieveMoviesTask().execute(sorting);
        new RetrieveMoviesTask(mPosterAdapter, getActivity(), postersGrid, landscape_columns, portrait_columns).execute(sorting);

        //Need to launch retrieve trailer task
        //Need to do a query first, in order to obtain the list of movies ids
        String [] columns = {MovieContract.PosterEntry.COL_MOVIE_ID};
        Cursor moviesIds = getActivity().getContentResolver()
                .query(MovieContract.PosterEntry.CONTENT_URI,
                        columns,
                        null,
                        null,
                        null);
        String movieIdsString [] = getIdsStringArray(moviesIds);

        new RetrieveTrailersTask(getActivity()).execute(movieIdsString);

    }

    private String[] getIdsStringArray(Cursor c){
        List<String> idsList = new ArrayList<String>();
        String movieIdsString [] = new String [c.getCount()];
        while(c.moveToNext()){
            idsList.add(c.getString(0));
        }
        for(int i=0;i<idsList.size();i++){
            movieIdsString[i] = idsList.remove(i);
        }

        return movieIdsString;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sorting = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_pop_key));

        String sortOrder = "";
        if(sorting.equals(getString(R.string.pref_sorting_pop_key))){
            sortOrder = MovieContract.PosterEntry.COL_POPULARITY+" DESC";
        }else{
            sortOrder = MovieContract.PosterEntry.COL_VOTE_COUNT+" DESC";
        }

        Uri posterWithSortingUri = MovieContract.PosterEntry.buildPosterWithSorting(sortOrder);

        return new CursorLoader(getActivity(),
                    posterWithSortingUri,
                    POSTER_COLUMNS,
                    null,
                    null,
                sortOrder);
        }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mPosterAdapter.swapCursor(cursor);

        if(saved_position != -1){
            postersGrid.smoothScrollToPosition(saved_position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mPosterAdapter.swapCursor(null);
    }
}
