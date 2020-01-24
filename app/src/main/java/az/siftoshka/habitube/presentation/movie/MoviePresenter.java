package az.siftoshka.habitube.presentation.movie;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.MediaType;
import az.siftoshka.habitube.di.qualifiers.MoviePosition;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.di.qualifiers.UpcomingMoviePosition;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movie.MovieGenre;
import az.siftoshka.habitube.model.interactor.PlannedMoviesInteractor;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import az.siftoshka.habitube.model.interactor.WatchedMoviesInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MoviePresenter extends MvpPresenter<MovieView> {

    private final Router router;
    private final Context context;
    private final WatchedMoviesInteractor watchedMoviesInteractor;
    private final PlannedMoviesInteractor plannedMoviesInteractor;
    private final Integer upcomingPosition, postPosition, postId, mediaType;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public MoviePresenter(Router router, Context context, RemotePostInteractor remotePostInteractor,
                          WatchedMoviesInteractor watchedMoviesInteractor,
                          PlannedMoviesInteractor plannedMoviesInteractor,
                          @UpcomingMoviePosition Integer upcomingPosition,
                          @MoviePosition Integer postPosition,
                          @PostId Integer postId,
                          @MediaType Integer mediaType) {
        this.router = router;
        this.context = context;
        this.remotePostInteractor = remotePostInteractor;
        this.watchedMoviesInteractor = watchedMoviesInteractor;
        this.plannedMoviesInteractor = plannedMoviesInteractor;
        this.upcomingPosition = upcomingPosition;
        this.postPosition = postPosition;
        this.postId = postId;
        this.mediaType = mediaType;
    }

    @Override
    protected void onFirstViewAttach() {
        if (upcomingPosition != 0) {
            getMovie(upcomingPosition, context.getResources().getString(R.string.language));
            getSimilarMovies(upcomingPosition, context.getResources().getString(R.string.language));
            getVideos(upcomingPosition, context.getResources().getString(R.string.language));
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(upcomingPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedMoviesInteractor.isMovieExists(upcomingPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        } else if (postPosition != 0) {
            getMovie(postPosition, context.getResources().getString(R.string.language));
            getSimilarMovies(postPosition, context.getResources().getString(R.string.language));
            getVideos(postPosition, context.getResources().getString(R.string.language));
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(postPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedMoviesInteractor.isMovieExists(postPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }
        if (mediaType == 1) {
            getMovie(postId, context.getResources().getString(R.string.language));
            getSimilarMovies(postId, context.getResources().getString(R.string.language));
            getVideos(postId, context.getResources().getString(R.string.language));
            compositeDisposable.add(watchedMoviesInteractor.isMovieExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedMoviesInteractor.isMovieExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }
    }

    private void getMovie(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getMovie(id, language)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(movie -> getViewState().showMovieScreen())
                .subscribe(movie -> getViewState().showMovie(movie),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getSimilarMovies(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getSimilarMovies(id, language)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void getVideos(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getMovieVideos(id, language)
                .subscribe(videoResponse -> getViewState().showVideos(videoResponse.getResults()),
                        Throwable::printStackTrace));
    }

    public void addMovieAsWatched(Movie movie) {
        watchedMoviesInteractor.addMovie(movie);
        getViewState().setSaveButtonEnabled(true);
    }

    public void deleteMovieFromWatched(Movie movie) {
        watchedMoviesInteractor.deleteMovie(movie);
        getViewState().setSaveButtonEnabled(false);
    }

    public void addMovieAsPlanned(Movie movie) {
        plannedMoviesInteractor.addMovie(movie);
        getViewState().setPlanButtonEnabled(true);
    }

    public void deleteMovieFromPlanned(Movie movie) {
        plannedMoviesInteractor.deleteMovie(movie);
        getViewState().setPlanButtonEnabled(false);
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
