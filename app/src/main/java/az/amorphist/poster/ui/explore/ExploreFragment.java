package az.amorphist.poster.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.MovieAdapter;
import az.amorphist.poster.adapters.ShowAdapter;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.presentation.explore.ExplorePresenter;
import az.amorphist.poster.presentation.explore.ExploreView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.Constants.DI.APP_SCOPE;

public class ExploreFragment extends MvpAppCompatFragment implements ExploreView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter ExplorePresenter explorePresenter;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_upcoming_movies) RecyclerView recyclerViewUpcoming;
    @BindView(R.id.recycler_view_movies) RecyclerView recyclerViewMovies;
    @BindView(R.id.recycler_view_tv_shows) RecyclerView recyclerViewTVShows;

    private MovieAdapter movieAdapter, upcomingAdapter;
    private ShowAdapter showAdapter;
    private Unbinder unbinder;

    @ProvidePresenter
    ExplorePresenter explorePresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(ExplorePresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        upcomingAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedUpcomingScreen( postId));
        movieAdapter = new MovieAdapter(postId -> explorePresenter.goToDetailedMovieScreen( postId));
        showAdapter = new ShowAdapter(showId -> explorePresenter.goToDetailedShowScreen( showId));
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
    }


    @Override
    public void showUpcomingMovieList(List<MovieLite> upcomingList) {
        upcomingAdapter.addAllMovies(upcomingList);
    }

    @Override
    public void showMovieList(List<MovieLite> movies) {
        movieAdapter.addAllMovies(movies);
    }

    @Override
    public void showTVShowList(List<MovieLite> tvShows) {
        showAdapter.addAllMovies(tvShows);
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
        Toast.makeText(getContext(),"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
