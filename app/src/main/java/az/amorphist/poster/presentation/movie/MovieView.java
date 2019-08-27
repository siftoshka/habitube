package az.amorphist.poster.presentation.movie;

import java.util.List;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Show;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface MovieView extends MvpView {
    void showMovie(Movie movie);

    void showSimilarMovieList(List<MovieLite> similarMovies);

    void showProgress(boolean loadingState);

    void showMovieScreen();
    void showErrorScreen();

    void setSaveButtonEnabled(boolean enabled);
}
