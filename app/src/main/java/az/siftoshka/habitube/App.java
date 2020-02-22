package az.siftoshka.habitube;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;

import az.siftoshka.habitube.di.modules.AppModule;
import az.siftoshka.habitube.di.modules.RepositoryModule;
import az.siftoshka.habitube.di.modules.ServerModule;
import toothpick.Scope;
import toothpick.Toothpick;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initDarkMode();
        initToothPick();
        initSorting();
    }

    private void initToothPick() {
        final Scope scope = Toothpick.openScope(Constants.DI.APP_SCOPE);
        scope.installModules(new AppModule(this));
        scope.installModules(new ServerModule());
        scope.installModules(new RepositoryModule());
    }

    private void initDarkMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            SharedPreferences prefs = getSharedPreferences("Dark-Mode", MODE_PRIVATE);
            int id = prefs.getInt("Dark", 0);
            switch (id) {
                case 101:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case 100:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
            }
        }
    }

    private void initSorting() {
        SharedPreferences prefs = getSharedPreferences("Radio-Sort", MODE_PRIVATE);
        int id = prefs.getInt("Radio", 0);
        if (id == 0) {
            SharedPreferences.Editor editor = getSharedPreferences("Radio-Sort", MODE_PRIVATE).edit();
            editor.putInt("Radio", 200);
            editor.apply();
        }
    }
}
