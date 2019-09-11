package az.siftoshka.habitube.di.modules;

import az.siftoshka.habitube.di.providers.ApiProvider;
import az.siftoshka.habitube.model.server.MovieDBApi;
import toothpick.config.Module;

public class ServerModule extends Module {

    public ServerModule() {
        bind(MovieDBApi.class).toProvider(ApiProvider.class).providesSingletonInScope();
    }
}
