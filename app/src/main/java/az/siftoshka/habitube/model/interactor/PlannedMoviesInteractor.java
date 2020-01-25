package az.siftoshka.habitube.model.interactor;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.repository.PlannedRepository;
import io.reactivex.Single;

public class PlannedMoviesInteractor {

    private final PlannedRepository plannedRepository;

    @Inject
    public PlannedMoviesInteractor(PlannedRepository plannedRepository) {
        this.plannedRepository = plannedRepository;
    }

    public void addMovie(Movie movie) {
        plannedRepository.addMovie(movie);
    }

    public void addShow(Show show) {
        plannedRepository.addShow(show);
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

    public void deleteMovie(Movie movie) {
        plannedRepository.deleteMovie(movie);
    }

    public void deleteShow(Show show) {
        plannedRepository.deleteShow(show);
    }
}
