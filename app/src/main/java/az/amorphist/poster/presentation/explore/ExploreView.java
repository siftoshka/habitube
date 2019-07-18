package az.amorphist.poster.presentation.explore;

import java.util.List;

import az.amorphist.poster.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ExploreView extends MvpView {
    void getUpcomingMovieList(List<MovieLite> upcomingList);
    void getMovieList(List<MovieLite> movies);
    void getTVShowList(List<MovieLite> tvShows);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void unsuccessfulQueryError();
}
