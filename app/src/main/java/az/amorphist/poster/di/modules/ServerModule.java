package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.server.MovieDBApi;
import toothpick.config.Module;

public class ServerModule extends Module {

    public ServerModule() {
        bind(MovieDBApi.class).toProvider(ApiProvider.class).providesSingletonInScope();
    }
}
