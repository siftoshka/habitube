package az.amorphist.habitube.presentation.library;

import java.util.List;

import az.amorphist.habitube.entities.movie.Movie;
import moxy.MvpView;

public interface LibraryWatchedView extends MvpView {
    void showWatchedMovies(List<Movie> movies);
}
