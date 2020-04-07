package az.siftoshka.habitube.model.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.repository.WatchedRepository;
import io.reactivex.Maybe;
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

    public void addMovieFB(Movie movie) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.addMovieToWatched(movie, user);
    }

    public void addShow(Show show) {
        watchedRepository.addShow(show);
    }

    public void addShowFB(Show show) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.addShowToWatched(show, user);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.deleteMovieFromWatched(movie, user);
    }

    public void deleteShow(Show show) {
        watchedRepository.deleteShow(show);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.deleteShowFromWatched(show, user);
    }

    public Maybe<Movie> getMovie(int postId) {
        return watchedRepository.getMovie(postId);
    }

    public Maybe<Show> getShow(int postId) {
        return watchedRepository.getShow(postId);
    }

    public void deleteAllMovies() {
        watchedRepository.deleteAllMovies();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.deleteAllMoviesFromWatched(user);
    }

    public void deleteAllShows() {
        watchedRepository.deleteAllShows();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            watchedRepository.deleteAllShowsFromWatched(user);
    }
}
