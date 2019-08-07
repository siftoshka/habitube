package az.amorphist.poster.model.repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.data.MovieDAO;

@Database(entities = Movie.class, exportSchema = false, version = 1)
public abstract class WatchedRoomRepository extends RoomDatabase {

    private static WatchedRoomRepository INSTANCE;

    public static WatchedRoomRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), WatchedRoomRepository.class, "movies")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public abstract MovieDAO movieDAO();
}
