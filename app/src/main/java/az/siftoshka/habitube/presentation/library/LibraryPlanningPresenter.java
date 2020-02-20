package az.siftoshka.habitube.presentation.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.PlannedInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    private final Router router;
    private final Context context;
    private final PlannedInteractor plannedInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryPlanningPresenter(Router router, Context context,
                                    PlannedInteractor plannedInteractor) {
        this.router = router;
        this.context = context;
        this.plannedInteractor = plannedInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
    }

    public void getMovies() {
        compositeDisposable.add(plannedInteractor.getAllMovies()
                .subscribe((movies, throwable) -> getViewState().showPlannedMovies(movies)));
    }

    public void getShows() {
        compositeDisposable.add(plannedInteractor.getAllShows()
                .subscribe((shows, throwable) -> getViewState().showPlannedShows(shows)));
    }

    public void removeFromLocal(Movie movie) {
        plannedInteractor.deleteMovie(movie);
    }

    public void removeFromLocal(Show show) {
        plannedInteractor.deleteShow(show);
    }

    public void goToDetailedMovieScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostMovieScreen(postId));
        } else {
            compositeDisposable.add(plannedInteractor.getMovie(postId)
                    .subscribe((movie, throwable) -> getViewState().showOfflineCard(movie)));
        }
    }

    public void goToDetailedShowScreen(int postId) {
        if (haveNetworkConnection()) {
            router.navigateTo(new Screens.PostShowScreen(postId));
        } else {
            compositeDisposable.add(plannedInteractor.getShow(postId)
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
