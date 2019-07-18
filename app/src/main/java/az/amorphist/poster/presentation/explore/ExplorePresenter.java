package az.amorphist.poster.presentation.explore;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.entities.movielite.MoviePagerLite;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;

@InjectViewState
public class ExplorePresenter extends MvpPresenter<ExploreView> {

    private final Router router;
    private ApiProvider provider;

    @Inject
    public ExplorePresenter(Router router, ApiProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        addUpcomingMovies();
        addMovies();
        addShows();
    }

    private void addUpcomingMovies() {
        provider.get().getUpcomingMoviesLite(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePagerLite>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MoviePagerLite moviePagerLite) {
                        List<MovieLite> upcomingMovies = moviePagerLite.getResults();
                        getViewState().getUpcomingMovieList(upcomingMovies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().unsuccessfulQueryError();
                    }
                });
    }

    private void addMovies() {
        provider.get().getTrendingMoviesLite(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePagerLite>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MoviePagerLite moviePagerLite) {
                        List<MovieLite> movies = moviePagerLite.getResults();
                        getViewState().getMovieList(movies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().unsuccessfulQueryError();
                    }
                });
    }


    private void addShows() {
        provider.get().getTrendingTVShowsLite(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePagerLite>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(MoviePagerLite moviePagerLite) {
                        List<MovieLite> tvShows = moviePagerLite.getResults();
                        getViewState().getTVShowList(tvShows);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().unsuccessfulQueryError();
                    }
                });
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
}
