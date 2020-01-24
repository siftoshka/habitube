package az.siftoshka.habitube.model.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.utils.DateConverter;

@Database(entities = {Movie.class}, exportSchema = false, version = 1)
@TypeConverters(DateConverter.class)
public abstract class PlannedRoomRepository extends RoomDatabase {

    public abstract MovieDAO movieDAO();
}
