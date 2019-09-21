package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import moxy.MvpView;

public interface LibraryPlanningView extends MvpView {
    void showPlannedMovies(List<Movie> movies);

}
