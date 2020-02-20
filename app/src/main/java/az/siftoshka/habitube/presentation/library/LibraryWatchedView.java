package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import moxy.MvpView;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface LibraryWatchedView extends MvpView {
    void showWatchedMovies(List<Movie> movies);
    void showWatchedShows(List<Show> shows);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showOfflineCard(Movie movie);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showOfflineCard(Show show);
}
