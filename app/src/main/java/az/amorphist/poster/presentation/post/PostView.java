package az.amorphist.poster.presentation.post;

import java.util.List;

import az.amorphist.poster.entities.movielite.MovieLite;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PostView extends MvpView {
    void getMovie(
            String image,
            String background,
            String title,
            String date,
            double  rate,
            int views,
            String description
    );
    void getShow(
            String image,
            String background,
            String title,
            String date,
            float  rate,
            float views,
            String description
    );
    void getPerson( String image,
                    String name,
                    String birthdate,
                    String placeOfBirth,
                    double popularity,
                    String bio);
    void showSimilarMovieList(List<MovieLite> similarMovies);
    void showSimilarTVShowList(List<MovieLite> similarShows);
    void showProgress(boolean loadingState);
    void showMovieScreen();
    void showErrorScreen();
    void showTVShowScreen();
    void showPersonScreen();
}
