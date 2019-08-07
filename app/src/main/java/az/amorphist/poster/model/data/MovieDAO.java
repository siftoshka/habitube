package az.amorphist.poster.model.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import az.amorphist.poster.entities.movie.Movie;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface MovieDAO {

    @Insert Completable addMovie(Movie movie);
    @Query("SELECT * FROM movies") Single<List<Movie>> getMovies();
    @Query("SELECT * FROM movies WHERE id = :id") Movie getMovieById(int id);
    @Delete void deleteMovie(Movie movie);
}
