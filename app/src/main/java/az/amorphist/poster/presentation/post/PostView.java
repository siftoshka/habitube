package az.amorphist.poster.presentation.post;

import java.util.List;

import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.show.Season;
import az.amorphist.poster.entities.show.ShowGenre;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.StateStrategyType;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface PostView extends MvpView {
    void getMovie(
            boolean isAdult,
            String image,
            String background,
            int id,
            String title,
            String date,
            int runtime,
            double  rate,
            int views,
            List<MovieGenre> genres,
            String imdbId,
            String description
    );

    void getShow(
            String image,
            String background,
            String title,
            String date,
            float  rate,
            float views,
            List<ShowGenre> showGenres,
            String description,
            List<Season> seasons
            );

    void getPerson(
            String image,
            String name,
            String birthdate,
            String placeOfBirth,
            double popularity,
            String bio
    );

    void showSimilarMovieList(List<MovieLite> similarMovies);
    void showSimilarTVShowList(List<MovieLite> similarShows);

    void showProgress(boolean loadingState);

    void showBottomSeasonDialog(int position);

    void showMovieScreen();
    void showErrorScreen();
    void showTVShowScreen();
    void showPersonScreen();
}
