package az.siftoshka.habitube.presentation.settings;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.model.interactor.PlannedInteractor;
import az.siftoshka.habitube.model.interactor.WatchedInteractor;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private final Context context;
    private final PlannedInteractor plannedInteractor;
    private final WatchedInteractor watchedInteractor;


    @Inject
    public SettingsPresenter(Context context, PlannedInteractor plannedInteractor, WatchedInteractor watchedInteractor) {
        this.context = context;
        this.plannedInteractor = plannedInteractor;
        this.watchedInteractor = watchedInteractor;
    }

    @Override
    public void onFirstViewAttach() {
        super.onFirstViewAttach();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null) {
            getViewState().showUser(user);
        } else {
            getViewState().showGoogleSignIn();
        }
    }

    public void clearCache() {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void deleteMedia(String command) {
        switch (command) {
            case "M-P": plannedInteractor.deleteAllMovies();break;
            case "M-W": watchedInteractor.deleteAllMovies();break;
            case "S-P": plannedInteractor.deleteAllShows();break;
            case "S-W": watchedInteractor.deleteAllShows();break;
        }
    }
}
