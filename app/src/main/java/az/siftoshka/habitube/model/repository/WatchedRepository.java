package az.siftoshka.habitube.model.repository;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.firebase.Media;
import az.siftoshka.habitube.entities.firebase.ShowMedia;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.data.WatchedRoomRepository;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static az.siftoshka.habitube.Constants.FIREBASE.WATCHED_MOVIE;
import static az.siftoshka.habitube.Constants.FIREBASE.WATCHED_SHOW;

public class WatchedRepository {

    private final WatchedRoomRepository watchedRepository;
    private DatabaseReference databaseReference;

    @Inject
    public WatchedRepository(WatchedRoomRepository watchedRepository) {
        this.watchedRepository = watchedRepository;
    }

    public void addMovie(Movie movie) {
        watchedRepository.movieDAO().addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    public void addShow(Show show) {
        watchedRepository.showDAO().addShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
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
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> isShowExists(int showId) {
        return Single.just(watchedRepository.showDAO().getShowCount(showId))
                .map(integer -> integer > 0)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<Movie> getMovie(int postId) {
        return watchedRepository.movieDAO().getMovieById(postId)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());}

    public Maybe<Show> getShow(int postId) {
        return watchedRepository.showDAO().getShowById(postId)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public void addMovieToWatched(Movie movie, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_MOVIE)
                .child(user.getUid());
        databaseReference.child(String.valueOf(movie.getId()))
                .setValue(new Media(movie.getId(), movie.getMyRating()));
    }

    public void addShowToWatched(Show show, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_SHOW)
                .child(user.getUid());
        databaseReference.child(String.valueOf(show.getId()))
                .setValue(new ShowMedia(show.getId(), show.getMyRating(), null));
    }

    public void deleteMovieFromWatched(Movie movie, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_MOVIE)
                .child(user.getUid());
        databaseReference.child(String.valueOf(movie.getId())).removeValue();
    }

    public void deleteShowFromWatched(Show show, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_SHOW)
                .child(user.getUid());
        databaseReference.child(String.valueOf(show.getId())).removeValue();
    }

    public void deleteAllMoviesFromWatched(FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_MOVIE)
                .child(user.getUid());
        databaseReference.removeValue();
    }

    public void deleteAllShowsFromWatched(FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(WATCHED_SHOW)
                .child(user.getUid());
        databaseReference.removeValue();
    }
}
