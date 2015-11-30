package lucagrazioli.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private String mSortingPreference;
    private boolean mTwoPane;

    private static final String MAINFRAGMENT_TAG = "DFTAG";
    private static final String DETAILFRAGMENT_TAG = "DTTAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mSortingPreference = prefs.getString(getString(R.string.pref_sorting_key),getString(R.string.pref_sorting_pop_key));

        setContentView(R.layout.activity_main);

        if(findViewById(R.id.poster_detail_conntainer)!=null){
            mTwoPane = true;

            /*if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.poster_detail_conntainer, new MovieDetailActivityFragment())
                        .commit();
            }*/
        }else{
            mTwoPane = false;
        }

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment(),MAINFRAGMENT_TAG)
                    .commit();
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortingOrder_pref = prefs.getString(getString(R.string.pref_sorting_key), getString(R.string.pref_sorting_pop_key));
        // update the location in our second pane using the fragment manager
        if (sortingOrder_pref != null && !sortingOrder_pref.equals(mSortingPreference)) {
            MainActivityFragment activityFragment = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.main_poster_fragment);
            if ( null != activityFragment ) {
                activityFragment.onPreferenceChanged();
            }

            MovieDetailActivityFragment movieDetailActivityFragment = (MovieDetailActivityFragment)
                    getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);

            if(movieDetailActivityFragment != null){
                movieDetailActivityFragment.onPreferenceChanged();
            }
        }

        mSortingPreference = sortingOrder_pref;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri) {
        if(mTwoPane){

            Bundle args = new Bundle();
            args.putParcelable(MovieDetailActivityFragment.DETAIL_URI, contentUri);

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.poster_detail_conntainer, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setData(contentUri);

            startActivity(intent);
        }
    }
}
