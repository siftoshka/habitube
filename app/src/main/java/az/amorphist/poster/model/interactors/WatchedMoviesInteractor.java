package az.amorphist.poster.model.interactors;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.repository.WatchedRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WatchedMoviesInteractor {

    private final WatchedRepository watchedRepository;

    @Inject
    public WatchedMoviesInteractor(WatchedRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie) {
        watchedRepository.addMovie(movie);
    }

    public Single<List<Movie>> getMovies() {
        return watchedRepository.getAllMovies()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }
}
