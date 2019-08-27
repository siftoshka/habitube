package az.amorphist.poster.presentation.movie;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.UpcomingMoviePosition;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MovieGenre;
import az.amorphist.poster.model.interactor.RemotePostInteractor;
import az.amorphist.poster.model.interactor.WatchedMoviesInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoviePresenter extends MvpPresenter<MovieView> {

    private final Router router;
    private final WatchedMoviesInteractor watchedMoviesInteractor;
    private final Integer upcomingPosition, postPosition, postId, mediaType;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MoviePresenter(Router router, RemotePostInteractor remotePostInteractor,
                          WatchedMoviesInteractor watchedMoviesInteractor,
                          @UpcomingMoviePosition Integer upcomingPosition,
                          @MoviePosition Integer postPosition,
                          @PostId Integer postId,
                          @MediaType Integer mediaType) {
        this.router = router;
        this.remotePostInteractor = remotePostInteractor;
        this.watchedMoviesInteractor = watchedMoviesInteractor;
        this.upcomingPosition = upcomingPosition;
        this.postPosition = postPosition;
        this.postId = postId;
        this.mediaType = mediaType;
    }

    @Override
    protected void onFirstViewAttach() {
        if (upcomingPosition != 0) {
            getMovie(upcomingPosition);
            getSimilarMovies(upcomingPosition);
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(upcomingPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
        } else if (postPosition != 0) {
            getMovie(postPosition);
            getSimilarMovies(postPosition);
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(postPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
        }

        if (mediaType == 1) {
            getMovie(postId);
            getSimilarMovies(postId);
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
        }
    }

    private void getMovie(int id) {
        compositeDisposable.add(remotePostInteractor.getMovie(id)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(movie -> getViewState().showMovieScreen())
                .subscribe(movie -> getViewState().showMovie(movie),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getSimilarMovies(int id) {
        compositeDisposable.add(remotePostInteractor.getSimilarMovies(id)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    public void addMovieAsWatched(Movie movie, List<MovieGenre> movieGenres) {
        watchedMoviesInteractor.addMovie(movie, movieGenres);
        getViewState().setSaveButtonEnabled(true);
    }

    public void deleteMovieFromWatched(Movie movie) {
        watchedMoviesInteractor.deleteMovie(movie);
        getViewState().setSaveButtonEnabled(false);
    }

    public void goToDetailedMovieScreen(Integer id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
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
