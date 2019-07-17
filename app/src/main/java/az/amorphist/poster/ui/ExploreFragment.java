package az.amorphist.poster.ui;

import android.app.Dialog;
import android.content.Context;
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

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.MovieAdapter;
import az.amorphist.poster.adapters.ShowAdapter;
import az.amorphist.poster.adapters.UpcomingAdapter;
import az.amorphist.poster.entities.MovieLite;
import az.amorphist.poster.presentation.explore.ExplorePresenter;
import az.amorphist.poster.presentation.explore.ExploreView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.App.DEV_CONTACT;

public class ExploreFragment extends MvpAppCompatFragment implements ExploreView, Toolbar.OnMenuItemClickListener {

    @Inject Context context;
    @InjectPresenter ExplorePresenter explorePresenter;

    private Toolbar toolbar;
    private Dialog aboutDialog;
    private RecyclerView recyclerViewUpcoming, recyclerViewMovies, recyclerViewTVShows;
    private MovieAdapter movieAdapter;
    private ShowAdapter showAdapter;
    private UpcomingAdapter upcomingAdapter;

    @ProvidePresenter
    ExplorePresenter explorePresenter() {
        return Toothpick.openScope("APP_SCOPE").getInstance(ExplorePresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, Toothpick.openScope("APP_SCOPE"));
        upcomingAdapter = new UpcomingAdapter(new UpcomingAdapter.UpcomingItemClickListener() {
            @Override
            public void onPostClicked(int position) {
                explorePresenter.goToDetailedUpcomingScreen(position + 1);
            }
        });
        movieAdapter = new MovieAdapter(new MovieAdapter.MovieItemClickListener() {
            @Override
            public void onPostClicked(int position) {
                explorePresenter.goToDetailedMovieScreen(position + 1);
            }
        });
        showAdapter = new ShowAdapter(new ShowAdapter.ShowItemClickListener() {
            @Override
            public void onPostClicked(int position) {
                explorePresenter.goToDetailedShowScreen(position + 1);
            }
        });
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

        LinearLayoutManager layoutManagerUpcoming = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewUpcoming.setLayoutManager(layoutManagerUpcoming);
        recyclerViewUpcoming.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUpcoming.setHasFixedSize(true);
        recyclerViewUpcoming.setAdapter(upcomingAdapter);

        LinearLayoutManager layoutManagerMovies = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewMovies.setLayoutManager(layoutManagerMovies);
        recyclerViewMovies.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMovies.setHasFixedSize(true);
        recyclerViewMovies.setAdapter(movieAdapter);

        LinearLayoutManager layoutManagerTVShows = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTVShows.setLayoutManager(layoutManagerTVShows);
        recyclerViewTVShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTVShows.setHasFixedSize(true);
        recyclerViewTVShows.setAdapter(showAdapter);
    }

    @Override
    public void getUpcomingMovieList(List<MovieLite> upcomingList) {
        upcomingAdapter.addAllMovies(upcomingList);
    }

    @Override
    public void getMovieList(List<MovieLite> movies) {
        movieAdapter.addAllMovies(movies);
    }

    @Override
    public void getTVShowList(List<MovieLite> tvShows) {
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
        View dialogView = LayoutInflater.from(context).inflate(R.layout.about_dialog, viewGroup, false);
        aboutDialog = new Dialog(getContext());
        aboutDialog.setContentView(dialogView);
        aboutDialog.show();

        aboutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });

        Button closeButton = aboutDialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(context,"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(aboutDialog != null) {
            aboutDialog.dismiss();
        }
    }
}
