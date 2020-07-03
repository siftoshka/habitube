package az.siftoshka.habitube.ui.explore;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.DiscoverAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.presentation.explore.DiscoverPresenter;
import az.siftoshka.habitube.presentation.explore.DiscoverView;
import az.siftoshka.habitube.presentation.explore.GenrePresenter;
import az.siftoshka.habitube.presentation.explore.GenreView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class GenreFragment extends MvpAppCompatFragment implements GenreView {

    @InjectPresenter GenrePresenter genrePresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout cToolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Unbinder unbinder;
    private DiscoverAdapter discoverAdapter;
    private String genreId, yearIndexUp, yearIndexDown, selection;
    private int voteIndexUp, voteIndexDown;

    @ProvidePresenter
    GenrePresenter genrePresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(GenrePresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selection = bundle.getString("Selection");
            if (selection != null) {
                Log.e("ERROR", String.valueOf(selection));
                genreId = bundle.getString("Discover-Mg");
                yearIndexUp = bundle.getString("Discover-MyUp");
                yearIndexDown = bundle.getString("Discover-MyDown");
                voteIndexUp = bundle.getInt("Discover-MvUp");
                voteIndexDown = bundle.getInt("Discover-MvDown");
                genrePresenter.discoverMovies(1, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genreId);
                discoverAdapter = new DiscoverAdapter(id -> genrePresenter.goToMovieScreen(id));
            } else {
                genreId = bundle.getString("Discover-Sg");
                yearIndexUp = bundle.getString("Discover-SyUp");
                yearIndexDown = bundle.getString("Discover-SyDown");
                voteIndexUp = bundle.getInt("Discover-SvUp");
                voteIndexDown = bundle.getInt("Discover-SvDown");
                genrePresenter.discoverShows(1, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genreId);
                discoverAdapter = new DiscoverAdapter(id -> genrePresenter.goToShowScreen(id));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(v -> genrePresenter.goBack());
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded);
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(discoverAdapter);
        if (selection != null) paginateMovies();
        else paginateShows();
    }

    private void paginateMovies() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        genrePresenter.getMoreMovies(page, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genreId);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateShows() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        genrePresenter.getMoreShows(page, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, genreId);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    @Override
    public void showMedia(List<MovieLite> media) {
        discoverAdapter.addAllMedia(media);
    }

    @Override
    public void showMoreMedia(List<MovieLite> media) {
        discoverAdapter.showMoreMedia(media);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
