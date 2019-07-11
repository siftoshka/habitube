package az.amorphist.poster.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;

import az.amorphist.poster.R;
import az.amorphist.poster.di.modules.MovieModule;
import az.amorphist.poster.presentation.post.PostPresenter;
import az.amorphist.poster.presentation.post.PostView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

public class PostFragment extends MvpAppCompatFragment implements PostView {

    private Toolbar toolbar;
    private ImageView posterBackground, posterMain;
    private TextView posterTitle, posterDate, posterRate, posterViews, posterDesc;


    @InjectPresenter PostPresenter postPresenter;

    @ProvidePresenter
    PostPresenter postPresenter() {
        final Bundle movieBundle = getArguments();
        final Integer postId = movieBundle.getInt("postPosition");
        final Integer showId = movieBundle.getInt("showPosition");
        final Integer upcomingId = movieBundle.getInt("upcomingPosition");

        final Scope temporaryPostScope = Toothpick.openScopes( "APP_SCOPE", "POST_SCOPE");
        temporaryPostScope.installModules(new MovieModule(postId, showId, upcomingId));
        final PostPresenter postPresenter = temporaryPostScope.getInstance(PostPresenter.class);
        Toothpick.closeScope("POST_SCOPE");
        return postPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        toolbar = view.findViewById(R.id.post_toolbar);
        posterBackground = view.findViewById(R.id.poster_background);
        posterMain = view.findViewById(R.id.poster_main_post);
        posterTitle = view.findViewById(R.id.poster_title);
        posterDate = view.findViewById(R.id.poster_date);
        posterRate = view.findViewById(R.id.poster_rate);
        posterViews = view.findViewById(R.id.poster_views);
        posterDesc = view.findViewById(R.id.poster_desc);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPresenter.goBack();
            }
        });
    }

    @Override
    public void getMovie(String image, String background, String title, String date, float rate, float views, String description) {
        Picasso.get().load("https://image.tmdb.org/t/p/original" + image)
                .placeholder(R.drawable.progress_animation)
                .into(posterMain);
        Picasso.get().load("https://image.tmdb.org/t/p/original" + background)
                .placeholder(R.drawable.progress_animation)
                .into(posterBackground);
        posterTitle.setText(title);
        posterDate.setText(date);
        posterRate.setText(String.valueOf(rate));
        posterViews.setText(String.valueOf(views));
        posterDesc.setText(description);
    }
}
