package az.siftoshka.habitube.di.modules;

import az.siftoshka.habitube.di.providers.WatchedDBProvider;
import az.siftoshka.habitube.model.data.WatchedRoomRepository;
import az.siftoshka.habitube.model.repository.WatchedRepository;
import toothpick.config.Module;

public class RepositoryModule extends Module {

    public RepositoryModule() {
        bind(WatchedRoomRepository.class).toProvider(WatchedDBProvider.class).providesSingletonInScope();
        bind(WatchedRepository.class).singletonInScope();
    }
}
