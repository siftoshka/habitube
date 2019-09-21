package az.siftoshka.habitube.presentation.library;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.model.interactor.PlannedMoviesInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    private final PlannedMoviesInteractor plannedMoviesInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryPlanningPresenter(PlannedMoviesInteractor plannedMoviesInteractor) {
        this.plannedMoviesInteractor = plannedMoviesInteractor;
    }

    @Override
    protected void onFirstViewAttach() {
        getMovies();
    }

    public void getMovies() {
        compositeDisposable.add(plannedMoviesInteractor.getAllMovies()
                .subscribe((movies, throwable) -> getViewState().showPlannedMovies(movies)));
    }

    public void removeFromLocal(Movie movie) {
        plannedMoviesInteractor.deleteMovie(movie);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
