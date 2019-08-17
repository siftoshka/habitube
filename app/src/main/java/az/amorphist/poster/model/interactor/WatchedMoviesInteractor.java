package az.amorphist.poster.model.interactor;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.model.repository.WatchedRepository;
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
}
