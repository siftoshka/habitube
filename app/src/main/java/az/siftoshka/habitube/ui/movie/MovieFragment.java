package az.siftoshka.habitube.ui.movie;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.Date;
import java.util.List;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.MovieAdapter;
import az.siftoshka.habitube.di.modules.MovieModule;
import az.siftoshka.habitube.di.modules.SearchModule;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.presentation.movie.MoviePresenter;
import az.siftoshka.habitube.presentation.movie.MovieView;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;

public class MovieFragment extends MvpAppCompatFragment implements MovieView {

    @InjectPresenter MoviePresenter moviePresenter;

    @BindView(R.id.movie_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_similar_movies) RecyclerView recyclerViewSimilarMovies;
    @BindView(R.id.main_screen) RelativeLayout mainScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.imdb_button) LinearLayout imdbButton;
    @BindView(R.id.watched_button) LinearLayout watchedButton;
    @BindView(R.id.watched_image) ImageView watchedImage;
    @BindView(R.id.watched_button_alt) LinearLayout watchedButtonAlt;
    @BindView(R.id.planning_button) LinearLayout planningButton;
    @BindView(R.id.poster_background) ImageView posterBackground;
    @BindView(R.id.poster_movie_post) ImageView posterMain;
    @BindView(R.id.poster_title) TextView posterTitle;
    @BindView(R.id.poster_date) TextView posterDate;
    @BindView(R.id.poster_rate) TextView posterRate;
    @BindView(R.id.poster_views) TextView posterViews;
    @BindView(R.id.poster_duration) TextView posterDuration;
    @BindView(R.id.poster_desc) TextView posterDesc;
    @BindView(R.id.movie_genres) ChipGroup movieGenresChip;
    @BindView(R.id.similar_movies_card_layout) LinearLayout similarMoviesCard;
    @BindView(R.id.desc_movie_card_layout) LinearLayout descMovieCard;

    private MovieAdapter similarMoviesAdapter;
    private DateChanger dateChanger = new DateChanger();
    private boolean isPosterReady = false, isBackgroundReady = false;


    private Unbinder unbinder;

    @ProvidePresenter
    MoviePresenter postPresenter() {
        final Bundle movieBundle = getArguments();
        final Integer postPosition = movieBundle.getInt("postPosition");
        final Integer upcomingPosition = movieBundle.getInt("upcomingPosition");

        final Bundle searchBundle = getArguments();
        final Integer postId = searchBundle.getInt("postId");
        final Integer mediaType = searchBundle.getInt("mediaType");
        final Scope temporaryPostScope = Toothpick.openScopes(Constants.DI.APP_SCOPE, Constants.DI.POST_SCOPE);
        temporaryPostScope.installModules(new MovieModule(postPosition, 0, upcomingPosition));
        temporaryPostScope.installModules(new SearchModule(postId, mediaType));
        final MoviePresenter moviePresenter = temporaryPostScope.getInstance(MoviePresenter.class);
        Toothpick.closeScope(Constants.DI.POST_SCOPE);
        return moviePresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toothpick.inject(this, Toothpick.openScope(Constants.DI.APP_SCOPE));
        similarMoviesAdapter = new MovieAdapter(postId -> moviePresenter.goToDetailedMovieScreen(postId));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> moviePresenter.goBack());
        watchedButton.setVisibility(View.VISIBLE);
        watchedButton.setEnabled(false);
        watchedButtonAlt.setVisibility(View.GONE);
        LinearLayoutManager layoutManagerSimilarMovies = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSimilarMovies.setLayoutManager(layoutManagerSimilarMovies);
        recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarMovies.setHasFixedSize(true);
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showMovie(Movie movie) {
        toolbar.setTitle(movie.getTitle());

        Glide.with(requireContext())
                .load(IMAGE_URL + movie.getPosterPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        isPosterReady = true;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        isPosterReady = true;
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.notfound)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(posterMain);

        Glide.with(requireContext())
                .load(IMAGE_URL + movie.getBackdropPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        try {
                            watchedImage.setImageResource(R.drawable.ic_favorite);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        watchedButton.setEnabled(true);
                        isBackgroundReady = true;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        watchedImage.setImageResource(R.drawable.ic_favorite);
                        watchedButton.setEnabled(true);
                        isBackgroundReady = true;
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.image_not_found)
                .into(posterBackground);

        posterTitle.setText(movie.getTitle());
        posterDate.setText(dateChanger.changeDate(movie.getReleaseDate()));
        posterRate.setText(String.valueOf(movie.getVoteAverage()));
        posterViews.setText("(" + movie.getVoteCount() + ")");
        posterDuration.setText(movie.getRuntime() + " " + getResources().getString(R.string.minutes));
        addGenres(movie);
        checkDescription(movie);
        posterDesc.setText(movie.getOverview());
        showImdbWeb(movie.getImdbId());
        addMovieToWatched(movie, movie.getMovieGenres());
        deleteMovieFromWatched(movie);
    }

    private void addGenres(Movie movie) {
        for (MovieGenre mGenres : movie.getMovieGenres()) {
            Chip chip = new Chip(requireContext());
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(requireContext(),
                    null, 0, R.style.Widget_MaterialComponents_Chip_Action);
            chip.setChipDrawable(chipDrawable);
            chip.setText(mGenres.getName());
            movieGenresChip.addView(chip);
        }
    }

    private void addMovieToWatched(Movie movie, List<MovieGenre> movieGenres) {
        watchedButton.setOnClickListener(v -> {
            if (isPosterReady & isBackgroundReady) {
                movie.setAddedDate(new Date());
                movie.setPosterImage(ImageLoader.imageView2Bitmap(posterMain));
                movie.setPosterBackground(ImageLoader.imageView2Bitmap(posterBackground));
                moviePresenter.addMovieAsWatched(movie, movieGenres);
            }
        });
    }

    private void checkDescription(Movie movie) {
        if (movie.getOverview().equals("")) {
            descMovieCard.setVisibility(View.GONE);
        }
    }

    private void deleteMovieFromWatched(Movie movie) {
        watchedButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie));
            alertDialogBuilder.setMessage(getResources().getString(R.string.are_you_sure));
            alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                    (arg0, arg1) -> moviePresenter.deleteMovieFromWatched(movie));

            alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss());

            alertDialogBuilder.show();
        });
    }

    @Override
    public void showSimilarMovieList(List<MovieLite> similarMovies) {
        if (similarMovies.isEmpty()) {
            similarMoviesCard.setVisibility(View.GONE);
        }
        similarMoviesAdapter.addAllMovies(similarMovies);
    }

    private void showImdbWeb(String imdbId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.SYSTEM.IMDB_WEBSITE + imdbId));
        imdbButton.setOnClickListener(v -> startActivity(intent));
    }

    @Override
    public void showProgress(boolean loadingState) {
        if (loadingState) {
            loadingScreen.setVisibility(View.VISIBLE);
        } else {
            loadingScreen.setVisibility(View.GONE);
        }
    }

    @Override
    public void setSaveButtonEnabled(boolean enabled) {
        if (enabled) {
            watchedButton.setVisibility(View.GONE);
            watchedButtonAlt.setVisibility(View.VISIBLE);
        } else {
            watchedButton.setVisibility(View.VISIBLE);
            watchedButtonAlt.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovieScreen() {
        mainScreen.setVisibility(View.VISIBLE);
        errorScreen.setVisibility(View.GONE);
    }

    @Override
    public void showErrorScreen() {
        mainScreen.setVisibility(View.GONE);
        errorScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
