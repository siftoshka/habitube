package az.siftoshka.habitube.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.adapters.SearchAdapter;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.model.system.KeyboardBehavior;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.search.SearchPresenter;
import az.siftoshka.habitube.presentation.search.SearchView;
import az.siftoshka.habitube.utils.animation.VegaXLayoutManager;
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
    @BindView(R.id.search_icon) ImageView searchIcon;
    @BindView(R.id.nothing_icon) LinearLayout nothingIcon;
    @BindView(R.id.search_bar) androidx.appcompat.widget.SearchView searchView;

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
        if (context instanceof MessageListener) {
            this.messageListener = (MessageListener) context;
        }
        if (context instanceof KeyboardBehavior) {
            this.keyboardBehavior = (KeyboardBehavior) context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchAdapter = new SearchAdapter((id, mediaType) -> searchPresenter.goToDetailedScreen(id, mediaType));
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

        recyclerViewSearch.setLayoutManager(new VegaXLayoutManager());
        recyclerViewSearch.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setAdapter(searchAdapter);

        toolbar.setNavigationOnClickListener(v -> searchPresenter.goBack());

        init();
        SharedPreferences prefs = requireContext().getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean adult = idAdult == 1;

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (!TextUtils.isEmpty(newText)) {
                    searchPresenter.searchMedia(newText, getResources().getString(R.string.language), adult);
                }
                return true;
            }
        });
        
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(view1 -> {
            searchAdapter.clean();
            searchIcon.setVisibility(View.VISIBLE);
            recyclerViewSearch.setVisibility(View.GONE);
            nothingIcon.setVisibility(View.GONE);
            keyboardBehavior.hideKeyboard();
            EditText et = searchView.findViewById(R.id.search_src_text);
            et.setText(null);
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

    @Override
    public void showSearchedMediaList(List<MovieLite> searchResult) {
        if (searchResult.size() == 0) {
            searchIcon.setVisibility(View.GONE);
            recyclerViewSearch.setVisibility(View.GONE);
            nothingIcon.setVisibility(View.VISIBLE);
        } else {
            searchAdapter.addAllMedia(searchResult);
            nothingIcon.setVisibility(View.GONE);
            searchIcon.setVisibility(View.GONE);
            recyclerViewSearch.setVisibility(View.VISIBLE);
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
