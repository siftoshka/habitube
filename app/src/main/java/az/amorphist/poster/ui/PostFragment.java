package az.amorphist.poster.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.di.modules.MovieModule;
import az.amorphist.poster.presentation.post.PostPresenter;
import az.amorphist.poster.presentation.post.PostView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.poster.App.IMAGE_URL;
import static az.amorphist.poster.di.DI.APP_SCOPE;
import static az.amorphist.poster.di.DI.POST_SCOPE;

public class PostFragment extends MvpAppCompatFragment implements PostView {

    @Inject Context context;
    private Toolbar toolbar;
    private RelativeLayout mainScreen;
    private LinearLayout loadingScreen, errorScreen;
    private ImageView posterBackground, posterMain;
    private TextView posterTitle, posterDate, posterRate, posterViews, posterDesc;


    @InjectPresenter
    PostPresenter postPresenter;

    @ProvidePresenter
    PostPresenter postPresenter() {
        final Bundle movieBundle = getArguments();
        final Integer postId = movieBundle.getInt("postPosition");
        final Integer showId = movieBundle.getInt("showPosition");
        final Integer upcomingId = movieBundle.getInt("upcomingPosition");

        final Scope temporaryPostScope = Toothpick.openScopes(APP_SCOPE, POST_SCOPE);
        temporaryPostScope.installModules(new MovieModule(postId, showId, upcomingId));
        final PostPresenter postPresenter = temporaryPostScope.getInstance(PostPresenter.class);
        Toothpick.closeScope(POST_SCOPE);
        return postPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        loadingScreen = view.findViewById(R.id.loading_screen);
        mainScreen = view.findViewById(R.id.main_screen);
        errorScreen = view.findViewById(R.id.error_screen);
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
        Glide.with(context)
                .load(IMAGE_URL + image)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.progress_animation)
                .into(posterMain);
        Glide.with(context)
                .load(IMAGE_URL + background)
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.progress_animation)
                .into(posterBackground);
        posterTitle.setText(title);
        posterDate.setText(date);
        posterRate.setText(String.valueOf(rate));
        posterViews.setText(String.valueOf(views));
        posterDesc.setText(description);
    }

    @Override
    public void showProgress(boolean loadingState) {
        if(loadingState){
            loadingScreen.setVisibility(View.VISIBLE);
            mainScreen.setVisibility(View.GONE);
        } else {
            loadingScreen.setVisibility(View.GONE);
            mainScreen.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showErrorScreen() {
        loadingScreen.setVisibility(View.GONE);
        mainScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
    }
}
