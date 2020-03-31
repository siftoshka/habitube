package az.siftoshka.habitube.presentation.movie;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.MediaType;
import az.siftoshka.habitube.di.qualifiers.MoviePosition;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.di.qualifiers.UpcomingMoviePosition;
import az.siftoshka.habitube.entities.credits.Cast;
import az.siftoshka.habitube.entities.credits.Credits;
import az.siftoshka.habitube.entities.credits.Crew;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.model.interactor.PlannedInteractor;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import az.siftoshka.habitube.model.interactor.WatchedInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class MoviePresenter extends MvpPresenter<MovieView> {

    private final Router router;
    private final Context context;
    private final WatchedInteractor watchedInteractor;
    private final PlannedInteractor plannedInteractor;
    private final Integer upcomingPosition, postPosition, postId, mediaType;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private boolean adult;

    @Inject
    public MoviePresenter(Router router, Context context, RemotePostInteractor remotePostInteractor,
                          WatchedInteractor watchedInteractor,
                          PlannedInteractor plannedInteractor,
                          @UpcomingMoviePosition Integer upcomingPosition,
                          @MoviePosition Integer postPosition,
                          @PostId Integer postId,
                          @MediaType Integer mediaType) {
        this.router = router;
        this.context = context;
        this.remotePostInteractor = remotePostInteractor;
        this.watchedInteractor = watchedInteractor;
        this.plannedInteractor = plannedInteractor;
        this.upcomingPosition = upcomingPosition;
        this.postPosition = postPosition;
        this.postId = postId;
        this.mediaType = mediaType;
    }

    @Override
    public void onFirstViewAttach() {
        if (upcomingPosition != 0) {
            getMovie(upcomingPosition, context.getResources().getString(R.string.language));
            getSimilarMovies(upcomingPosition, context.getResources().getString(R.string.language));
            getVideos(upcomingPosition, context.getResources().getString(R.string.language));
            getCredits(upcomingPosition);
            compositeDisposable.add(watchedInteractor.isMovieExists(upcomingPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedInteractor.isMovieExists(upcomingPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        } else if (postPosition != 0) {
            getMovie(postPosition, context.getResources().getString(R.string.language));
            getSimilarMovies(postPosition, context.getResources().getString(R.string.language));
            getVideos(postPosition, context.getResources().getString(R.string.language));
            getCredits(postPosition);
            compositeDisposable.add(watchedInteractor.isMovieExists(postPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedInteractor.isMovieExists(postPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }
        if (mediaType == 1) {
            getMovie(postId, context.getResources().getString(R.string.language));
            getSimilarMovies(postId, context.getResources().getString(R.string.language));
            getVideos(postId, context.getResources().getString(R.string.language));
            getCredits(postId);
            compositeDisposable.add(watchedInteractor.isMovieExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedInteractor.isMovieExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }
    }

    private void getMovie(int id, String language) {
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        adult = idAdult == 1;
        compositeDisposable.add(remotePostInteractor.getMovie(id, language)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterSuccess(movie -> getViewState().showMovieScreen())
                .subscribe(movie -> {
                    if (adult) {
                        getViewState().showMovie(movie);
                    } else {
                        if (movie.isAdult()) {
                            getViewState().showErrorScreen();
                        } else {
                            getViewState().showMovie(movie);
                        }
                    }}, throwable -> getViewState().showErrorScreen()));
    }

    private void getSimilarMovies(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getSimilarMovies(id, language)
                .subscribe(movieResponses -> getViewState().showSimilarMovieList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void updatePlannedMovie(Movie movieFromLocal, Movie movieFromWeb) {
        if (!movieFromLocal.equals(movieFromWeb)) {
            movieFromWeb.setAddedDate(movieFromLocal.getAddedDate());
            movieFromWeb.setPosterPath(movieFromLocal.getPosterPath());
            plannedInteractor.updateMovie(movieFromWeb);
        }
    }

    private void updateWatchedMovie(Movie movieFromLocal, Movie movieFromWeb) {
        if (!movieFromLocal.equals(movieFromWeb)) {
            movieFromWeb.setAddedDate(movieFromLocal.getAddedDate());
            movieFromWeb.setPosterPath(movieFromLocal.getPosterPath());
            watchedInteractor.updateMovie(movieFromWeb);
        }
    }

    private void getVideos(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getMovieVideos(id, language)
                .subscribe(videoResponse -> getViewState().showVideos(videoResponse.getResults()),
                        Throwable::printStackTrace));
    }

    private void getCredits(int id) {
        compositeDisposable.add(remotePostInteractor.getCredits(id)
                .subscribe(this::sendCredits, Throwable::printStackTrace));
    }

    private void sendCredits(Credits credits) {
        List<Cast> newCastList = new ArrayList<>();
        List<Crew> newCrewList = new ArrayList<>();
        for (int number = 0; number < credits.getCrew().size(); number++) {
            if (credits.getCrew().get(number).getJob().equals("Director") ||
                    credits.getCrew().get(number).getJob().equals("Screenplay") ||
                    credits.getCrew().get(number).getJob().equals("Producer") ||
                    credits.getCrew().get(number).getJob().equals("Original Music Composer")) {
                newCrewList.add(credits.getCrew().get(number));
            }
        }
        if (credits.getCrew().size() > 10) getViewState().showCrewExpandButton(credits.getCrew());
        for (int number = 0; number < credits.getCast().size(); number++) {
            if (number < 10) newCastList.add(credits.getCast().get(number));
        }
        if (credits.getCast().size() > 10) getViewState().showCastExpandButton(credits.getCast());
        getViewState().showCrew(newCrewList);
        getViewState().showCast(newCastList);
    }

    public void addMovieAsWatched(Movie movie) {
        watchedInteractor.addMovie(movie);
        watchedInteractor.addMovieFB(movie.getId());
        getViewState().setSaveButtonEnabled(true);
    }

    public void deleteMovieFromWatched(Movie movie) {
        watchedInteractor.deleteMovie(movie);
        getViewState().setSaveButtonEnabled(false);
    }

    public void addMovieAsPlanned(Movie movie) {
        plannedInteractor.addMovie(movie);
        plannedInteractor.addMovieFB(movie.getId());
        getViewState().setPlanButtonEnabled(true);
    }

    public void deleteMovieFromPlanned(Movie movie) {
        plannedInteractor.deleteMovie(movie);
        getViewState().setPlanButtonEnabled(false);
    }

    @Nullable
    public boolean isPlannedMovieChanged(int id, Movie movieFromWeb) {
        return compositeDisposable.add(plannedInteractor.getMovie(id)
                .subscribe(movie -> updatePlannedMovie(movie, movieFromWeb), Throwable::printStackTrace));
    }

    @Nullable
    public boolean isWatchedMovieChanged(int id, Movie movieFromWeb) {
        return compositeDisposable.add(watchedInteractor.getMovie(id)
                .subscribe(movie -> updateWatchedMovie(movie, movieFromWeb), Throwable::printStackTrace));
    }

    public void goToDetailedMovieScreen(Integer id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
    }

    public void goToDetailedPersonScreen(int id) {
        router.navigateTo(new Screens.SearchItemScreen(id, 3));
    }

    public Router getRouter() {
        return router;
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
