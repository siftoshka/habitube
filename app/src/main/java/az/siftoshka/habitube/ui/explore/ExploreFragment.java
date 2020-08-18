package az.siftoshka.habitube.ui.explore;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.GenreButtonAdapter;
import az.siftoshka.habitube.adapters.MediaAdapter;
import az.siftoshka.habitube.adapters.MovieAdapter;
import az.siftoshka.habitube.adapters.ShowAdapter;
import az.siftoshka.habitube.entities.genres.Genres;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.explore.ExplorePresenter;
import az.siftoshka.habitube.presentation.explore.ExploreView;
import az.siftoshka.habitube.ui.explore.dialog.AmazonDialog;
import az.siftoshka.habitube.ui.explore.dialog.AppleDialog;
import az.siftoshka.habitube.ui.explore.dialog.DiscoverDialog;
import az.siftoshka.habitube.ui.explore.dialog.DisneyDialog;
import az.siftoshka.habitube.ui.explore.dialog.GenresDialog;
import az.siftoshka.habitube.ui.explore.dialog.NetflixDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ghyeok.stickyswitch.widget.StickySwitch;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.content.Context.MODE_PRIVATE;

public class ExploreFragment extends MvpAppCompatFragment implements ExploreView {

    @InjectPresenter ExplorePresenter explorePresenter;

    @BindView(R.id.search) ImageView searchButton;
    @BindView(R.id.recycler_view_upcoming_movies) RecyclerView recyclerViewUpcoming;
    @BindView(R.id.recycler_view_movies) RecyclerView recyclerViewMovies;
    @BindView(R.id.recycler_view_tv_shows) RecyclerView recyclerViewTVShows;
    @BindView(R.id.recycler_view_airtoday_tv_shows) RecyclerView recyclerViewAirToday;
    @BindView(R.id.genres_layout) LinearLayout genresOptions;
    @BindView(R.id.upcoming_movies) LinearLayout upcomingMovieScreen;
    @BindView(R.id.trending_movies) LinearLayout trendingMovieScreen;
    @BindView(R.id.trendind_tv_shows) LinearLayout trendingShowScreen;
    @BindView(R.id.airtoday_tv_shows) LinearLayout airTodayShowScreen;
    @BindView(R.id.explore_loading) ProgressBar progressBar;
    @BindView(R.id.explore_scroll) NestedScrollView scrollView;
    @BindView(R.id.error_screen) View errorScreen;
    @BindView(R.id.refresh) ImageView refreshButton;
    @BindView(R.id.explore_movies) MaterialButton discoverMovies;
    @BindView(R.id.explore_shows) MaterialButton discoverShows;
    @BindView(R.id.recycler_view_media) RecyclerView recyclerViewMedia;
    @BindView(R.id.recycler_view_genres) RecyclerView recyclerViewGenres;
    @BindView(R.id.genres_list) LinearLayout genresList;
    @BindView(R.id.genres_icon) ImageView genresButton;
    @BindView(R.id.sticky_switch) StickySwitch stickySwitch;


    private MovieAdapter movieAdapter, upcomingAdapter;
    private ShowAdapter showAdapter, airTodayAdapter;
    private GenreButtonAdapter genreAdapter;
    private MessageListener messageListener;
    private MediaAdapter mediaAdapter;
    private ArrayList<Genres> genres, genresShow;
    private Unbinder unbinder;
    private int index;

    @ProvidePresenter
    ExplorePresenter explorePresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(ExplorePresenter.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upcomingAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedUpcomingScreen(postId), postName -> messageListener.showText(postName));
        movieAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedMovieScreen(postId), postName -> messageListener.showText(postName));
        showAdapter = new ShowAdapter(showId -> explorePresenter.goToDetailedShowScreen(showId), postName -> messageListener.showText(postName));
        airTodayAdapter = new ShowAdapter(showId -> explorePresenter.goToDetailedShowScreen(showId), postName -> messageListener.showText(postName));
        genreAdapter = new GenreButtonAdapter(this::showGenresDialog, requireContext());
        mediaAdapter = new MediaAdapter(requireContext(), this::showMediaDialog);
        mediaAdapter.addAllMedia();
        initGenres();
        initShowGenres();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        errorScreen.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        searchButton.setOnClickListener(view1 -> explorePresenter.goToSearchScreen());
        discoverMovies.setOnClickListener(view1 -> showDiscoverMovieDialog());
        discoverShows.setOnClickListener(view1 -> showDiscoverTVShowDialog());
        LinearLayoutManager layoutManagerUpcoming = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layoutManagerUpcoming);
        recyclerViewUpcoming.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUpcoming.setHasFixedSize(true);
        recyclerViewUpcoming.setAdapter(upcomingAdapter);
        paginateUpcoming();
        LinearLayoutManager layoutManagerMovies = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovies.setLayoutManager(layoutManagerMovies);
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setAdapter(movieAdapter);
        paginateTrendingMovies();
        LinearLayoutManager layoutManagerTVShows = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTVShows.setLayoutManager(layoutManagerTVShows);
        recyclerViewTVShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTVShows.setHasFixedSize(true);
        recyclerViewTVShows.setAdapter(showAdapter);
        paginateTVShows();
        LinearLayoutManager layoutManagerAirToday = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAirToday.setLayoutManager(layoutManagerAirToday);
        recyclerViewAirToday.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAirToday.setHasFixedSize(true);
        recyclerViewAirToday.setAdapter(airTodayAdapter);
        paginateAirToday();
        GridLayoutManager layoutManagerGenres = new GridLayoutManager(getContext(), 2);
        recyclerViewGenres.setLayoutManager(layoutManagerGenres);
        recyclerViewGenres.setItemAnimator(new DefaultItemAnimator());
        recyclerViewGenres.setHasFixedSize(true);
        recyclerViewGenres.setAdapter(genreAdapter);
        LinearLayoutManager layoutManagerMedia = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerViewMedia.setLayoutManager(layoutManagerMedia);
        snapHelper.attachToRecyclerView(recyclerViewMedia);
        recyclerViewMedia.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMedia.setHasFixedSize(true);
        recyclerViewMedia.setAdapter(mediaAdapter);
        initSearchDefault();
        initGenresButtons();
    }


    private void showGenresDialog(String id) {
        GenresDialog genresDialog = new GenresDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DISCOVER-ID", index);
        bundle.putString("DISCOVER", id);
        genresDialog.setArguments(bundle);
        genresDialog.show(getChildFragmentManager(), null);
    }

    private void showDiscoverMovieDialog() {
        DiscoverDialog discoverDialog = new DiscoverDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DISCOVER", 0);
        discoverDialog.setArguments(bundle);
        discoverDialog.show(getChildFragmentManager(), null);
    }

    private void showDiscoverTVShowDialog() {
        DiscoverDialog discoverDialog = new DiscoverDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("DISCOVER", 1);
        discoverDialog.setArguments(bundle);
        discoverDialog.show(getChildFragmentManager(), null);
    }

    private void showNetflixDialog() {
        NetflixDialog netflixDialog = new NetflixDialog();
        netflixDialog.show(getChildFragmentManager(), null);
    }

    private void showAmazonDialog() {
        AmazonDialog amazonDialog = new AmazonDialog();
        amazonDialog.show(getChildFragmentManager(), null);
    }

    private void showDisneyDialog() {
        DisneyDialog disneyDialog = new DisneyDialog();
        disneyDialog.show(getChildFragmentManager(), null);
    }

    private void showAppleDialog() {
        AppleDialog appleDialog = new AppleDialog();
        appleDialog.show(getChildFragmentManager(), null);
    }

    private void toggle(View target) {
        Transition transition = new Fade();
        transition.setDuration(1000);
        transition.addTarget(target);
        TransitionManager.beginDelayedTransition((ViewGroup) Objects.requireNonNull(getView()).getParent(), transition);
    }

    private void paginateUpcoming() {
        recyclerViewUpcoming.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerViewUpcoming.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE && page <= 3) {
                        explorePresenter.addMoreUpcoming(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateTrendingMovies() {
        recyclerViewMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerViewMovies.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE && page <= 3) {
                        explorePresenter.addMoreMovies(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateTVShows() {
        recyclerViewTVShows.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerViewTVShows.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        explorePresenter.addMoreShows(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateAirToday() {
        recyclerViewAirToday.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerViewAirToday.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        explorePresenter.addMoreAirToday(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public void showUpcomingMovieList(List<MovieLite> upcomingList) {
        upcomingAdapter.addAllMovies(upcomingList);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        toggle(upcomingMovieScreen);
        showMediaCards();
        upcomingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreUpcoming(List<MovieLite> upcomingList) {
        upcomingAdapter.showMoreMovies(upcomingList);
    }

    @Override
    public void showMovieList(List<MovieLite> movies) {
        movieAdapter.addAllMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        toggle(trendingMovieScreen);
        showMediaCards();
        trendingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreTrending(List<MovieLite> trendingList) {
        movieAdapter.showMoreMovies(trendingList);
    }

    @Override
    public void showTVShowList(List<MovieLite> tvShows) {
        showAdapter.addAllShows(tvShows);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        toggle(trendingShowScreen);
        showMediaCards();
        trendingShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreTrendingShows(List<MovieLite> trendingListShows) {
        showAdapter.showMoreShows(trendingListShows);
    }

    @Override
    public void showAirTodayShows(List<MovieLite> tvShows) {
        airTodayAdapter.addAllShows(tvShows);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        toggle(airTodayShowScreen);
        showMediaCards();
        airTodayShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreAirToday(List<MovieLite> airTodayList) {
        airTodayAdapter.showMoreShows(airTodayList);
    }

    @Override
    public void unsuccessfulQueryError() {
        errorScreen.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        genresOptions.setVisibility(View.GONE);
        refreshButton.bringToFront();
        refreshButton.setOnClickListener(view -> explorePresenter.addContent());
    }

    private void initSearchDefault() {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
        editor.putInt("Search", 100);
        editor.apply();
    }

    private void showMediaDialog(int id) {
        switch (id) {
            case 1: showNetflixDialog(); break;
            case 2: showAmazonDialog(); break;
            case 3: showDisneyDialog(); break;
            case 4: showAppleDialog(); break;
        }
    }

    private void initGenres() {
        genres = new ArrayList<>();
        genres.add(new Genres("28", R.string.action, R.drawable.ic_genre_action));
        genres.add(new Genres("12", R.string.adventure, R.drawable.ic_genre_adventure));
        genres.add(new Genres("16", R.string.animation, R.drawable.ic_genre_animation));
        genres.add(new Genres("35", R.string.comedy, R.drawable.ic_genre_comedy));
        genres.add(new Genres("80", R.string.crime, R.drawable.ic_genre_crime));
        genres.add(new Genres("99", R.string.documentary, R.drawable.ic_genre_documentary));
        genres.add(new Genres("18", R.string.drama, R.drawable.ic_genre_drama));
        genres.add(new Genres("10751", R.string.family, R.drawable.ic_genre_family));
        genres.add(new Genres("14", R.string.fantasy, R.drawable.ic_genre_fantasy));
        genres.add(new Genres("36", R.string.history, R.drawable.ic_genre_history));
        genres.add(new Genres("27", R.string.horror, R.drawable.ic_genre_horror));
        genres.add(new Genres("10402", R.string.music, R.drawable.ic_genre_music));
        genres.add(new Genres("9648", R.string.mystery, R.drawable.ic_genre_mystery));
        genres.add(new Genres("10749", R.string.romance, R.drawable.ic_genre_romance));
        genres.add(new Genres("878", R.string.scifi, R.drawable.ic_genre_scifi));
        genres.add(new Genres("10770", R.string.tv_movie, R.drawable.ic_genre_tvmovie));
        genres.add(new Genres("53", R.string.thriller, R.drawable.ic_genre_thriller));
        genres.add(new Genres("10752", R.string.war, R.drawable.ic_genre_war));
        genres.add(new Genres("37", R.string.western, R.drawable.ic_genre_western));
    }

    private void initShowGenres() {
        genresShow = new ArrayList<>();
        genresShow.add(new Genres("10759", R.string.action_adventure, R.drawable.ic_genre_adventure));
        genresShow.add(new Genres("16", R.string.animation, R.drawable.ic_genre_animation));
        genresShow.add(new Genres("35", R.string.comedy, R.drawable.ic_genre_comedy));
        genresShow.add(new Genres("80", R.string.crime, R.drawable.ic_genre_crime));
        genresShow.add(new Genres("99", R.string.documentary, R.drawable.ic_genre_documentary));
        genresShow.add(new Genres("18", R.string.drama, R.drawable.ic_genre_drama));
        genresShow.add(new Genres("10751", R.string.family, R.drawable.ic_genre_family));
        genresShow.add(new Genres("10762", R.string.kids, R.drawable.ic_genre_kids));
        genresShow.add(new Genres("9648", R.string.mystery, R.drawable.ic_genre_mystery));
        genresShow.add(new Genres("10763", R.string.news, R.drawable.ic_genre_news));
        genresShow.add(new Genres("10764", R.string.reality, R.drawable.ic_genre_reality));
        genresShow.add(new Genres("10765", R.string.scifi_fantasy, R.drawable.ic_genre_scifi));
        genresShow.add(new Genres("10766", R.string.soap, R.drawable.ic_genre_soap));
        genresShow.add(new Genres("10767", R.string.talk, R.drawable.ic_genre_talk));
        genresShow.add(new Genres("10768", R.string.war_politics, R.drawable.ic_genre_war));
        genresShow.add(new Genres("37", R.string.western, R.drawable.ic_genre_western));
    }

    private void initGenresButtons() {
        genresButton.setOnClickListener(view -> {
            if (genresList.getVisibility() == View.GONE) {
                genreAdapter.addGenres(genres);
                index = 0;
                genresButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_up_arrow));
                genresList.setVisibility(View.VISIBLE);
                stickySwitch.setOnSelectedChangeListener((direction, s) -> selectedGenres(direction));
            } else {
                genresButton.setImageDrawable(requireContext().getDrawable(R.drawable.ic_down_arrow));
                genresList.setVisibility(View.GONE);
            }
        });
    }

    private void selectedGenres(StickySwitch.Direction direction) {
        if (direction == StickySwitch.Direction.LEFT) {
            genreAdapter.addGenres(genres);
            index = 0;
        } else {
            genreAdapter.addGenres(genresShow);
            index = 1;
        }
    }

    private void showMediaCards() {
        if (recyclerViewMedia.getVisibility() == View.GONE) {
            recyclerViewMedia.setVisibility(View.VISIBLE);
            genresOptions.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
