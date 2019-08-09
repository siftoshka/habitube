package az.amorphist.poster.presentation.library;

import javax.inject.Inject;

import az.amorphist.poster.model.interactors.WatchedMoviesInteractor;
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
        compositeDisposable.add(watchedMoviesInteractor.getMovies()
        .subscribe((movies, throwable) -> getViewState().showWatchedMovies(movies)));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
