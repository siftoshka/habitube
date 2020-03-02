package az.siftoshka.habitube.presentation.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.WatchedInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryWatchedPresenter extends MvpPresenter<LibraryWatchedView> {

    private final Router router;
    private final Context context;

    private final WatchedInteractor watchedInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryWatchedPresenter(Router router, Context context, WatchedInteractor watchedInteractor) {
        this.router = router;
        this.context = context;
        this.watchedInteractor = watchedInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
    }

    public void getMovies() {
        compositeDisposable.add(watchedInteractor.getAllMovies()
        .subscribe((movies, throwable) -> getViewState().showWatchedMovies(movies)));
    }

    public void getShows() {
        compositeDisposable.add(watchedInteractor.getAllShows()
                .subscribe((shows, throwable) -> getViewState().showWatchedShows(shows)));
    }

    public void removeFromLocal(Movie movie, int position) {
        watchedInteractor.deleteMovie(movie);
        getViewState().screenWatcher(position);
    }

    public void removeFromLocal(Show show, int position) {
        watchedInteractor.deleteShow(show);
        getViewState().screenWatcher(position);
    }

    public void goToDetailedMovieScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostMovieScreen(postId));
        } else {
            compositeDisposable.add(watchedInteractor.getMovie(postId)
                    .subscribe((movie, throwable) -> getViewState().showOfflineCard(movie)));
        }
    }

    public void goToDetailedShowScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostShowScreen(postId));
        } else {
            compositeDisposable.add(watchedInteractor.getShow(postId)
                    .subscribe((show, throwable) -> getViewState().showOfflineCard(show)));
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm != null ? cm.getAllNetworkInfo() : new NetworkInfo[0];
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
