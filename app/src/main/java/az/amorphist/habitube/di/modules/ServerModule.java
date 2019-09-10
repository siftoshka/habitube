package az.amorphist.habitube.di.modules;

import az.amorphist.habitube.di.providers.ApiProvider;
import az.amorphist.habitube.model.server.MovieDBApi;
import toothpick.config.Module;

public class ServerModule extends Module {

    public ServerModule() {
        bind(MovieDBApi.class).toProvider(ApiProvider.class).providesSingletonInScope();
    }
}
