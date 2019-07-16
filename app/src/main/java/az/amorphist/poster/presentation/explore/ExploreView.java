package az.amorphist.poster.presentation.explore;

import java.util.List;

import az.amorphist.poster.entities.MovieLite;
import moxy.MvpView;

public interface ExploreView extends MvpView {
    void getUpcomingMovieList(List<MovieLite> upcomingList);
    void getMovieList(List<MovieLite> movies);
    void getTVShowList(List<MovieLite> tvShows);
    void unsuccessfulQueryError();
}
