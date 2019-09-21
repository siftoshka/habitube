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

    private void addContent() {
        addUpcomingMovies(context.getResources().getString(R.string.language));
        addMovies(context.getResources().getString(R.string.language));
        addShows(context.getResources().getString(R.string.language));
    }

    public void addUpcomingMovies(String language) {
        compositeDisposable.add(remoteExploreInteractor.getUpcomingMovies(language)
        .subscribe(movieResponse -> getViewState().showUpcomingMovieList(movieResponse.getResults()),
                throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addMovies(String language) {
        compositeDisposable.add(remoteExploreInteractor.getMovies(language)
                .subscribe(movieResponse -> getViewState().showMovieList(movieResponse.getResults()),
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void addShows(String language) {
        compositeDisposable.add(remoteExploreInteractor.getTVShows(language)
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
