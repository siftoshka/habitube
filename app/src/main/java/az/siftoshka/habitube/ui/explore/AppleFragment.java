package az.siftoshka.habitube.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.DiscoverAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.presentation.explore.ApplePresenter;
import az.siftoshka.habitube.presentation.explore.AppleView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class AppleFragment extends MvpAppCompatFragment implements AppleView {

    @InjectPresenter ApplePresenter discoverPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout cToolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private Unbinder unbinder;
    private DiscoverAdapter discoverAdapter;
    private int index;

    @ProvidePresenter
    ApplePresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(ApplePresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            index = bundle.getInt("Discover-A");
            if (index == 0) discoverPresenter.showApplePopular(1);
            if (index == 1) discoverPresenter.showAppleBest(1);
            if (index == 2) discoverPresenter.showAppleNew(1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_discover, container, false);
        discoverAdapter = new DiscoverAdapter(id -> discoverPresenter.goToShowScreen(id));
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
        if (index == 0) paginatePopular();
        else if (index == 1) paginateBest();
        else paginateNew();
    }

    private void paginatePopular() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        discoverPresenter.showMoreApplePopular(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateBest() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        discoverPresenter.showMoreAppleBest(page);
                        page++;
                    }
                } catch (Exception ignored) {}
            }
        });
    }

    private void paginateNew() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                try {
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        discoverPresenter.showMoreAppleNew(page);
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
