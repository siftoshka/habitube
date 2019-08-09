package az.amorphist.poster.presentation.post;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowPosition;
import az.amorphist.poster.di.qualifiers.UpcomingMoviePosition;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.model.interactors.RemotePostInteractor;
import az.amorphist.poster.model.interactors.WatchedMoviesInteractor;
import io.reactivex.disposables.CompositeDisposable;
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
        if (upcomingPosition != 0) { getMovie(upcomingPosition); getSimilarMovies(upcomingPosition); }
        else if (postPosition != 0) { getMovie(postPosition); getSimilarMovies(postPosition);}
        else if (showPosition != 0) { getTVShow(showPosition); getSimilarTVShows(showPosition);}

        switch (mediaType) {
            case 1: getMovie(postId); getSimilarMovies(postId); break;
            case 2: getTVShow(postId); getSimilarTVShows(postId); break;
            case 3: getMovieStar(); break;
        }
    }

    private void getMovie(int id) {
        compositeDisposable.add(remotePostInteractor.getMovie(id)
        .doOnSubscribe(disposable -> getViewState().showProgress(true))
        .doAfterTerminate(() -> getViewState().showProgress(false))
        .doAfterSuccess(movie -> getViewState().showMovieScreen())
        .subscribe(movie -> getViewState().getMovie(
                movie.isAdult(),
                movie.getPosterPath(),
                movie.getBackdropPath(),
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getRuntime(),
                movie.getVoteAverage(),
                movie.getVoteCount(),
                movie.getMovieGenres(),
                movie.getImdbId(),
                movie.getOverview()),
                throwable -> getViewState().showErrorScreen()));
    }

    private void getTVShow(int id) {
        compositeDisposable.add(remotePostInteractor.getTVShow(id)
        .doOnSubscribe(disposable -> getViewState().showProgress(true))
        .doAfterTerminate(() -> getViewState().showProgress(false))
        .doAfterSuccess(show -> getViewState().showTVShowScreen())
        .subscribe(show -> getViewState().getShow(
                show.getPosterPath(),
                show.getBackdropPath(),
                show.getName(),
                show.getFirstAirDate(),
                show.getVoteAverage(),
                show.getVoteCount(),
                show.getShowGenres(),
                show.getOverview(),
                show.getSeasons()),
                throwable -> getViewState().showErrorScreen()));
    }

    private void getMovieStar() {
        compositeDisposable.add(remotePostInteractor.getStar(postId)
        .doOnSubscribe(disposable -> getViewState().showProgress(true))
        .doAfterTerminate(() -> getViewState().showProgress(false))
        .doAfterSuccess(person -> getViewState().showPersonScreen())
        .subscribe(person -> getViewState().getPerson(
                person.getProfilePath(),
                person.getName(),
                person.getBirthday(),
                person.getPlaceOfBirth(),
                person.getPopularity(),
                person.getBiography()),
                throwable -> getViewState().showErrorScreen()));
    }

    private void getSimilarMovies(int id) {
        compositeDisposable.add(remotePostInteractor.getSimilarMovies(id)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void getSimilarTVShows(int id) {
        compositeDisposable.add(remotePostInteractor.getSimilarTVShows(id)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    public void addMovieAsWatched(Movie movie) {
        watchedMoviesInteractor.addMovie(movie);
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
