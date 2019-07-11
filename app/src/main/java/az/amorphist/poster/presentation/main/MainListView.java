package az.amorphist.poster.presentation.main;

import java.util.List;

import az.amorphist.poster.entities.MovieLite;
import moxy.MvpView;

public interface MainListView extends MvpView {
    void getUpcomingMovieList(List<MovieLite> upcomingList);
    void getMovieList(List<MovieLite> movies);
    void getTVShowList(List<MovieLite> tvShows);
    void unsuccessfulQueryError();
}
