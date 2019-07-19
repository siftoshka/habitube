package az.amorphist.poster.presentation.post;

import javax.inject.Inject;

import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowPosition;
import az.amorphist.poster.di.qualifiers.UpcomingMoviePosition;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.person.Person;
import az.amorphist.poster.entities.show.Show;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private ApiProvider provider;
    private final Integer upcomingPosition, postPosition, showPosition, postId, mediaType;

    @Inject
    public PostPresenter(Router router, ApiProvider provider,
                         @UpcomingMoviePosition Integer upcomingPosition,
                         @MoviePosition Integer postPosition,
                         @ShowPosition Integer showPosition,
                         @PostId Integer postId,
                         @MediaType Integer mediaType) {
        this.router = router;
        this.provider = provider;
        this.upcomingPosition = upcomingPosition;
        this.postPosition = postPosition;
        this.showPosition = showPosition;
        this.postId = postId;
        this.mediaType = mediaType;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (upcomingPosition != 0) { getMovie(upcomingPosition); }
        else if (postPosition != 0) { getMovie(postPosition); }
        else if (showPosition != 0) { getTVShow(showPosition); }

        switch (mediaType) {
            case 1: getMovie(postId); break;
            case 2: getTVShow(postId); break;
            case 3: getMovieStar(); break;
        }
    }

    private void getMovie(int id) {
        provider.get().getMovie(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(Movie movie) {
                        getViewState().showProgress(false);
                        getViewState().getMovie(
                                movie.getPosterPath(),
                                movie.getBackdropPath(),
                                movie.getTitle(),
                                movie.getReleaseDate(),
                                movie.getVoteAverage(),
                                movie.getVoteCount(),
                                movie.getOverview()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showErrorScreen();
                    }
                });
    }

    private void getTVShow(int id) {
        provider.get().getTVShow(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Show>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(Show show) {
                        getViewState().showProgress(false);
                        getViewState().getMovie(
                                show.getPosterPath(),
                                show.getBackdropPath(),
                                show.getName(),
                                show.getFirstAirDate(),
                                show.getVoteAverage(),
                                show.getVoteCount(),
                                show.getOverview()
                        );
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().showErrorScreen();
                    }
                });
    }

    private void getMovieStar() {
        provider.get().getMovieStar(postId, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Person>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getViewState().showProgress(true);
                    }

                    @Override
                    public void onSuccess(Person person) {
                        getViewState().showProgress(false);
                        getViewState().getPerson(
                                person.getProfilePath(),
                                person.getProfilePath(),
                                person.getName(),
                                person.getBirthday(),
                                person.getPlaceOfBirth(),
                                person.getPopularity(),
                                person.getBiography()
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
