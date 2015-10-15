package lucagrazioli.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
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
                .into(imageView);

        return rootView;
    }

    private String extractReleaseYear(String date){
        //To retrieve the release year, the original json string is splitted using the - character

        String [] splittedString = date.split("-");
        return splittedString[0];
    }
}
