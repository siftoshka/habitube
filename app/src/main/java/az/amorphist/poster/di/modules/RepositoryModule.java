package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.providers.WatchedDBProvider;
import az.amorphist.poster.model.WatchedRepository;
import az.amorphist.poster.model.data.MovieDAO;
import toothpick.config.Module;

public class RepositoryModule extends Module {

    public RepositoryModule() {
        bind(MovieDAO.class).toProvider(WatchedDBProvider.class).providesSingletonInScope();
        bind(WatchedRepository.class).singletonInScope();
    }
}
