package az.amorphist.poster.model.interactors;

import javax.inject.Inject;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.WatchedRepository;

public class WatchedMoviesInteractor {

    private final WatchedRepository watchedRepository;

    @Inject
    public WatchedMoviesInteractor(WatchedRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie) {
        watchedRepository.addMovie(movie);
    }

    public void getMovies() {
        watchedRepository.getAllMovies();
    }
}
