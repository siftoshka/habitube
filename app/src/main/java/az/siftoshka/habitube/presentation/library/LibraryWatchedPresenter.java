package az.siftoshka.habitube.presentation.library;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.WatchedMoviesInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryWatchedPresenter extends MvpPresenter<LibraryWatchedView> {

    private final Router router;
    private final WatchedMoviesInteractor watchedMoviesInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryWatchedPresenter(Router router, WatchedMoviesInteractor watchedMoviesInteractor) {
        this.router = router;
        this.watchedMoviesInteractor = watchedMoviesInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
    }

    public void getMovies() {
        compositeDisposable.add(watchedMoviesInteractor.getAllMovies()
        .subscribe((movies, throwable) -> getViewState().showWatchedMovies(movies)));
    }

    public void getShows() {
        compositeDisposable.add(watchedMoviesInteractor.getAllShows()
                .subscribe((shows, throwable) -> getViewState().showWatchedShows(shows)));
    }


    public void removeFromLocal(Movie movie) {
        watchedMoviesInteractor.deleteMovie(movie);
    }

    public void removeFromLocal(Show show) {
        watchedMoviesInteractor.deleteShow(show);
    }

    public void goToDetailedMovieScreen(int postId) {
        router.navigateTo(new Screens.PostMovieScreen(postId));
    }

    public void goToDetailedShowScreen(int postId) {
        router.navigateTo(new Screens.PostShowScreen(postId));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
