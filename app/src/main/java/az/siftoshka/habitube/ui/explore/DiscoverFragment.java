package az.siftoshka.habitube.ui.explore;

import android.os.Bundle;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class DiscoverFragment extends MvpAppCompatFragment implements DiscoverView {

    @InjectPresenter DiscoverPresenter discoverPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout cToolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Unbinder unbinder;
    private DiscoverAdapter discoverAdapter;
    private String sortSelection, yearIndex;
    private int voteIndex;

    @ProvidePresenter
    DiscoverPresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(DiscoverPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sortSelection = bundle.getString("Discover-Ms");
            if (sortSelection != null) {
                yearIndex = bundle.getString("Discover-My");
                voteIndex = bundle.getInt("Discover-Mv");
                discoverPresenter.discoverMovies(1, sortSelection, yearIndex, voteIndex);
                discoverAdapter = new DiscoverAdapter(id -> discoverPresenter.goToMovieScreen(id));
            } else {
                yearIndex = bundle.getString("Discover-Sy");
                voteIndex = bundle.getInt("Discover_Sv");
                discoverPresenter.discoverShows(1, yearIndex, voteIndex);
                discoverAdapter = new DiscoverAdapter(id -> discoverPresenter.goToShowScreen(id));
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
        toolbar.setNavigationOnClickListener(v -> discoverPresenter.goBack());
        cToolbar.setExpandedTitleTextAppearance(R.style.CollapsingExpanded);
        cToolbar.setCollapsedTitleTextAppearance(R.style.CollapsingCollapsed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(discoverAdapter);
        if (sortSelection != null) paginateMovies();
        else paginateShows();
    }

    private void paginateMovies() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    discoverPresenter.getMoreMovies(page, sortSelection, yearIndex, voteIndex);
                    page++;
                }
            }
        });
    }

    private void paginateShows() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    discoverPresenter.getMoreShows(page, yearIndex, voteIndex);
                    page++;
                }
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
