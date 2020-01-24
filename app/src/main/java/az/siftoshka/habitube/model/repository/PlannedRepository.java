package az.siftoshka.habitube.model.repository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.model.data.PlannedRoomRepository;
import az.siftoshka.habitube.model.data.WatchedRoomRepository;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PlannedRepository {

    private final PlannedRoomRepository plannedRepository;

    @Inject
    public PlannedRepository(PlannedRoomRepository plannedRepository) {
        this.plannedRepository = plannedRepository;
    }

    public void addMovie(Movie movie, List<MovieGenre> movieGenres) {
        List<MovieGenre> genreList = new ArrayList<>();
        for (MovieGenre genre : movieGenres) {
            genreList.add(new MovieGenre(movie.getId(), genre.getId(), genre.getName()));
        }

        plannedRepository.movieDAO().addMovie(movie)
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

    public Single<List<Movie>> getAllMovies() {
        return plannedRepository.movieDAO().getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace);
    }

    public Single<Boolean> isMovieExists(int movieId) {
        return Single.just(plannedRepository.movieDAO().getMovieById(movieId))
                .map(integer -> integer > 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

