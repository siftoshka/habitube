package az.amorphist.habitube.model.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import az.amorphist.habitube.entities.movie.Movie;
import az.amorphist.habitube.entities.movie.MovieGenre;
import az.amorphist.habitube.utils.DateConverter;

@Database(entities = {Movie.class, MovieGenre.class}, exportSchema = false, version = 1)
@TypeConverters(DateConverter.class)
public abstract class WatchedRoomRepository extends RoomDatabase {

    public abstract MovieDAO movieDAO();
}
