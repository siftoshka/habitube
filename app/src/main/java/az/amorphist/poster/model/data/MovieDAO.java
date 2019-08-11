package az.amorphist.poster.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface MovieDAO {

    @Insert Completable addMovie(Movie movie);
    @Insert Completable addMovieGenres(List<MovieGenre> movieGenres);
    @Transaction @Query("SELECT * FROM movies") Single<List<Movie>> getMovies();
    @Transaction @Query("SELECT * FROM movies WHERE id = :id") Single<Movie> getMovieById(int id);
    @Delete Completable deleteMovie(Movie movie);
}
