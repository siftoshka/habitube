package az.siftoshka.habitube.presentation.search;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.MediaType;
import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.model.interactor.RemoteExploreInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private final Router router;
    private final RemoteExploreInteractor remoteExploreInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public SearchPresenter(Router router, RemoteExploreInteractor remoteExploreInteractor) {
        this.router = router;
        this.remoteExploreInteractor = remoteExploreInteractor;
    }

    public void multiSearch(String queryName, String language, boolean isAdult) {
        compositeDisposable.add(remoteExploreInteractor.getSearchResults(queryName, language, isAdult)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(movieResponse -> getViewState().showSearchedMediaList(movieResponse.getResults()),
                throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void movieSearch(String queryName, String language, boolean isAdult) {
        compositeDisposable.add(remoteExploreInteractor.getMovieSearchResults(queryName, language, isAdult)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(movieResponse -> {getViewState().showSearchedMediaList(movieResponse.getResults()); addMediaType(movieResponse, "movie");},
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void showSearch(String queryName, String language, boolean isAdult) {
        compositeDisposable.add(remoteExploreInteractor.getShowSearchResults(queryName, language, isAdult)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(movieResponse -> {getViewState().showSearchedMediaList(movieResponse.getResults()); addMediaType(movieResponse, "tv");},
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void personSearch(String queryName, String language, boolean isAdult) {
        compositeDisposable.add(remoteExploreInteractor.getPersonSearchResults(queryName, language, isAdult)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(movieResponse -> {getViewState().showSearchedMediaList(movieResponse.getResults()); addMediaType(movieResponse, "person");},
                        throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addMediaType(MovieResponse movieResponse, String mediaType) {
        for(int i = 0; i < movieResponse.getResults().size(); i++) {
            movieResponse.getResults().get(i).setMediaType(mediaType);
        }
    }

    public void goToDetailedScreen(Integer id, int mediaType) {
        router.navigateTo(new Screens.SearchItemScreen(id, mediaType));
    }

    public void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
