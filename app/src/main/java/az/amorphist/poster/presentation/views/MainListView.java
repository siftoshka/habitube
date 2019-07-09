package az.amorphist.poster.presentation.views;

import java.util.List;

import az.amorphist.poster.entities.Movie;
import moxy.MvpView;

public interface MainListView extends MvpView {
    void getMovieList(List<Movie> movies);
    void getTVShowList(List<Movie> tvShows);
    void unsuccessfulQueryError();
}
