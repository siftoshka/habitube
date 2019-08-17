package az.amorphist.poster.presentation.post;

import java.util.List;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Season;
import az.amorphist.poster.entities.show.Show;
import az.amorphist.poster.entities.show.ShowGenre;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PostView extends MvpView {
    void getMovie(
            Movie movie
    );

    void getShow(
            Show show
            );

    void getPerson(
            Person person
    );

    void showSimilarMovieList(List<MovieLite> similarMovies);
    void showSimilarTVShowList(List<MovieLite> similarShows);

    void showProgress(boolean loadingState);

    void showBottomSeasonDialog(int position);

    void showMovieScreen();
    void showErrorScreen();
    void showTVShowScreen();
    void showPersonScreen();

    void setSaveButtonEnabled(boolean enabled);
}
