package az.amorphist.poster;

import android.app.Application;

import az.amorphist.poster.di.modules.AppModule;
import az.amorphist.poster.di.modules.RepositoryModule;
import az.amorphist.poster.di.modules.ServerModule;
import toothpick.Scope;
import toothpick.Toothpick;

import static az.amorphist.poster.di.DI.APP_SCOPE;

public class App extends Application {

    public static final String IMDB_WEBSITE = "https://www.imdb.com/title/";
    public static final String DEV_CONTACT = "https://t.me/amorphist";
    public static final String API_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_URL = "https://image.tmdb.org/t/p/original";
    public static final String API_KEY = "2de4fcda741c028964fe7c0faca007bc";
    public static final String APP_LANG = "en-US";

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
