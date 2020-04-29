package az.siftoshka.habitube.presentation.explore;

import android.content.Context;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.model.interactor.RemoteExploreInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ExplorePresenter extends MvpPresenter<ExploreView> {

    private final Router router;
    private final Context context;
    private final RemoteExploreInteractor remoteExploreInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public ExplorePresenter(Router router, Context context,
                            RemoteExploreInteractor remoteExploreInteractor) {
        this.router = router;
        this.context = context;
        this.remoteExploreInteractor = remoteExploreInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        addContent();
    }

    public void addContent() {
        addUpcomingMovies(context.getResources().getString(R.string.language));
        addMovies(context.getResources().getString(R.string.language));
        addShows(context.getResources().getString(R.string.language));
        addAirToday(context.getResources().getString(R.string.language));
    }

    private void addUpcomingMovies(String language) {
        compositeDisposable.add(remoteExploreInteractor.getUpcomingMovies(1, language)
        .subscribe(movieResponse -> getViewState().showUpcomingMovieList(movieResponse.getResults()),
                throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addMoreUpcoming(int page) {
        compositeDisposable.add(remoteExploreInteractor.getUpcomingMovies(page, context.getResources().getString(R.string.language))
                .subscribe(movieResponse -> getViewState().showMoreUpcoming(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addMovies(String language) {
        compositeDisposable.add(remoteExploreInteractor.getMovies(1, language)
                .subscribe(movieResponse -> getViewState().showMovieList(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addMoreMovies(int page) {
        compositeDisposable.add(remoteExploreInteractor.getMovies(page, context.getResources().getString(R.string.language))
                .subscribe(movieResponse -> getViewState().showMoreTrending(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addShows(String language) {
        compositeDisposable.add(remoteExploreInteractor.getTVShows(1, language)
                .subscribe(movieResponse -> getViewState().showTVShowList(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addMoreShows(int page) {
        compositeDisposable.add(remoteExploreInteractor.getTVShows(page, context.getResources().getString(R.string.language))
                .subscribe(movieResponse -> getViewState().showMoreTrendingShows(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addAirToday(String language) {
        compositeDisposable.add(remoteExploreInteractor.getAirTodayShows(1, language)
                .subscribe(movieResponse -> getViewState().showAirTodayShows(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addMoreAirToday(int page) {
        compositeDisposable.add(remoteExploreInteractor.getAirTodayShows(page, context.getResources().getString(R.string.language))
                .subscribe(movieResponse -> getViewState().showMoreAirToday(movieResponse.getResults()),
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
