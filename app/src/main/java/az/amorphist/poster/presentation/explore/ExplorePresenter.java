package az.amorphist.poster.presentation.explore;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.model.interactors.RemoteExploreInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ExplorePresenter extends MvpPresenter<ExploreView> {

    private final Router router;
    private final RemoteExploreInteractor remoteExploreInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public ExplorePresenter(Router router, RemoteExploreInteractor remoteExploreInteractor) {
        this.router = router;
        this.remoteExploreInteractor = remoteExploreInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        addUpcomingMovies();
        addMovies();
        addShows();
    }

    private void addUpcomingMovies() {
        compositeDisposable.add(remoteExploreInteractor.getUpcomingMovies()
        .subscribe(movieResponse -> getViewState().showUpcomingMovieList(movieResponse.getResults()),
                throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addMovies() {
        compositeDisposable.add(remoteExploreInteractor.getMovies()
                .subscribe(movieResponse -> getViewState().showMovieList(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addShows() {
        compositeDisposable.add(remoteExploreInteractor.getTVShows()
                .subscribe(movieResponse -> getViewState().showTVShowList(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void goToDetailedMovieScreen(Integer id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
    }

    public void goToDetailedShowScreen(Integer id) {
        router.navigateTo(new Screens.PostShowScreen(id));
    }

    public void goToDetailedUpcomingScreen(Integer id) {
        router.navigateTo(new Screens.PostUpcomingScreen(id));
    }

    public void goToSearchScreen() {
        router.navigateTo(new Screens.SearchScreen());
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
