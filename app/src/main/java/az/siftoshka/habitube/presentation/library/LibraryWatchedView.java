package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import moxy.MvpView;

public interface LibraryWatchedView extends MvpView {
    void showWatchedMovies(List<Movie> movies);
}
