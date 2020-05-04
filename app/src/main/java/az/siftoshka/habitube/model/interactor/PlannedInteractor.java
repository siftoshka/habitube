package az.siftoshka.habitube.model.interactor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.repository.PlannedRepository;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class PlannedInteractor {

    private final PlannedRepository plannedRepository;

    @Inject
    public PlannedInteractor(PlannedRepository plannedRepository) {
        this.plannedRepository = plannedRepository;
    }

    public void addMovie(Movie movie) {
        plannedRepository.addMovie(movie);
    }

    public void addMovieFB(Movie movie) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            plannedRepository.addMovieToPlanning(movie, user);
    }

    public void addShow(Show show) {
        plannedRepository.addShow(show);
    }

    public void addShowFB(Show show) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            plannedRepository.addShowToPlanning(show, user);
    }

    public void updateMovie(Movie movie) {
        plannedRepository.updateMovie(movie);
    }

    public void updateShow(Show show) {
        plannedRepository.updateShow(show);
    }

    public Single<List<Movie>> getAllMovies() {
        return plannedRepository.getAllMovies();
    }

    public Single<List<Show>> getAllShows() {
        return plannedRepository.getAllShows();
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return plannedRepository.isMovieExists(movieId);
    }

    public Single<Boolean> isShowExists(int showId) {
        return plannedRepository.isShowExists(showId);
    }

    public Maybe<Movie> getMovie(int movieId) {
        return plannedRepository.getMovie(movieId);
    }

    public Maybe<Show> getShow(int showId) {
        return plannedRepository.getShow(showId);
    }

    public void deleteMovie(Movie movie) {
        plannedRepository.deleteMovie(movie);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            plannedRepository.deleteMovieFromPlanning(movie, user);
        }
    }

    public void deleteShow(Show show) {
        plannedRepository.deleteShow(show);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            plannedRepository.deleteShowFromPlanning(show, user);
        }
    }

    public void deleteAllMovies() {
        plannedRepository.deleteAllMovies();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            plannedRepository.deleteAllMoviesFromPlanning(user);
        }
    }

    public void deleteAllShows() {
        plannedRepository.deleteAllShows();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            plannedRepository.deleteAllShowsFromPlanning(user);
        }
    }
}
