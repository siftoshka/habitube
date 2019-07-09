package az.amorphist.poster.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.MovieAdapter;
import az.amorphist.poster.adapters.ShowAdapter;
import az.amorphist.poster.entities.Movie;
import az.amorphist.poster.presentation.presenters.MainListPresenter;
import az.amorphist.poster.presentation.views.MainListView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class MainListFragment extends MvpAppCompatFragment implements MainListView {

    @Inject Context context;
    @InjectPresenter MainListPresenter mainListPresenter;

    private RecyclerView recyclerViewMovies, recyclerViewTVShows;
    private MovieAdapter movieAdapter;
    private ShowAdapter showAdapter;

    @ProvidePresenter
    MainListPresenter mainListPresenter() {
        return Toothpick.openScope("APP_SCOPE").getInstance(MainListPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieAdapter = new MovieAdapter(new MovieAdapter.MovieItemClickListener() {
            @Override
            public void onPostClicked(int position) {
                mainListPresenter.goToDetailedMovieScreen(position);
            }
        });
        showAdapter = new ShowAdapter(new ShowAdapter.ShowItemClickListener() {
            @Override
            public void onPostClicked(int position) {
                mainListPresenter.goToDetailedShowScreen(position);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        recyclerViewMovies = view.findViewById(R.id.recycler_view_movies);
        recyclerViewTVShows = view.findViewById(R.id.recycler_view_tv_shows);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
    public void getMovieList(List<Movie> movies) {
        movieAdapter.addAllMovies(movies);
    }

    @Override
    public void getTVShowList(List<Movie> tvShows) {
        showAdapter.addAllMovies(tvShows);
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(context,"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }
}
