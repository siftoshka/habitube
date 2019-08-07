package az.amorphist.poster.presentation.library;

import javax.inject.Inject;

import az.amorphist.poster.model.interactors.WatchedMoviesInteractor;
import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class LibraryWatchedPresenter extends MvpPresenter<LibraryWatchedView> {

    private final WatchedMoviesInteractor watchedMoviesInteractor;

    @Inject
    public LibraryWatchedPresenter(WatchedMoviesInteractor watchedMoviesInteractor) {
        this.watchedMoviesInteractor = watchedMoviesInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
    }

    public void getMovies() {
        watchedMoviesInteractor.getMovies();
    }
}
