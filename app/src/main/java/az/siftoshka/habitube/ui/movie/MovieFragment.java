package az.siftoshka.habitube.ui.movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.CastAdapter;
import az.siftoshka.habitube.adapters.CrewAdapter;
import az.siftoshka.habitube.adapters.MovieAdapter;
import az.siftoshka.habitube.adapters.VideoAdapter;
import az.siftoshka.habitube.di.modules.MovieModule;
import az.siftoshka.habitube.di.modules.SearchModule;
import az.siftoshka.habitube.entities.credits.Cast;
import az.siftoshka.habitube.entities.credits.Crew;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.entities.video.Video;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.movie.MoviePresenter;
import az.siftoshka.habitube.presentation.movie.MovieView;
import az.siftoshka.habitube.ui.credits.CastBottomDialog;
import az.siftoshka.habitube.ui.credits.CrewBottomDialog;
import az.siftoshka.habitube.utils.CurrencyFormatter;
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
import static az.siftoshka.habitube.Constants.SYSTEM.YOUTUBE_URL;

public class MovieFragment extends MvpAppCompatFragment implements MovieView {

    @InjectPresenter MoviePresenter moviePresenter;

    @BindView(R.id.movie_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_similar_movies) RecyclerView recyclerViewSimilarMovies;
    @BindView(R.id.recycler_view_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_view_crew) RecyclerView recyclerViewCrew;
    @BindView(R.id.recycler_view_cast) RecyclerView recyclerViewCast;
    @BindView(R.id.main_screen) RelativeLayout mainScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.imdb_button) LinearLayout imdbButton;
    @BindView(R.id.watched_button) LinearLayout watchedButton;
    @BindView(R.id.watched_image) ImageView watchedImage;
    @BindView(R.id.watched_button_alt) LinearLayout watchedButtonAlt;
    @BindView(R.id.planning_button) LinearLayout planningButton;
    @BindView(R.id.planned_image) ImageView plannedImage;
    @BindView(R.id.planning_button_alt) LinearLayout planningButtonAlt;
    @BindView(R.id.poster_background) ImageView posterBackground;
    @BindView(R.id.poster_movie_post) ImageView posterMain;
    @BindView(R.id.poster_title) TextView posterTitle;
    @BindView(R.id.poster_date) TextView posterDate;
    @BindView(R.id.poster_rate) TextView posterRate;
    @BindView(R.id.poster_views) TextView posterViews;
    @BindView(R.id.poster_duration) TextView posterDuration;
    @BindView(R.id.poster_budget) TextView posterBudget;
    @BindView(R.id.poster_revenue) TextView posterRevenue;
    @BindView(R.id.poster_desc) TextView posterDesc;
    @BindView(R.id.movie_genres) ChipGroup movieGenresChip;
    @BindView(R.id.similar_movies_card_layout) LinearLayout similarMoviesCard;
    @BindView(R.id.videos_movies_card_layout) LinearLayout videosCard;
    @BindView(R.id.desc_movie_card_layout) LinearLayout descMovieCard;
    @BindView(R.id.tab_info) MaterialButton tabInfo;
    @BindView(R.id.tab_credits) MaterialButton tabCredits;
    @BindView(R.id.tab_similar) MaterialButton tabSimilar;
    @BindView(R.id.cast_button) MaterialButton castButton;
    @BindView(R.id.crew_button) MaterialButton crewButton;
    @BindView(R.id.tab_info_layout) LinearLayout tabInfoCard;
    @BindView(R.id.tab_credits_layout) LinearLayout tabCreditsCard;
    @BindView(R.id.cast_text) TextView castText;
    @BindView(R.id.crew_text) TextView crewText;
    @BindView(R.id.refresh) ImageView refreshButton;


    private MovieAdapter similarMoviesAdapter;
    private VideoAdapter videoAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private MessageListener messageListener;
    private DateChanger dateChanger = new DateChanger();

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(Constants.DI.APP_SCOPE));
        similarMoviesAdapter = new MovieAdapter(postId -> moviePresenter.goToDetailedMovieScreen(postId), postName -> messageListener.showText(postName));
        videoAdapter = new VideoAdapter(this::showVideo);
        castAdapter = new CastAdapter(id -> moviePresenter.goToDetailedPersonScreen(id));
        crewAdapter = new CrewAdapter(id -> moviePresenter.goToDetailedPersonScreen(id));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movie, container, false);
        unbinder = ButterKnife.bind(this, view);
        posterTitle.setSelected(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> moviePresenter.goBack());
        initialTab();
        initTabs();
        watchedButton.setVisibility(View.VISIBLE);
        watchedButton.setEnabled(false);
        watchedButtonAlt.setVisibility(View.GONE);
        planningButton.setVisibility(View.VISIBLE);
        planningButton.setEnabled(false);
        planningButtonAlt.setVisibility(View.GONE);

        LinearLayoutManager layoutManagerSimilarMovies = new GridLayoutManager(getContext(), 3);
        recyclerViewSimilarMovies.setLayoutManager(layoutManagerSimilarMovies);
        recyclerViewSimilarMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarMovies.setHasFixedSize(true);
        recyclerViewSimilarMovies.setAdapter(similarMoviesAdapter);
        LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(layoutManagerVideos);
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setHasFixedSize(true);
        recyclerViewVideos.setAdapter(videoAdapter);
        LinearLayoutManager layoutManagerCasts = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCasts);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setHasFixedSize(true);
        recyclerViewCast.setAdapter(castAdapter);
        LinearLayoutManager layoutManagerCrews = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCrew.setLayoutManager(layoutManagerCrews);
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setHasFixedSize(true);
        recyclerViewCrew.setAdapter(crewAdapter);
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
                        watchedImage.setImageResource(R.drawable.ic_favorite);
                        plannedImage.setImageResource(R.drawable.ic_watch);
                        watchedButton.setEnabled(true);
                        planningButton.setEnabled(true);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        showProgress(false);
                        watchedImage.setImageResource(R.drawable.ic_favorite);
                        plannedImage.setImageResource(R.drawable.ic_watch);
                        watchedButton.setEnabled(true);
                        planningButton.setEnabled(true);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_missing)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(posterMain);
        ImageLoader.loadBackground(getContext(), movie.getBackdropPath(), posterBackground);
        posterTitle.setText(movie.getTitle());
        posterDate.setText(dateChanger.changeDate(movie.getReleaseDate()));
        posterRate.setText(String.valueOf(movie.getVoteAverage()));
        posterViews.setText("(" + movie.getVoteCount() + ")");
        posterDuration.setText(movie.getRuntime() + " " + getResources().getString(R.string.minutes));
        posterBudget.setText("$" + CurrencyFormatter.format(movie.getBudget()));
        posterRevenue.setText("$" + CurrencyFormatter.format(movie.getRevenue()));
        addGenres(movie);
        checkDescription(movie);
        posterDesc.setText(movie.getOverview());
        showImdbWeb(movie.getImdbId());
        addMovieToWatched(movie);
        addMovieToPlanned(movie);
        deleteMovieFromWatched(movie);
        deleteMovieFromPlanned(movie);
        moviePresenter.isPlannedMovieChanged(movie.getId(), movie);
        moviePresenter.isWatchedMovieChanged(movie.getId(), movie);
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

    private void addMovieToWatched(Movie movie) {
        watchedButton.setOnClickListener(v -> {
            movie.setAddedDate(new Date());
            ImageLoader.saveToInternalStorage(movie.getPosterPath(), requireContext(), posterMain);
            moviePresenter.addMovieAsWatched(movie);
        });
    }

    private void addMovieToPlanned(Movie movie) {
        planningButton.setOnClickListener(v -> {
            movie.setAddedDate(new Date());
            ImageLoader.saveToInternalStorage(movie.getPosterPath(), requireContext(), posterMain);
            moviePresenter.addMovieAsPlanned(movie);
        });
    }

    private void checkDescription(Movie movie) {
        if (movie.getOverview().equals("")) descMovieCard.setVisibility(View.GONE);
    }

    private void deleteMovieFromWatched(Movie movie) {
        watchedButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie))
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), (arg0, arg1) -> moviePresenter.deleteMovieFromWatched(movie))
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void deleteMovieFromPlanned(Movie movie) {
        planningButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie))
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), (arg0, arg1) -> moviePresenter.deleteMovieFromPlanned(movie))
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void showVideo(String videoKey) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(YOUTUBE_URL + videoKey));
        startActivity(intent);
    }

    private void initialTab() {
        tabInfo.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabCredits.setTextColor(getResources().getColor(R.color.dark_800));
        tabSimilar.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.VISIBLE);
        tabCreditsCard.setVisibility(View.GONE);
        similarMoviesCard.setVisibility(View.GONE);
    }

    private void initTabs() {
        tabInfo.setOnClickListener(view -> initialTab());
        tabCredits.setOnClickListener(view -> {
            tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
            tabCredits.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabSimilar.setTextColor(getResources().getColor(R.color.dark_800));
            tabInfoCard.setVisibility(View.GONE);
            tabCreditsCard.setVisibility(View.VISIBLE);
            similarMoviesCard.setVisibility(View.GONE);
        });
        tabSimilar.setOnClickListener(view -> {
            tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
            tabCredits.setTextColor(getResources().getColor(R.color.dark_800));
            tabSimilar.setTextColor(getResources().getColor(R.color.colorPrimary));
            tabInfoCard.setVisibility(View.GONE);
            tabCreditsCard.setVisibility(View.GONE);
            similarMoviesCard.setVisibility(View.VISIBLE);
        });
    }

    private void showImdbWeb(String imdbId) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Constants.SYSTEM.IMDB_WEBSITE + imdbId));
        imdbButton.setOnClickListener(v -> startActivity(intent));
    }

    @Override
    public void showSimilarMovieList(List<MovieLite> similarMovies) {
        if (similarMovies.isEmpty()) similarMoviesCard.setVisibility(View.GONE);
        similarMoviesAdapter.addAllMovies(similarMovies);
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (videos.isEmpty()) videosCard.setVisibility(View.GONE);
        videoAdapter.addAllVideos(videos);
    }

    @Override
    public void showCast(List<Cast> casts) {
        if (casts == null || casts.size() == 0) castText.setVisibility(View.GONE);
        castAdapter.addAllPersons(casts);
    }

    @Override
    public void showCastExpandButton(List<Cast> casts) {
        castButton.setVisibility(View.VISIBLE);
        castButton.setOnClickListener(view -> {
            CastBottomDialog castBottomDialog = new CastBottomDialog(moviePresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("CAST", (ArrayList<? extends Parcelable>) casts);
            castBottomDialog.setArguments(bundle);
            castBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showCrew(List<Crew> crews) {
        if (crews == null || crews.size() == 0) crewText.setVisibility(View.GONE);
        crewAdapter.addAllPersons(crews);
    }

    @Override
    public void showCrewExpandButton(List<Crew> crews) {
        crewButton.setVisibility(View.VISIBLE);
        crewButton.setOnClickListener(view -> {
            CrewBottomDialog crewBottomDialog = new CrewBottomDialog(moviePresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("CREW", (ArrayList<? extends Parcelable>) crews);
            crewBottomDialog.setArguments(bundle);
            crewBottomDialog.show(getChildFragmentManager(), null);
        });
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
    public void setPlanButtonEnabled(boolean enabled) {
        if (enabled) {
            planningButton.setVisibility(View.GONE);
            planningButtonAlt.setVisibility(View.VISIBLE);
        } else {
            planningButton.setVisibility(View.VISIBLE);
            planningButtonAlt.setVisibility(View.GONE);
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
        refreshButton.setOnClickListener(view -> moviePresenter.onFirstViewAttach());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
