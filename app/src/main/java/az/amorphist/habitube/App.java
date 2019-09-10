package az.amorphist.habitube;

import android.app.Application;

import az.amorphist.habitube.di.modules.AppModule;
import az.amorphist.habitube.di.modules.RepositoryModule;
import az.amorphist.habitube.di.modules.ServerModule;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.habitube.Constants.DI.APP_SCOPE;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initToothPick();
    }

    private void initToothPick() {
        final Scope scope = Toothpick.openScope(APP_SCOPE);
        scope.installModules(new AppModule(this));
        scope.installModules(new ServerModule());
        scope.installModules(new RepositoryModule());
    }
}
