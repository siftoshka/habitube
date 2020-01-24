package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import moxy.MvpView;

public interface LibraryWatchedView extends MvpView {
    void showWatchedMovies(List<Movie> movies);
    void showWatchedShows(List<Show> shows);
}
