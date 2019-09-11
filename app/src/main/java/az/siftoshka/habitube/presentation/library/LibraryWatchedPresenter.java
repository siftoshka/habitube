package az.siftoshka.habitube.presentation.library;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.model.interactor.WatchedMoviesInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class LibraryWatchedPresenter extends MvpPresenter<LibraryWatchedView> {

    private final WatchedMoviesInteractor watchedMoviesInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryWatchedPresenter(WatchedMoviesInteractor watchedMoviesInteractor) {
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

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    public void removeFromLocal(Movie movie) {
        watchedMoviesInteractor.deleteMovie(movie);
    }
}
