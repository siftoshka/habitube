package az.amorphist.poster;

import android.app.Application;

import az.amorphist.poster.di.modules.AppModule;
import az.amorphist.poster.di.modules.ServerModule;
import toothpick.Scope;
import toothpick.Toothpick;

public class App extends Application {

    public static final String apiUrl = "https://api.themoviedb.org/3/";
    public static final String apiKey = "2de4fcda741c028964fe7c0faca007bc";

    @Override
    public void onCreate() {
        super.onCreate();

        initToothPick();
    }

    private void initToothPick() {
        final Scope scope = Toothpick.openScope("APP_SCOPE");
        scope.installModules(new AppModule(this));
        scope.installModules(new ServerModule());
    }
}
