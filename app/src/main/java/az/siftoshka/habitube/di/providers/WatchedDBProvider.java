package az.siftoshka.habitube.di.providers;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Inject;
import javax.inject.Provider;

import az.siftoshka.habitube.model.data.WatchedRoomRepository;

import static az.siftoshka.habitube.Constants.DB.MOVIE_TABLE;

public class WatchedDBProvider implements Provider<WatchedRoomRepository> {

    private final Context context;

    @Inject
    public WatchedDBProvider(Context context) {
        this.context = context;
    }

    @Override
    public WatchedRoomRepository get() {
        return Room.databaseBuilder(context, WatchedRoomRepository.class, MOVIE_TABLE).allowMainThreadQueries().build();
    }
}