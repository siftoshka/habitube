package az.siftoshka.habitube.model.repository;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.firebase.Media;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.data.PlannedRoomRepository;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static az.siftoshka.habitube.Constants.FIREBASE.PLANNING_MOVIE;
import static az.siftoshka.habitube.Constants.FIREBASE.PLANNING_SHOW;

public class PlannedRepository {

    private final PlannedRoomRepository plannedRepository;
    private DatabaseReference databaseReference;

    @Inject
    public PlannedRepository(PlannedRoomRepository plannedRepository) {
        this.plannedRepository = plannedRepository;
    }

    public void addMovie(Movie movie) {
        plannedRepository.movieDAO().addMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    public void addShow(Show show) {
        plannedRepository.showDAO().addShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }

    public void updateMovie(Movie movie) {
        plannedRepository.movieDAO().updateMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void updateShow(Show show) {
        plannedRepository.showDAO().updateShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteMovie(Movie movie) {
        plannedRepository.movieDAO().deleteMovie(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteShow(Show show) {
        plannedRepository.showDAO().deleteShow(show)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public Single<List<Movie>> getAllMovies() {
        return plannedRepository.movieDAO().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<List<Show>> getAllShows() {
        return plannedRepository.showDAO().getShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return Single.just(plannedRepository.movieDAO().getMovieCount(movieId))
                .map(integer -> integer > 0)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Boolean> isShowExists(int shiwId) {
        return Single.just(plannedRepository.showDAO().getShowCount(shiwId))
                .map(integer -> integer > 0)
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Movie> getMovie(int movieId) {
        return plannedRepository.movieDAO().getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Show> getShow(int showId) {
        return plannedRepository.showDAO().getShowById(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public void deleteAllMovies() {
        plannedRepository.movieDAO().deleteAllMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteAllShows() {
        plannedRepository.showDAO().deleteAllShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void addMovieToPlanning(int id, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_MOVIE)
                .child(user.getUid());
        databaseReference.child(String.valueOf(id))
                .setValue(new Media(id));
    }

    public void addShowToPlanning(int id, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_SHOW)
                .child(user.getUid());
        databaseReference.child(String.valueOf(id))
                .setValue(new Media(id));
    }

    public void deleteMovieFromPlanning(Movie movie, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_MOVIE)
                .child(user.getUid());
        databaseReference.child(String.valueOf(movie.getId())).removeValue();
    }

    public void deleteShowFromPlanning(Show show, FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_SHOW)
                .child(user.getUid());
        databaseReference.child(String.valueOf(show.getId())).removeValue();
    }

    public void deleteAllMoviesFromPlanning(FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_MOVIE)
                .child(user.getUid());
        databaseReference.removeValue();
    }

    public void deleteAllShowsFromPlanning(FirebaseUser user) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(PLANNING_SHOW)
                .child(user.getUid());
        databaseReference.removeValue();
    }
}
