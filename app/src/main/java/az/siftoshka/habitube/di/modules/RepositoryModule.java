package az.siftoshka.habitube.di.modules;

import az.siftoshka.habitube.di.providers.PlannedDBProvider;
import az.siftoshka.habitube.di.providers.WatchedDBProvider;
import az.siftoshka.habitube.model.data.PlannedRoomRepository;
import az.siftoshka.habitube.model.data.WatchedRoomRepository;
import az.siftoshka.habitube.model.repository.PlannedRepository;
import az.siftoshka.habitube.model.repository.WatchedRepository;
import toothpick.config.Module;

public class RepositoryModule extends Module {

    public RepositoryModule() {
        bind(WatchedRoomRepository.class).toProvider(WatchedDBProvider.class).providesSingletonInScope();
        bind(PlannedRoomRepository.class).toProvider(PlannedDBProvider.class).providesSingletonInScope();
        bind(PlannedRepository.class).singletonInScope();
        bind(WatchedRepository.class).singletonInScope();
    }
}
