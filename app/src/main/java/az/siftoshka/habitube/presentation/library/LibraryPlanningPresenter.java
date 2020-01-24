package az.siftoshka.habitube.presentation.library;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.PlannedMoviesInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class LibraryPlanningPresenter extends MvpPresenter<LibraryPlanningView> {

    private final Router router;
    private final PlannedMoviesInteractor plannedMoviesInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public LibraryPlanningPresenter(Router router, PlannedMoviesInteractor plannedMoviesInteractor) {
        this.router = router;
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

    public void getShows() {
        compositeDisposable.add(plannedMoviesInteractor.getAllShows()
                .subscribe((shows, throwable) -> getViewState().showPlannedShows(shows)));
    }

    public void removeFromLocal(Movie movie) {
        plannedMoviesInteractor.deleteMovie(movie);
    }

    public void removeFromLocal(Show show) {
        plannedMoviesInteractor.deleteShow(show);
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
