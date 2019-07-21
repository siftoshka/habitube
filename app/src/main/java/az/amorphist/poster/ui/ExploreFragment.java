package az.amorphist.poster.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.App.DEV_CONTACT;
import static az.amorphist.poster.di.DI.APP_SCOPE;

public class ExploreFragment extends MvpAppCompatFragment implements ExploreView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter ExplorePresenter explorePresenter;

    private Toolbar toolbar;
    private Dialog aboutDialog;
    private RecyclerView recyclerViewUpcoming, recyclerViewMovies, recyclerViewTVShows;
    private MovieAdapter movieAdapter, upcomingAdapter;
    private ShowAdapter showAdapter;

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
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        toolbar = view.findViewById(R.id.main_toolbar);
        recyclerViewUpcoming = view.findViewById(R.id.recycler_view_upcoming_movies);
        recyclerViewMovies = view.findViewById(R.id.recycler_view_movies);
        recyclerViewTVShows = view.findViewById(R.id.recycler_view_tv_shows);
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
        switch (item.getItemId()) {
            case R.id.contact_us_menu:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(DEV_CONTACT));
                startActivity(intent);
                break;
            case R.id.about_us_menu:
                showAboutDialog();
                break;
            case R.id.search_movies:
                explorePresenter.goToSearchScreen();
        }
        return false;
    }

    private void showAboutDialog() {
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.about_dialog, viewGroup, false);
        aboutDialog = new Dialog(getContext());
        aboutDialog.setContentView(dialogView);
        aboutDialog.show();

        aboutDialog.setOnDismissListener(dialog -> { });

        Button closeButton = aboutDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> aboutDialog.dismiss());
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(getContext(),"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(aboutDialog != null) {
            aboutDialog.dismiss();
        }
    }
}
