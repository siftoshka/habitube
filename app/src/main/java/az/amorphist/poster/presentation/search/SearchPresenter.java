package az.amorphist.poster.presentation.search;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.movielite.MoviePager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;
import static az.amorphist.poster.App.APP_LANG;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private final Router router;
    private ApiProvider provider;

    @Inject
    public SearchPresenter(Router router, ApiProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    public void searchMedia(String queryName) {
        provider.get().getSearchResults(API_KEY, APP_LANG, queryName, 1, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePager>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MoviePager moviePager) {
                        List<MovieLite> results = moviePager.getResults();
                        getViewState().showSearchedMediaList(results);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void goToDetailedScreen(Integer id, int mediaType) {
        router.navigateTo(new Screens.SearchdItemScreen(id, mediaType));
    }

    public void goBack() {
        router.exit();
    }
}
