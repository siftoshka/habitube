package az.amorphist.poster.presentation.search;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.server.MovieDBApi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.Constants.SYSTEM.APP_LANG;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private final Router router;
    private final MovieDBApi movieDBApi;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public SearchPresenter(Router router, MovieDBApi movieDBApi) {
        this.router = router;
        this.movieDBApi = movieDBApi;
    }

    public void searchMedia(String queryName) {
        compositeDisposable.add(movieDBApi.getSearchResults(APP_LANG, queryName, 1, false)
                .subscribeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviePager -> getViewState().showSearchedMediaList(moviePager.getResults()),
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
