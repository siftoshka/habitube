package az.siftoshka.habitube.ui.explore;

import android.content.Context;
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

import com.google.android.material.button.MaterialButton;

import java.util.List;

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

        LinearLayoutManager layoutManagerMovies = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovies.setLayoutManager(layoutManagerMovies);
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setAdapter(movieAdapter);

        LinearLayoutManager layoutManagerTVShows = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTVShows.setLayoutManager(layoutManagerTVShows);
        recyclerViewTVShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTVShows.setHasFixedSize(true);
        recyclerViewTVShows.setAdapter(showAdapter);

        LinearLayoutManager layoutManagerAirToday = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAirToday.setLayoutManager(layoutManagerAirToday);
        recyclerViewAirToday.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAirToday.setHasFixedSize(true);
        recyclerViewAirToday.setAdapter(airTodayAdapter);
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

    @Override
    public void showUpcomingMovieList(List<MovieLite> upcomingList) {
        upcomingAdapter.addAllMovies(upcomingList);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        upcomingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMovieList(List<MovieLite> movies) {
        movieAdapter.addAllMovies(movies);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        trendingMovieScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTVShowList(List<MovieLite> tvShows) {
        showAdapter.addAllMovies(tvShows);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        trendingShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAirTodayShows(List<MovieLite> tvShows) {
        airTodayAdapter.addAllMovies(tvShows);
        progressBar.setVisibility(View.GONE);
        errorScreen.setVisibility(View.GONE);
        airTodayShowScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void unsuccessfulQueryError() {
        errorScreen.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        refreshButton.setOnClickListener(view -> explorePresenter.addContent());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
