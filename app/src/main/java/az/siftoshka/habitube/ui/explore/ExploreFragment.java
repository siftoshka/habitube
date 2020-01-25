package az.siftoshka.habitube.ui.explore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.MovieAdapter;
import az.siftoshka.habitube.adapters.ShowAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.explore.ExplorePresenter;
import az.siftoshka.habitube.presentation.explore.ExploreView;
import az.siftoshka.habitube.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class ExploreFragment extends MvpAppCompatFragment implements ExploreView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter ExplorePresenter explorePresenter;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
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
    private LinearLayoutManager layoutManagerUpcoming, layoutManagerMovies, layoutManagerTVShows, layoutManagerAirToday;

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
        if(context instanceof MessageListener) {
            this.messageListener = (MessageListener) context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upcomingAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedUpcomingScreen(postId));
        movieAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedMovieScreen(postId));
        showAdapter = new ShowAdapter(showId -> explorePresenter.goToDetailedShowScreen(showId));
        airTodayAdapter = new ShowAdapter(showId -> explorePresenter.goToDetailedShowScreen(showId));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_explore, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.inflateMenu(R.menu.main_menu);
        toolbar.setOnMenuItemClickListener(this);

        layoutManagerUpcoming = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layoutManagerUpcoming);
        recyclerViewUpcoming.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUpcoming.setHasFixedSize(true);
        recyclerViewUpcoming.setAdapter(upcomingAdapter);

        layoutManagerMovies = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovies.setLayoutManager(layoutManagerMovies);
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setAdapter(movieAdapter);

        layoutManagerTVShows = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTVShows.setLayoutManager(layoutManagerTVShows);
        recyclerViewTVShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTVShows.setHasFixedSize(true);
        recyclerViewTVShows.setAdapter(showAdapter);

        layoutManagerAirToday = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAirToday.setLayoutManager(layoutManagerAirToday);
        recyclerViewAirToday.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAirToday.setHasFixedSize(true);
        recyclerViewAirToday.setAdapter(airTodayAdapter);
    }


    @Override
    public void showUpcomingMovieList(List<MovieLite> upcomingList) {
        upcomingAdapter.addAllMovies(upcomingList);
        progressBar.setVisibility(View.GONE);
        upcomingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieList(List<MovieLite> movies) {
        movieAdapter.addAllMovies(movies);
        progressBar.setVisibility(View.GONE);
        trendingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTVShowList(List<MovieLite> tvShows) {
        showAdapter.addAllMovies(tvShows);
        progressBar.setVisibility(View.GONE);
        trendingShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAirTodayShows(List<MovieLite> tvShows) {
        airTodayAdapter.addAllMovies(tvShows);
        progressBar.setVisibility(View.GONE);
        airTodayShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.search_movies) {
            explorePresenter.goToSearchScreen();
        }
        return false;
    }

    @Override
    public void unsuccessfulQueryError() {
        messageListener.showInternetError("Internet error");
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
