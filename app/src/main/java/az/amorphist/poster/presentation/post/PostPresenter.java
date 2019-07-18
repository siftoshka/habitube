package az.amorphist.poster.presentation.post;

import javax.inject.Inject;

import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowId;
import az.amorphist.poster.di.qualifiers.UpcomingId;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movie.MoviePager;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private ApiProvider provider;
    private final Integer upcomingId, postId, showId;

    @Inject
    public PostPresenter(Router router, ApiProvider provider, @UpcomingId Integer upcomingId, @PostId Integer postId, @ShowId Integer showId) {
        this.router = router;
        this.provider = provider;
        this.upcomingId = upcomingId;
        this.postId = postId;
        this.showId = showId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (upcomingId != 0) {
            getUpcomingMovie();
        } else if (postId != 0) {
            getMovie();
        } else {
            getTVShow();
        }
    }

    private void getUpcomingMovie() {
        provider.get().getUpcomingMovies(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePager>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(MoviePager moviePager) {
                        getViewState().showProgress(false);
                        Movie upcomingMovie = moviePager.getResults().get(upcomingId - 1);
                        getViewState().getMovie(
                                upcomingMovie.getMovieImage(),
                                upcomingMovie.getMovieBackgroundImage(),
                                upcomingMovie.getMovieTitle(),
                                upcomingMovie.getMovieDate(),
                                upcomingMovie.getMovieRate(),
                                upcomingMovie.getMovieViews(),
                                upcomingMovie.getMovieBody()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showErrorScreen();
                    }
                });
    }

    private void getMovie() {
        provider.get().getTrendingMovies(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePager>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(MoviePager moviePager) {
                        getViewState().showProgress(false);
                        Movie movie = moviePager.getResults().get(postId - 1);
                        getViewState().getMovie(
                                movie.getMovieImage(),
                                movie.getMovieBackgroundImage(),
                                movie.getMovieTitle(),
                                movie.getMovieDate(),
                                movie.getMovieRate(),
                                movie.getMovieViews(),
                                movie.getMovieBody()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showErrorScreen();
                    }
                });
    }

    private void getTVShow() {
        provider.get().getTrendingTVShows(API_KEY)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<MoviePager>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(MoviePager moviePager) {
                        getViewState().showProgress(false);
                        Movie show = moviePager.getResults().get(showId - 1);
                        getViewState().getMovie(
                                show.getMovieImage(),
                                show.getMovieBackgroundImage(),
                                show.getShowTitle(),
                                show.getShowDate(),
                                show.getMovieRate(),
                                show.getMovieViews(),
                                show.getMovieBody()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showErrorScreen();
                    }
                });
    }

    public void goBack() {
        router.exit();
    }

}
