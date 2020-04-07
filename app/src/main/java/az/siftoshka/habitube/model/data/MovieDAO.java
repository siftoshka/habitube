package az.siftoshka.habitube.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import az.siftoshka.habitube.entities.movie.Movie;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE) Completable addMovie(Movie movie);
    @Update Completable updateMovie(Movie movie);
    @Transaction @Query("SELECT * FROM movies") Single<List<Movie>> getMovies();
    @Transaction @Query("SELECT count(*) FROM movies WHERE id = :movieId") int getMovieCount(int movieId);
    @Transaction @Query("SELECT * FROM movies WHERE id = :movieId") Maybe<Movie> getMovieById(int movieId);
    @Delete Completable deleteMovie(Movie movie);
    @Transaction @Query("DELETE FROM movies") Completable deleteAllMovies();
}
