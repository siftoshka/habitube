package az.amorphist.habitube.presentation.explore;

import java.util.List;

import az.amorphist.habitube.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ExploreView extends MvpView {
    void showUpcomingMovieList(List<MovieLite> upcomingList);
    void showMovieList(List<MovieLite> movies);
    void showTVShowList(List<MovieLite> tvShows);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void unsuccessfulQueryError();
}
