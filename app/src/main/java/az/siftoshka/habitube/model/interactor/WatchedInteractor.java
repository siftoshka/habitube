package az.siftoshka.habitube.model.interactor;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.repository.WatchedRepository;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;

public class WatchedInteractor {

    private final WatchedRepository watchedRepository;

    @Inject
    public WatchedInteractor(WatchedRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie) {
        watchedRepository.addMovie(movie);
    }

    public void addShow(Show show) {
        watchedRepository.addShow(show);
    }

    public void updateMovie(Movie movie) {
        watchedRepository.updateMovie(movie);
    }

    public void updateShow(Show show) {
        watchedRepository.updateShow(show);
    }

    public Single<List<Movie>> getAllMovies() {
        return watchedRepository.getAllMovies();
    }

    public Single<List<Show>> getAllShows() {
        return watchedRepository.getAllShows();
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return watchedRepository.isMovieExists(movieId);
    }

    public Single<Boolean> isShowExists(int showId) {
        return watchedRepository.isShowExists(showId);
    }

    public void deleteMovie(Movie movie) {
        watchedRepository.deleteMovie(movie);
    }

    public void deleteShow(Show show) {
        watchedRepository.deleteShow(show);
    }

    public Single<Movie> getMovie(int postId) {
        return watchedRepository.getMovie(postId);
    }

    public Single<Show> getShow(int postId) {
        return watchedRepository.getShow(postId);
    }
}
