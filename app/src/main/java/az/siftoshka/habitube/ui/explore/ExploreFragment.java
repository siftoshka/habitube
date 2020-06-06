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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.MovieAdapter;
import az.siftoshka.habitube.adapters.ShowAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.explore.ExplorePresenter;
import az.siftoshka.habitube.presentation.explore.ExploreView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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
    @BindView(R.id.explore_netflix) LinearLayout discoverNetflix;

    private MovieAdapter movieAdapter, upcomingAdapter;
    private ShowAdapter showAdapter, airTodayAdapter;
    private MessageListener messageListener;
    private Unbinder unbinder;

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
        discoverNetflix.setOnClickListener(view1 -> showNetflixDialog());
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

        initSearchDefault();
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
        refreshButton.setOnClickListener(view -> explorePresenter.addContent());
    }

    private void initSearchDefault() {
        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
        editor.putInt("Search", 100);
        editor.apply();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
