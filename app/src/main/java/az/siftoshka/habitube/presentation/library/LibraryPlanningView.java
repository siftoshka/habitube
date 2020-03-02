package az.siftoshka.habitube.presentation.library;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface LibraryPlanningView extends MvpView {

    void showPlannedMovies(List<Movie> movies);
    void showPlannedShows(List<Show> shows);
    void screenWatcher(int position);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showOfflineCard(Movie movie);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showOfflineCard(Show show);
}
