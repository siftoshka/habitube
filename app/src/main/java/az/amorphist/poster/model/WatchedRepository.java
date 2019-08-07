package az.amorphist.poster.model;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.data.MovieDAO;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WatchedRepository {

    private final MovieDAO movieDAO;

    @Inject
    public WatchedRepository(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public Completable addMovie(Movie movie) {
        return movieDAO.addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Movie>> getAllMovies() {
        return movieDAO.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
