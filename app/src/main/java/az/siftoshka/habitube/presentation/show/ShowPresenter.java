package az.siftoshka.habitube.presentation.show;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.MediaType;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.di.qualifiers.ShowPosition;
import az.siftoshka.habitube.entities.credits.Cast;
import az.siftoshka.habitube.entities.credits.Credits;
import az.siftoshka.habitube.entities.credits.Crew;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.interactor.PlannedInteractor;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import az.siftoshka.habitube.model.interactor.WatchedInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ShowPresenter extends MvpPresenter<ShowView> {

    private final Router router;
    private final Context context;
    private final Integer showPosition, postId, mediaType;
    private final WatchedInteractor watchedInteractor;
    private final PlannedInteractor plannedInteractor;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ShowPresenter(Router router, Context context,
                         WatchedInteractor watchedInteractor,
                         PlannedInteractor plannedInteractor,
                         RemotePostInteractor remotePostInteractor,
                         @ShowPosition Integer showPosition, @PostId Integer postId,
                         @MediaType Integer mediaType) {
        this.router = router;
        this.context = context;
        this.watchedInteractor = watchedInteractor;
        this.plannedInteractor = plannedInteractor;
        this.remotePostInteractor = remotePostInteractor;
        this.showPosition = showPosition;
        this.postId = postId;
        this.mediaType = mediaType;
    }

    @Override
    public void onFirstViewAttach() {
        if(showPosition != 0) {
            getTVShow(showPosition, context.getResources().getString(R.string.language));
            getSimilarTVShows(showPosition, context.getResources().getString(R.string.language));
            getVideos(showPosition, context.getResources().getString(R.string.language));
            getCredits(showPosition);
            compositeDisposable.add(watchedInteractor.isShowExists(showPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedInteractor.isShowExists(showPosition)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }

        if (mediaType == 2) {
            getTVShow(postId, context.getResources().getString(R.string.language));
            getSimilarTVShows(postId, context.getResources().getString(R.string.language));
            getVideos(postId, context.getResources().getString(R.string.language));
            getCredits(postId);
            compositeDisposable.add(watchedInteractor.isShowExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setSaveButtonEnabled));
            compositeDisposable.add(plannedInteractor.isShowExists(postId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getViewState()::setPlanButtonEnabled));
        }
    }

    private void getTVShow(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getTVShow(id, language)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterSuccess(show -> getViewState().showTVShowScreen())
                .subscribe(show -> getViewState().showTVShow(show),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void updatePlannedShow(Show showFromLocal, Show showFromWeb) {
        if (!showFromLocal.equals(showFromWeb)) {
            showFromWeb.setAddedDate(showFromLocal.getAddedDate());
            showFromWeb.setPosterPath(showFromLocal.getPosterPath());
            plannedInteractor.updateShow(showFromWeb);
        }
    }

    private void updateWatchedShow(Show showFromLocal, Show showFromWeb) {
        if (!showFromLocal.equals(showFromWeb)) {
            showFromWeb.setAddedDate(showFromLocal.getAddedDate());
            showFromWeb.setMyRating(showFromLocal.getMyRating());
            showFromWeb.setPosterPath(showFromLocal.getPosterPath());
            watchedInteractor.updateShow(showFromWeb);
        }
    }

    public void updateRating(Show show, float rating) {
        show.setMyRating(rating);
        watchedInteractor.updateShow(show);
        watchedInteractor.addShowFB(show);
    }

    public void getSavedWShowId(int id) {
        compositeDisposable.add(watchedInteractor.getShow(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(show -> getViewState().showRating(show, show.getMyRating())));
    }


    private void getSimilarTVShows(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getSimilarTVShows(id, 1, language)
                .subscribe(movieResponses -> getViewState().showSimilarTVShowList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    public void getMoreSimilarShows(int id, int page) {
        compositeDisposable.add(remotePostInteractor.getSimilarTVShows(id, page, context.getResources().getString(R.string.language))
                .subscribe(movieResponses -> getViewState().showMoreSimilarShows(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void getVideos(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getTVShowVideos(id, language)
                .subscribe(videoResponse -> getViewState().showVideos(videoResponse.getResults()),
                        Throwable::printStackTrace));
    }

    private void getCredits(int id) {
        compositeDisposable.add(remotePostInteractor.getShowCredits(id)
                .subscribe(this::sendCredits, Throwable::printStackTrace));
    }

    private void sendCredits(Credits credits) {
        List<Cast> newCastList = new ArrayList<>();
        List<Crew> newCrewList = new ArrayList<>();
        for (int number = 0; number <= credits.getCrew().size() - 1; number++) {
            if (credits.getCrew().get(number).getJob().equals("Novel") ||
                    credits.getCrew().get(number).getJob().equals("Director") ||
                    credits.getCrew().get(number).getJob().equals("Screenplay") ||
                    credits.getCrew().get(number).getJob().equals("Producer") ||
                    credits.getCrew().get(number).getJob().equals("Original Music Composer")) {
                newCrewList.add(credits.getCrew().get(number));
            }
        }
        if (credits.getCrew().size() > 10) getViewState().showCrewExpandButton(credits.getCrew());
        for (int number = 0; number <= credits.getCast().size() - 1; number++) {
            if (number < 10) newCastList.add(credits.getCast().get(number));
        }
        if (credits.getCast().size() > 10) getViewState().showCastExpandButton(credits.getCast());
        getViewState().showCrew(newCrewList);
        getViewState().showCast(newCastList);
    }

    public void addShowAsWatched(Show show) {
        watchedInteractor.addShow(show);
        watchedInteractor.addShowFB(show);
        getViewState().setSaveButtonEnabled(true);
    }

    public void addShowAsPlanned(Show show) {
        plannedInteractor.addShow(show);
        plannedInteractor.addShowFB(show);
        getViewState().setPlanButtonEnabled(true);
    }

    public void deleteShowFromWatched(Show show) {
        watchedInteractor.deleteShow(show);
        getViewState().setSaveButtonEnabled(false);
    }

    public void deleteShowFromPlanned(Show show) {
        plannedInteractor.deleteShow(show);
        getViewState().setPlanButtonEnabled(false);
    }

    public boolean isPlannedShowChanged(int id, Show showFromWeb) {
        return compositeDisposable.add(plannedInteractor.getShow(id)
                .subscribe(show -> updatePlannedShow(show, showFromWeb), Throwable::printStackTrace));
    }

    public boolean isWatchedShowChanged(int id, Show showFromWeb) {
        return compositeDisposable.add(watchedInteractor.getShow(id)
                .subscribe(show -> updateWatchedShow(show, showFromWeb), Throwable::printStackTrace));
    }

    public void goToDetailedShowScreen(Integer id) {
        router.navigateTo(new Screens.PostShowScreen(id));
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
