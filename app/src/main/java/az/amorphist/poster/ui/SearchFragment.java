package az.amorphist.poster.ui;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.utils.animation.VegaXLayoutManager;
import az.amorphist.poster.adapters.SearchAdapter;
import az.amorphist.poster.entities.MovieLite;
import az.amorphist.poster.presentation.search.SearchPresenter;
import az.amorphist.poster.presentation.search.SearchView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class SearchFragment extends MvpAppCompatFragment implements SearchView {

    @Inject Context context;
    @InjectPresenter SearchPresenter searchPresenter;

    private SearchAdapter searchAdapter;
    private Toolbar toolbar;
    private RecyclerView recyclerViewSearch;
    private androidx.appcompat.widget.SearchView searchView;

    @ProvidePresenter
    SearchPresenter searchPresenter() {
        return Toothpick.openScope("APP_SCOPE").getInstance(SearchPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope("APP_SCOPE"));

        searchAdapter = new SearchAdapter(new SearchAdapter.SearchItemClickListener() {
            @Override
            public void onPostClicked(int position) {

            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = view.findViewById(R.id.search_toolbar);
        searchView = view.findViewById(R.id.search_bar);
        recyclerViewSearch = view.findViewById(R.id.recycler_view_search);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.requestFocus();
        searchView.onActionViewExpanded();

        LinearLayoutManager layoutManagerMovies = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recyclerViewSearch.setLayoutManager(new VegaXLayoutManager());
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setAdapter(searchAdapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPresenter.goBack();
            }
        });

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPresenter.searchMedia(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }
        });
    }

    @Override
    public void getSearchedMediaList(List<MovieLite> searchResult) {
        searchAdapter.addAllMedia(searchResult);
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(context,"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }
}
