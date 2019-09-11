package az.siftoshka.habitube.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.model.data.WatchedRoomRepository;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WatchedRepository {

    private final WatchedRoomRepository watchedRepository;

    @Inject
    public WatchedRepository(WatchedRoomRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie, List<MovieGenre> movieGenres) {
        List<MovieGenre> genreList = new ArrayList<>();
        for (MovieGenre genre : movieGenres) {
            genreList.add(new MovieGenre(movie.getId(), genre.getId(), genre.getName()));
        }

        watchedRepository.movieDAO().addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> addMovieGenres(genreList))
                .subscribe();
    }

    public void deleteMovie(Movie movie) {
        watchedRepository.movieDAO().deleteMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        deleteMovieGenres(movie.getId());
    }

    public Single<List<Movie>> getAllMovies() {
        return watchedRepository.movieDAO().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return Single.just(watchedRepository.movieDAO().getMovieById(movieId))
                .map(integer -> integer > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void addMovieGenres(List<MovieGenre> movieGenres) {
        watchedRepository.movieDAO().addMovieGenres(movieGenres)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void deleteMovieGenres(int id) {
        watchedRepository.movieDAO().deleteMovieGenres(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
