package az.siftoshka.habitube;

import android.app.Application;

import az.siftoshka.habitube.di.modules.AppModule;
import az.siftoshka.habitube.di.modules.RepositoryModule;
import az.siftoshka.habitube.di.modules.ServerModule;
import toothpick.Scope;
import toothpick.Toothpick;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initToothPick();
    }

    private void initToothPick() {
        final Scope scope = Toothpick.openScope(Constants.DI.APP_SCOPE);
        scope.installModules(new AppModule(this));
        scope.installModules(new ServerModule());
        scope.installModules(new RepositoryModule());
    }
}
