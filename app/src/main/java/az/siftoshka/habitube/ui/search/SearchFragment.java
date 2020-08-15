package az.siftoshka.habitube.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.SearchAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.model.system.KeyboardBehavior;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.search.SearchPresenter;
import az.siftoshka.habitube.presentation.search.SearchView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.content.Context.MODE_PRIVATE;
import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class SearchFragment extends MvpAppCompatFragment implements SearchView {

    @InjectPresenter SearchPresenter searchPresenter;

    @BindView(R.id.search_toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view_search) RecyclerView recyclerViewSearch;
    @BindView(R.id.search_icon) LinearLayout searchIcon;
    @BindView(R.id.nothing_icon) LinearLayout nothingIcon;
    @BindView(R.id.search_bar) androidx.appcompat.widget.SearchView searchView;
    @BindView(R.id.page_down) ImageView pageDown;
    @BindView(R.id.radio_group) LinearLayout radioLayout;
    @BindView(R.id.radio_multi) RadioButton radioMulti;
    @BindView(R.id.radio_movie) RadioButton radioMovie;
    @BindView(R.id.radio_tv) RadioButton radioShow;
    @BindView(R.id.radio_people) RadioButton radioPeople;

    private SearchAdapter searchAdapter;
    private MessageListener messageListener;
    private KeyboardBehavior keyboardBehavior;
    private Unbinder unbinder;

    @ProvidePresenter
    SearchPresenter searchPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(SearchPresenter.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
        if (context instanceof KeyboardBehavior) this.keyboardBehavior = (KeyboardBehavior) context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAdapter = new SearchAdapter((id, mediaType) -> {searchPresenter.goToDetailedScreen(id, mediaType); keyboardBehavior.hideKeyboard();},
            postName -> messageListener.showText(postName));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView.requestFocus();
        searchView.onActionViewExpanded();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerViewSearch.setLayoutManager(layoutManager);
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setAdapter(searchAdapter);
        pageDown.setOnClickListener(view1 -> pageCheck());
        toolbar.setNavigationOnClickListener(v -> {
            searchPresenter.goBack();
            keyboardBehavior.hideKeyboard();
        });

        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        EditText searchText = searchView.findViewById(R.id.search_src_text);
        searchText.setText(null);

        init();
        SharedPreferences prefs = requireContext().getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean adult = idAdult == 1;
        SharedPreferences prefsSearch = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE);
        int search = prefsSearch.getInt("Search", 0);

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                System.out.println(newText);
                if (!TextUtils.isEmpty(newText)) {
                    if (search == 100 && radioMulti.isChecked()) {
                        System.out.println("MULTI");
                        searchPresenter.multiSearch(newText, getResources().getString(R.string.language), adult);
                        paginateMultiSearch(newText, getResources().getString(R.string.language), adult);
                    }
                    if (search == 101 || radioMovie.isChecked()) {
                        searchPresenter.movieSearch(newText, getResources().getString(R.string.language), adult);
                        paginateMovieSearch(newText, getResources().getString(R.string.language), adult);
                    }
                    if (search == 102 || radioShow.isChecked()) {
                        searchPresenter.showSearch(newText, getResources().getString(R.string.language), adult);
                        paginateShowSearch(newText, getResources().getString(R.string.language), adult);
                    }
                    if (search == 103 || radioPeople.isChecked()) {
                        System.out.println("PERSON");
                        searchPresenter.personSearch(newText, getResources().getString(R.string.language), adult);
                        paginatePersonSearch(newText, getResources().getString(R.string.language), adult);
                    }
                }
                return true;
            }
        });

        closeButton.setOnClickListener(view1 -> {
            searchAdapter.clean();
            searchIcon.setVisibility(View.VISIBLE);
            recyclerViewSearch.setVisibility(View.GONE);
            nothingIcon.setVisibility(View.GONE);
            keyboardBehavior.hideKeyboard();
            searchText.setText(null);
        });

        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                keyboardBehavior.hideKeyboard();
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void paginateMultiSearch(String text, String language, boolean adult) {
        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerViewSearch.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    searchPresenter.moreMultiSearch(text, page, language, adult);
                    page++;
                }
            }
        });
    }

    private void paginateMovieSearch(String text, String language, boolean adult) {
        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerViewSearch.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    searchPresenter.moreMovieSearch(text, page, language, adult);
                    page++;
                }
            }
        });
    }

    private void paginateShowSearch(String text, String language, boolean adult) {
        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerViewSearch.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    searchPresenter.moreTVShowSearch(text, page, language, adult);
                    page++;
                }
            }
        });
    }

    private void paginatePersonSearch(String text, String language, boolean adult) {
        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int page = 2;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerViewSearch.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    searchPresenter.morePersonSearch(text, page, language, adult);
                    page++;
                }
            }
        });
    }

    @Override
    public void showSearchedMediaList(List<MovieLite> searchResult) {
        searchAdapter.addAllMedia(searchResult);
        nothingIcon.setVisibility(View.GONE);
        searchIcon.setVisibility(View.GONE);
        recyclerViewSearch.setVisibility(View.VISIBLE);
        if (searchResult.size() == 0) {
            searchIcon.setVisibility(View.GONE);
            recyclerViewSearch.setVisibility(View.GONE);
            nothingIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showMoreSearchResults(List<MovieLite> searchResult) {
        searchAdapter.addMoreMedia(searchResult);
    }

    private void radioListener() {
        radioMulti.setOnClickListener(view -> {
            radioLayout.setVisibility(View.GONE);
            pageDown.setImageResource(R.drawable.ic_down_arrow_alt);
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
            editor.putInt("Search", 100);
            editor.apply();
        });
        radioMovie.setOnClickListener(view -> {
            radioLayout.setVisibility(View.GONE);
            pageDown.setImageResource(R.drawable.ic_down_arrow_alt);
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
            editor.putInt("Search", 101);
            editor.apply();
        });
        radioShow.setOnClickListener(view -> {
            radioLayout.setVisibility(View.GONE);
            pageDown.setImageResource(R.drawable.ic_down_arrow_alt);
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
            editor.putInt("Search", 102);
            editor.apply();
        });
        radioPeople.setOnClickListener(view -> {
            radioLayout.setVisibility(View.GONE);
            pageDown.setImageResource(R.drawable.ic_down_arrow_alt);
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Search-Settings", MODE_PRIVATE).edit();
            editor.putInt("Search", 103);
            editor.apply();
        });
    }

    private void pageCheck() {
        if (pageDown.getDrawable().getConstantState() == pageDown.getResources().getDrawable(R.drawable.ic_down_arrow_alt).getConstantState()) {
            radioLayout.setVisibility(View.VISIBLE); radioListener();
            pageDown.setImageResource(R.drawable.ic_up_arrow_alt);
        } else {
            radioLayout.setVisibility(View.GONE);
            pageDown.setImageResource(R.drawable.ic_down_arrow_alt);
        }
    }

    private void init() {
        searchIcon.setVisibility(View.VISIBLE);
        recyclerViewSearch.setVisibility(View.GONE);
        nothingIcon.setVisibility(View.GONE);
    }

    @Override
    public void unsuccessfulQueryError() {
        messageListener.showInternetError("Unexpected error");
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
