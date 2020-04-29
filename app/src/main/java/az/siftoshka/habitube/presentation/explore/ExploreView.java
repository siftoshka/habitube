package az.siftoshka.habitube.presentation.explore;

import java.util.List;

import az.siftoshka.habitube.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface ExploreView extends MvpView {

    void showUpcomingMovieList(List<MovieLite> upcomingList);
    void showMoreUpcoming(List<MovieLite> upcomingList);
    void showMovieList(List<MovieLite> movies);
    void showMoreTrending(List<MovieLite> trendingList);
    void showTVShowList(List<MovieLite> tvShows);
    void showMoreTrendingShows(List<MovieLite> trendingListShows);
    void showAirTodayShows(List<MovieLite> tvShows);
    void showMoreAirToday(List<MovieLite> airTodayList);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void unsuccessfulQueryError();
}
