package az.siftoshka.habitube.model.repository;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
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

    public void addMovie(Movie movie) {
        watchedRepository.movieDAO().addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addShow(Show show) {
        watchedRepository.showDAO().addShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updateMovie(Movie movie) {
        watchedRepository.movieDAO().updateMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updateShow(Show show) {
        watchedRepository.showDAO().updateShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteMovie(Movie movie) {
        watchedRepository.movieDAO().deleteMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteShow(Show show) {
        watchedRepository.showDAO().deleteShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public Single<List<Movie>> getAllMovies() {
        return watchedRepository.movieDAO().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<List<Show>> getAllShows() {
        return watchedRepository.showDAO().getShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return Single.just(watchedRepository.movieDAO().getMovieCount(movieId))
                .map(integer -> integer > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> isShowExists(int shiwId) {
        return Single.just(watchedRepository.showDAO().getShowCount(shiwId))
                .map(integer -> integer > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Movie> getMovie(int postId) {
        return watchedRepository.movieDAO().getMovieById(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Show> getShow(int postId) {
        return watchedRepository.showDAO().getShowById(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public void deleteAllMovies() {
        watchedRepository.movieDAO().deleteAllMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteAllShows() {
        watchedRepository.showDAO().deleteAllShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
