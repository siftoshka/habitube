package az.amorphist.poster.presentation.explore;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.server.MovieDBApi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ExplorePresenter extends MvpPresenter<ExploreView> {

    private final Router router;
    private final MovieDBApi movieDBApi;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public ExplorePresenter(Router router, MovieDBApi movieDBApi) {
        this.router = router;
        this.movieDBApi = movieDBApi;
    }

    @Override
    protected void onFirstViewAttach() {
        addUpcomingMovies();
        addMovies();
        addShows();
    }

    private void addUpcomingMovies() {
        compositeDisposable.add(movieDBApi.getUpcomingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moviePager -> getViewState().showUpcomingMovieList(moviePager.getResults()),
                            throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addMovies() {
        compositeDisposable.add(movieDBApi.getTrendingMovies()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(moviePager -> getViewState().showMovieList(moviePager.getResults()),
                    throwable -> getViewState().unsuccessfulQueryError()));
    }

    private void addShows() {
        compositeDisposable.add(movieDBApi.getTrendingTVShows()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(moviePager -> getViewState().showTVShowList(moviePager.getResults()),
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
