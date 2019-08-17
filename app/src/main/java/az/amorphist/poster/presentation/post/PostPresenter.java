package az.amorphist.poster.presentation.post;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowPosition;
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
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private final WatchedMoviesInteractor watchedMoviesInteractor;
    private final Integer upcomingPosition, postPosition, showPosition, postId, mediaType;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PostPresenter(Router router, RemotePostInteractor remotePostInteractor,
                         WatchedMoviesInteractor watchedMoviesInteractor,
                         @UpcomingMoviePosition Integer upcomingPosition,
                         @MoviePosition Integer postPosition,
                         @ShowPosition Integer showPosition,
                         @PostId Integer postId,
                         @MediaType Integer mediaType) {
        this.router = router;
        this.remotePostInteractor = remotePostInteractor;
        this.watchedMoviesInteractor = watchedMoviesInteractor;
        this.upcomingPosition = upcomingPosition;
        this.postPosition = postPosition;
        this.showPosition = showPosition;
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
        } else if (showPosition != 0) {
            getTVShow(showPosition);
            getSimilarTVShows(showPosition);
        }

        switch (mediaType) {
            case 1:
                getMovie(postId);
                getSimilarMovies(postId);
                compositeDisposable.add(watchedMoviesInteractor.isMovieExists(postId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getViewState()::setSaveButtonEnabled));
                break;
            case 2:
                getTVShow(postId);
                getSimilarTVShows(postId);
                break;
            case 3:
                getMovieStar();
                break;
        }
    }

    private void getMovie(int id) {
        compositeDisposable.add(remotePostInteractor.getMovie(id)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(movie -> getViewState().showMovieScreen())
                .subscribe(movie -> getViewState().getMovie(movie),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getTVShow(int id) {
        compositeDisposable.add(remotePostInteractor.getTVShow(id)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(show -> getViewState().showTVShowScreen())
                .subscribe(show -> getViewState().getShow(show),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getMovieStar() {
        compositeDisposable.add(remotePostInteractor.getStar(postId)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(person -> getViewState().showPersonScreen())
                .subscribe(person -> getViewState().getPerson( person),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getSimilarMovies(int id) {
        compositeDisposable.add(remotePostInteractor.getSimilarMovies(id)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void getSimilarTVShows(int id) {
        compositeDisposable.add(remotePostInteractor.getSimilarTVShows(id)
                .subscribe(movieResponses -> getViewState().showSimilarTVShowList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    public void addMovieAsWatched(Movie movie, List<MovieGenre> movieGenres) {
        watchedMoviesInteractor.addMovie(movie, movieGenres);
        getViewState().setSaveButtonEnabled(true);
    }

    public void goToDetailedMovieScreen(Integer id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
    }

    public void goToDetailedShowScreen(Integer id) {
        router.navigateTo(new Screens.PostShowScreen(id));
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
