package az.siftoshka.habitube.ui.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.CastAdapter;
import az.siftoshka.habitube.adapters.CrewAdapter;
import az.siftoshka.habitube.adapters.GenreShowAdapter;
import az.siftoshka.habitube.adapters.SeasonAdapter;
import az.siftoshka.habitube.adapters.ShowAdapter;
import az.siftoshka.habitube.adapters.SimilarShowAdapter;
import az.siftoshka.habitube.adapters.VideoAdapter;
import az.siftoshka.habitube.di.modules.MovieModule;
import az.siftoshka.habitube.di.modules.SearchModule;
import az.siftoshka.habitube.entities.credits.Cast;
import az.siftoshka.habitube.entities.credits.Crew;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.entities.show.Season;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.entities.video.Video;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.show.ShowPresenter;
import az.siftoshka.habitube.presentation.show.ShowView;
import az.siftoshka.habitube.ui.credits.CastBottomDialog;
import az.siftoshka.habitube.ui.credits.CrewBottomDialog;
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

import static android.content.Context.MODE_PRIVATE;
import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;
import static az.siftoshka.habitube.Constants.DI.POST_SCOPE;
import static az.siftoshka.habitube.Constants.SYSTEM.IMAGE_URL;
import static az.siftoshka.habitube.Constants.SYSTEM.YOUTUBE_URL;

public class ShowFragment extends MvpAppCompatFragment implements ShowView {

    @InjectPresenter ShowPresenter showPresenter;

    @BindView(R.id.show_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_similar_shows) RecyclerView recyclerViewSimilarShows;
    @BindView(R.id.recycler_view_seasons) RecyclerView recyclerViewSeasons;
    @BindView(R.id.recycler_view_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.recycler_view_crew) RecyclerView recyclerViewCrew;
    @BindView(R.id.recycler_view_cast) RecyclerView recyclerViewCast;
    @BindView(R.id.show_screen) RelativeLayout showScreen;
    @BindView(R.id.loading_screen) View loadingScreen;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.watched_button) LinearLayout watchedButton;
    @BindView(R.id.watched_image) ImageView watchedImage;
    @BindView(R.id.watched_button_alt) RelativeLayout watchedButtonAlt;
    @BindView(R.id.planning_button) LinearLayout planningButton;
    @BindView(R.id.planned_image) ImageView plannedImage;
    @BindView(R.id.planning_button_alt) LinearLayout planningButtonAlt;
    @BindView(R.id.poster_show_post) ImageView posterShow;
    @BindView(R.id.show_poster_background) ImageView posterShowBackground;
    @BindView(R.id.poster_show_title) TextView posterShowTitle;
    @BindView(R.id.poster_show_date) TextView posterShowDate;
    @BindView(R.id.poster_show_rate) TextView posterShowRate;
    @BindView(R.id.poster_show_views) TextView posterShowViews;
    @BindView(R.id.poster_show_duration) TextView posterShowDuration;
    @BindView(R.id.poster_show_desc) TextView posterShowDesc;
    @BindView(R.id.watched_rating) TextView ratingText;
    @BindView(R.id.rating) RatingBar posterRating;
    @BindView(R.id.rating_layout) LinearLayout ratingCard;
    @BindView(R.id.show_genres) RecyclerView recyclerViewGenres;
    @BindView(R.id.videos_shows_card_layout) LinearLayout videosCard;
    @BindView(R.id.seasons_card_layout) LinearLayout seasonsCard;
    @BindView(R.id.similar_shows_card_layout) LinearLayout similarShowsCard;
    @BindView(R.id.desc_show_card_layout) LinearLayout descShowCard;
    @BindView(R.id.tab_info) MaterialButton tabInfo;
    @BindView(R.id.tab_credits) MaterialButton tabCredits;
    @BindView(R.id.tab_seasons) MaterialButton tabSeasons;
    @BindView(R.id.tab_similar) MaterialButton tabSimilar;
    @BindView(R.id.cast_button) MaterialButton castButton;
    @BindView(R.id.crew_button) MaterialButton crewButton;
    @BindView(R.id.cast_text) TextView castText;
    @BindView(R.id.crew_text) TextView crewText;
    @BindView(R.id.tab_info_layout) LinearLayout tabInfoCard;
    @BindView(R.id.tab_credits_layout) LinearLayout tabCreditsCard;
    @BindView(R.id.refresh) ImageView refreshButton;
    @BindView(R.id.show_netflix) MaterialButton showNetflix;

    private SimilarShowAdapter similarShowsAdapter;
    private VideoAdapter videoAdapter;
    private SeasonAdapter seasonAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private GenreShowAdapter genreAdapter;
    private MessageListener messageListener;
    private DateChanger dateChanger = new DateChanger();
    private Unbinder unbinder;
    private int showID;

    @ProvidePresenter
    ShowPresenter showPresenter() {
        final Bundle showBundle = getArguments();
        final Integer showPosition = showBundle.getInt("showPosition");
        final Bundle searchBundle = getArguments();
        final Integer postId = searchBundle.getInt("postId");
        final Integer mediaType = searchBundle.getInt("mediaType");

        final Scope temporaryPostScope = Toothpick.openScopes(APP_SCOPE, POST_SCOPE);
        temporaryPostScope.installModules(new MovieModule(0, showPosition, 0));
        temporaryPostScope.installModules(new SearchModule(postId, mediaType));
        final ShowPresenter showPresenter = temporaryPostScope.getInstance(ShowPresenter.class);
        Toothpick.closeScope(POST_SCOPE);
        return showPresenter;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        similarShowsAdapter = new SimilarShowAdapter(showId -> showPresenter.goToDetailedShowScreen(showId), postName -> messageListener.showText(postName));
        videoAdapter = new VideoAdapter(this::showVideo);
        seasonAdapter = new SeasonAdapter(this::showBottomSeasonDialog);
        castAdapter = new CastAdapter(id -> showPresenter.goToDetailedPersonScreen(id));
        crewAdapter = new CrewAdapter(id -> showPresenter.goToDetailedPersonScreen(id));
        genreAdapter = new GenreShowAdapter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_show, container, false);
        unbinder = ButterKnife.bind(this, view);
        posterShowTitle.setSelected(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> showPresenter.goBack());
        checkTabs();
        initTabs();
        LinearLayoutManager layoutManagerGenres = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewGenres.setLayoutManager(layoutManagerGenres);
        recyclerViewGenres.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGenres.setHasFixedSize(true);
        recyclerViewGenres.setAdapter(genreAdapter);
        GridLayoutManager layoutManagerSimilarShows = new GridLayoutManager(getContext(), 3);
        recyclerViewSimilarShows.setLayoutManager(layoutManagerSimilarShows);
        recyclerViewSimilarShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSimilarShows.setHasFixedSize(true);
        recyclerViewSimilarShows.setAdapter(similarShowsAdapter);
        paginateSimilarShows();
        GridLayoutManager layoutManagerSeasons = new GridLayoutManager(getContext(), 3);
        recyclerViewSeasons.setLayoutManager(layoutManagerSeasons);
        recyclerViewSeasons.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSeasons.setHasFixedSize(true);
        recyclerViewSeasons.setAdapter(seasonAdapter);
        LinearLayoutManager layoutManagerVideos = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(layoutManagerVideos);
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setHasFixedSize(true);
        recyclerViewVideos.setAdapter(videoAdapter);
        LinearLayoutManager layoutManagerCasts = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerViewCast.setLayoutManager(layoutManagerCasts);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setHasFixedSize(true);
        recyclerViewCast.setAdapter(castAdapter);
        LinearLayoutManager layoutManagerCrews = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerViewCrew.setLayoutManager(layoutManagerCrews);
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setHasFixedSize(true);
        recyclerViewCrew.setAdapter(crewAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showTVShow(Show show) {
        showID = show.getId();
        Glide.with(requireContext())
                .load(IMAGE_URL + show.getPosterPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (watchedImage != null) {
                            watchedImage.setImageResource(R.drawable.ic_favorite);
                            plannedImage.setImageResource(R.drawable.ic_watch);
                            watchedButton.setEnabled(true);
                            planningButton.setEnabled(true);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        showProgress(false);
                        if (watchedImage != null) {
                            watchedImage.setImageResource(R.drawable.ic_favorite);
                            plannedImage.setImageResource(R.drawable.ic_watch);
                            watchedButton.setEnabled(true);
                            planningButton.setEnabled(true);
                        }
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(R.drawable.ic_missing)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(posterShow);
        ImageLoader.loadBackground(getContext(), show.getBackdropPath(), posterShowBackground);
        posterShowTitle.setText(show.getName());
        posterShowDate.setText(dateChanger.changeDate(show.getFirstAirDate()));
        posterShowRate.setText(String.valueOf(show.getVoteAverage()));
        posterShowViews.setText("(" + show.getVoteCount() + ")");
        episodeRuntime(show.getEpisodeRunTime().toString());
        checkDescription(show);
        posterShowDesc.setText(show.getOverview());
        genreAdapter.addAllGenres(show.getShowGenres());
        addMovieToPlanned(show);
        addMovieToWatched(show);
        deleteMovieFromPlanned(show);
        deleteMovieFromWatched(show);
        showPresenter.isPlannedShowChanged(show.getId(), show);
        showPresenter.isWatchedShowChanged(show.getId(), show);
        showPresenter.getSavedWShowId(show.getId());
        showNetflixPage(show.getHomepage());
    }

    private void addMovieToWatched(Show show) {
        watchedButton.setOnClickListener(v -> {
            show.setAddedDate(new Date());
            ImageLoader.saveToInternalStorage(show.getPosterPath(), requireContext(), posterShow);
            showPresenter.addShowAsWatched(show);
            ratingCard.setVisibility(View.VISIBLE);
            posterRating.setOnRatingBarChangeListener((ratingBar, v1, b) -> {
                ratingText.setText(String.valueOf((int) ratingBar.getRating()));
                showPresenter.updateRating(show, ratingBar.getRating());
            });
        });
    }

    private void addMovieToPlanned(Show show) {
        planningButton.setOnClickListener(v -> {
            show.setAddedDate(new Date());
            ImageLoader.saveToInternalStorage(show.getPosterPath(), requireContext(), posterShow);
            showPresenter.addShowAsPlanned(show);
        });
    }

    private void deleteMovieFromWatched(Show show) {
        watchedButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie))
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), (arg0, arg1) -> {showPresenter.deleteShowFromWatched(show);
                                                                ratingCard.setVisibility(View.GONE);})
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void deleteMovieFromPlanned(Show show) {
        planningButtonAlt.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle);
            alertDialogBuilder.setTitle(getResources().getString(R.string.delete_movie))
                    .setMessage(getResources().getString(R.string.are_you_sure))
                    .setPositiveButton(getResources().getString(R.string.yes), (arg0, arg1) -> showPresenter.deleteShowFromPlanned(show))
                    .setNegativeButton(getResources().getString(R.string.no), (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void showVideo(String videoKey) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(YOUTUBE_URL + videoKey));
        startActivity(intent);
    }

    private void episodeRuntime(String episodeRuntime) {
        String updatedText = episodeRuntime.substring(1, episodeRuntime.length() - 1);
        posterShowDuration.setText(String.format("%s %s", updatedText, getResources().getString(R.string.minutes)));
    }

    private void checkDescription(Show show) {
        if (show.getOverview().equals("")) descShowCard.setVisibility(View.GONE);
    }

    private void paginateSimilarShows() {
        recyclerViewSimilarShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerViewSimilarShows.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && page <= 3) {
                    showPresenter.getMoreSimilarShows(showID, page);
                    page++;
                }
            }
        });
    }

    private void showNetflixPage(String homepage) {
        if (homepage.contains("netflix")) {
            new Handler().postDelayed(() -> {
                showNetflix.setVisibility(View.VISIBLE);
                showNetflix.setAlpha(0.0f);
                showNetflix.animate().translationX(showNetflix.getHeight()).alpha(1.0f).setListener(null);
                showNetflix.setOnClickListener(view -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(homepage));
                    startActivity(intent);
                });
            }, 500);
        } else
            showNetflix.setVisibility(View.GONE);
    }

    private void initTabInfo() {
        tabInfo.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabCredits.setTextColor(getResources().getColor(R.color.dark_800));
        tabSeasons.setTextColor(getResources().getColor(R.color.dark_800));
        tabSimilar.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.VISIBLE);
        tabCreditsCard.setVisibility(View.GONE);
        seasonsCard.setVisibility(View.GONE);
        similarShowsCard.setVisibility(View.GONE);
    }

    private void initTabCredits() {
        tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
        tabCredits.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabSeasons.setTextColor(getResources().getColor(R.color.dark_800));
        tabSimilar.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.GONE);
        tabCreditsCard.setVisibility(View.VISIBLE);
        seasonsCard.setVisibility(View.GONE);
        similarShowsCard.setVisibility(View.GONE);
    }

    private void initTabSeasons() {
        tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
        tabCredits.setTextColor(getResources().getColor(R.color.dark_800));
        tabSeasons.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabSimilar.setTextColor(getResources().getColor(R.color.dark_800));
        tabInfoCard.setVisibility(View.GONE);
        tabCreditsCard.setVisibility(View.GONE);
        seasonsCard.setVisibility(View.VISIBLE);
        similarShowsCard.setVisibility(View.GONE);
    }

    private void initTabSimilar() {
        tabInfo.setTextColor(getResources().getColor(R.color.dark_800));
        tabCredits.setTextColor(getResources().getColor(R.color.dark_800));
        tabSeasons.setTextColor(getResources().getColor(R.color.dark_800));
        tabSimilar.setTextColor(getResources().getColor(R.color.colorPrimary));
        tabInfoCard.setVisibility(View.GONE);
        tabCreditsCard.setVisibility(View.GONE);
        seasonsCard.setVisibility(View.GONE);
        similarShowsCard.setVisibility(View.VISIBLE);
    }

        private void initTabs() {
        tabInfo.setOnClickListener(view -> {
            initTabInfo();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Show-Tab", MODE_PRIVATE).edit();
            editor.putInt("STab", 100).apply();
        });
        tabCredits.setOnClickListener(view -> {
            initTabCredits();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Show-Tab", MODE_PRIVATE).edit();
            editor.putInt("STab", 101).apply();
        });
        tabSeasons.setOnClickListener(view -> initTabSeasons());
        tabSimilar.setOnClickListener(view -> {
            initTabSimilar();
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Show-Tab", MODE_PRIVATE).edit();
            editor.putInt("STab", 102).apply();
        });
    }

    private void checkTabs() {
        SharedPreferences prefs = requireContext().getSharedPreferences("Show-Tab", MODE_PRIVATE);
        int idTheme = prefs.getInt("STab", 0);
        switch (idTheme) {
            case 100: initTabInfo(); break;
            case 101: initTabCredits(); break;
            case 102: initTabSimilar(); break;
        }
    }

    @Override
    public void showSimilarTVShowList(List<MovieLite> similarShows) {
        if(similarShows.isEmpty()) tabSimilar.setVisibility(View.GONE);
        similarShowsAdapter.addAllShows(similarShows);
    }

    @Override
    public void showMoreSimilarShows(List<MovieLite> shows) {
        similarShowsAdapter.showMoreShows(shows);
    }

    @Override
    public void showSeasons(Show show) {
        seasonAdapter.addAllShows(show.getSeasons());
    }

    @Override
    public void showBottomSeasonDialog(int position) {
        SeasonBottomDialog seasonBottomDialog = new SeasonBottomDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SEASON", seasonAdapter.getSeason(position));
        seasonBottomDialog.setArguments(bundle);
        seasonBottomDialog.show(getChildFragmentManager(), null);
    }

    @Override
    public void showVideos(List<Video> videos) {
        if (videos.isEmpty()) videosCard.setVisibility(View.GONE);
        videoAdapter.addAllVideos(videos);
    }

    @Override
    public void showRating(Show show, float myRating) {
        if (watchedButtonAlt != null) {
            ratingText.setText(String.valueOf((int) myRating));
            if (myRating == 0.0) ratingText.setText(null);
            watchedButtonAlt.setOnLongClickListener(view -> {
                posterRating.setRating(myRating);
                ratingCard.setVisibility(View.VISIBLE);
                posterRating.setOnRatingBarChangeListener((ratingBar, v1, b) -> {
                    ratingText.setText(String.valueOf((int) ratingBar.getRating()));
                    showPresenter.updateRating(show, ratingBar.getRating());
                });
                return true;
            });
        }
    }

    @Override
    public void showProgress(boolean loadingState) {
        if (loadingState && loadingScreen != null) loadingScreen.setVisibility(View.VISIBLE);
        else if (loadingScreen != null) loadingScreen.setVisibility(View.GONE);
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
            CastBottomDialog castBottomDialog = new CastBottomDialog(showPresenter.getRouter());
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
            CrewBottomDialog crewBottomDialog = new CrewBottomDialog(showPresenter.getRouter());
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("CREW", (ArrayList<? extends Parcelable>) crews);
            crewBottomDialog.setArguments(bundle);
            crewBottomDialog.show(getChildFragmentManager(), null);
        });
    }

    @Override
    public void showTVShowScreen() {
        errorScreen.setVisibility(View.GONE);
        showScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorScreen() {
        errorScreen.setVisibility(View.VISIBLE);
        showScreen.setVisibility(View.GONE);
        refreshButton.setOnClickListener(view -> showPresenter.onFirstViewAttach());
    }

    @Override
    public void setSaveButtonEnabled(boolean enabled) {
        watchedButton.setVisibility(View.VISIBLE);
        watchedButtonAlt.setVisibility(View.GONE);
        if (enabled) {
            watchedButton.setVisibility(View.GONE);
            watchedButtonAlt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setPlanButtonEnabled(boolean enabled) {
        planningButton.setVisibility(View.VISIBLE);
        planningButtonAlt.setVisibility(View.GONE);
        if (enabled) {
            planningButton.setVisibility(View.GONE);
            planningButtonAlt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
