package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import moxy.MvpView;

public interface LibraryPlanningView extends MvpView {
    void showPlannedMovies(List<Movie> movies);
    void showPlannedShows(List<Show> shows);
}
