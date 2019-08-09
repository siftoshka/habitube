package az.amorphist.poster.presentation.search;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.model.interactors.RemoteExploreInteractor;
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

    public void searchMedia(String queryName) {
        compositeDisposable.add(remoteExploreInteractor.getSearchResults(queryName)
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(movieResponse -> getViewState().showSearchedMediaList(movieResponse.getResults()),
                throwable -> getViewState().unsuccessfulQueryError()));
    }

    public void goToDetailedScreen(Integer id, int mediaType) {
        router.navigateTo(new Screens.SearchdItemScreen(id, mediaType));
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
