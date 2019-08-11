package az.amorphist.poster.model.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;

@Database(entities = {Movie.class, MovieGenre.class}, exportSchema = false, version = 1)
public abstract class WatchedRoomRepository extends RoomDatabase {

    public abstract MovieDAO movieDAO();
}
