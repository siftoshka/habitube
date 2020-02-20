package az.siftoshka.habitube.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface MovieDAO {
    @Insert Completable addMovie(Movie movie);
    @Transaction @Query("SELECT * FROM movies") Single<List<Movie>> getMovies();
    @Transaction @Query("SELECT count(*) FROM movies WHERE id = :movieId") int getMovieCount(int movieId);
    @Transaction @Query("SELECT * FROM movies WHERE id = :movieId") Single<Movie> getMovieById(int movieId);
    @Delete Completable deleteMovie(Movie movie);
}
