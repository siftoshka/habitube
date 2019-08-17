package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.providers.WatchedDBProvider;
import az.amorphist.poster.model.data.WatchedRoomRepository;
import az.amorphist.poster.model.repository.WatchedRepository;
import toothpick.config.Module;

public class RepositoryModule extends Module {

    public RepositoryModule() {
        bind(WatchedRoomRepository.class).toProvider(WatchedDBProvider.class).providesSingletonInScope();
        bind(WatchedRepository.class).singletonInScope();
    }
}
