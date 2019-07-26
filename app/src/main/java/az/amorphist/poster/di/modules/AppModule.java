package az.amorphist.poster.di.modules;

import android.content.Context;

import az.amorphist.poster.utils.navigation.LocalRouter;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import toothpick.config.Module;

public class AppModule extends Module {

    public AppModule(Context context) {
        bind(Context.class).toInstance(context);

        final Cicerone<Router> cicerone = Cicerone.create(new Router());
        bind(Router.class).toInstance(cicerone.getRouter());
        bind(NavigatorHolder.class).toInstance(cicerone.getNavigatorHolder());

        bind(LocalRouter.class).toInstance(new LocalRouter());
    }
}
