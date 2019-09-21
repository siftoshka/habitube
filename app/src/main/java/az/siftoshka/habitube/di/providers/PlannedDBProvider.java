package az.siftoshka.habitube.di.providers;

import android.content.Context;

import androidx.room.Room;

import javax.inject.Inject;
import javax.inject.Provider;

import az.siftoshka.habitube.model.data.PlannedRoomRepository;

import static az.siftoshka.habitube.Constants.DB.PLANNED;

public class PlannedDBProvider implements Provider<PlannedRoomRepository> {

    private final Context context;

    @Inject
    public PlannedDBProvider(Context context) {
        this.context = context;
    }

    @Override
    public PlannedRoomRepository get() {
        return Room.databaseBuilder(context, PlannedRoomRepository.class, PLANNED).allowMainThreadQueries().build();
    }
}
