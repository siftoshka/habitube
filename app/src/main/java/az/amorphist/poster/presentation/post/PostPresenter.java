package az.amorphist.poster.presentation.post;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowPosition;
import az.amorphist.poster.di.qualifiers.UpcomingMoviePosition;
import az.amorphist.poster.server.MovieDBApi;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private final MovieDBApi movieDBApi;
    private final Integer upcomingPosition, postPosition, showPosition, postId, mediaType;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PostPresenter(Router router, MovieDBApi movieDBApi,
                         @UpcomingMoviePosition Integer upcomingPosition,
                         @MoviePosition Integer postPosition,
                         @ShowPosition Integer showPosition,
                         @PostId Integer postId,
                         @MediaType Integer mediaType) {
        this.router = router;
        this.movieDBApi = movieDBApi;
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
        compositeDisposable.add(movieDBApi.getMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(person -> getViewState().showMovieScreen())
                .subscribe(movie -> getViewState().getMovie(
                        movie.getPosterPath(),
                        movie.getBackdropPath(),
                        movie.getTitle(),
                        movie.getReleaseDate(),
                        movie.getVoteAverage(),
                        movie.getVoteCount(),
                        movie.getMovieGenres(),
                        movie.getOverview()),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getTVShow(int id) {
        compositeDisposable.add(movieDBApi.getTVShow(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(person -> getViewState().showTVShowScreen())
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
        compositeDisposable.add(movieDBApi.getMovieStar(postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        compositeDisposable.add(movieDBApi.getSimilarMovies(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(moviePager -> getViewState().showSimilarMovieList(moviePager.getResults()),
                Throwable::printStackTrace));
    }

    private void getSimilarTVShows(int id) {
        compositeDisposable.add(movieDBApi.getSimilarTVShow(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(moviePager -> getViewState().showSimilarTVShowList(moviePager.getResults()),
                Throwable::printStackTrace));
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
