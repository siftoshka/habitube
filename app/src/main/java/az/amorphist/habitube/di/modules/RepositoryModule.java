package az.amorphist.habitube.di.modules;

import az.amorphist.habitube.di.providers.WatchedDBProvider;
import az.amorphist.habitube.model.data.WatchedRoomRepository;
import az.amorphist.habitube.model.repository.WatchedRepository;
import toothpick.config.Module;

public class RepositoryModule extends Module {

    public RepositoryModule() {
        bind(WatchedRoomRepository.class).toProvider(WatchedDBProvider.class).providesSingletonInScope();
        bind(WatchedRepository.class).singletonInScope();
    }
}
