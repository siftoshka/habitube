package az.amorphist.poster.presentation.library;

import java.util.List;

import az.amorphist.poster.entities.movie.Movie;
import moxy.MvpView;

public interface LibraryWatchedView extends MvpView {
    void showWatchedMovies(List<Movie> movies);
}
