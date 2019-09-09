package az.amorphist.poster.ui.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.SearchAdapter;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.presentation.search.SearchPresenter;
import az.amorphist.poster.presentation.search.SearchView;
import az.amorphist.poster.utils.animation.VegaXLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.Constants.DI.APP_SCOPE;

public class SearchFragment extends MvpAppCompatFragment implements SearchView {

    @InjectPresenter SearchPresenter searchPresenter;

    @BindView(R.id.search_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_search) RecyclerView recyclerViewSearch;
    @BindView(R.id.search_bar) androidx.appcompat.widget.SearchView searchView;

    private SearchAdapter searchAdapter;
    private Unbinder unbinder;

    @ProvidePresenter
    SearchPresenter searchPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(SearchPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchAdapter = new SearchAdapter((id, mediaType) -> searchPresenter.goToDetailedScreen(id, mediaType));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.requestFocus();
        searchView.onActionViewExpanded();

        recyclerViewSearch.setLayoutManager(new VegaXLayoutManager());
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setAdapter(searchAdapter);


        toolbar.setNavigationOnClickListener(v -> searchPresenter.goBack());

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if(!TextUtils.isEmpty(newText)) {
                    searchPresenter.searchMedia(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void showSearchedMediaList(List<MovieLite> searchResult) {
        searchAdapter.addAllMedia(searchResult);
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(getContext(), "Unsuccessful request", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
