package az.siftoshka.habitube.model.interactor;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.model.repository.WatchedRepository;
import io.reactivex.Single;

public class WatchedMoviesInteractor {

    private final WatchedRepository watchedRepository;

    @Inject
    public WatchedMoviesInteractor(WatchedRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie, List<MovieGenre> movieGenres) {
        watchedRepository.addMovie(movie, movieGenres);
    }

    public Single<List<Movie>> getAllMovies() {
        return watchedRepository.getAllMovies();
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return watchedRepository.isMovieExists(movieId);
    }

    public void deleteMovie(Movie movie) {
        watchedRepository.deleteMovie(movie);
    }
}
