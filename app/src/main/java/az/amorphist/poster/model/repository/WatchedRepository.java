package az.amorphist.poster.model.repository;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.data.WatchedRoomRepository;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WatchedRepository {

    private final WatchedRoomRepository watchedRepository;

    @Inject
    public WatchedRepository(WatchedRoomRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public Completable addMovie(Movie movie) {
        Log.e("MOVIE OBJECT", movie.toString());
        return watchedRepository.movieDAO().addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<Movie>> getAllMovies() {
        Log.e("F","FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
        return watchedRepository.movieDAO().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}