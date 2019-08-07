package az.amorphist.poster.di.providers;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Provider;

import az.amorphist.poster.model.data.MovieDAO;
import az.amorphist.poster.model.repository.WatchedRoomRepository;

public class WatchedDBProvider implements Provider<MovieDAO> {

    private final Context context;

    @Inject
    public WatchedDBProvider(Context context) {
        this.context = context;
    }

    @Override
    public MovieDAO get() {
        return WatchedRoomRepository.getInstance(context).movieDAO();
    }
}
