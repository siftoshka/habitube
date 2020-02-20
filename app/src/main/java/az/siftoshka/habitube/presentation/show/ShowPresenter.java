package az.siftoshka.habitube.presentation.show;

import android.content.Context;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.MediaType;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.di.qualifiers.ShowPosition;
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
    protected void onFirstViewAttach() {
        if(showPosition != 0) {
            getTVShow(showPosition, context.getResources().getString(R.string.language));
            getSimilarTVShows(showPosition, context.getResources().getString(R.string.language));
            getVideos(showPosition, context.getResources().getString(R.string.language));
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

    private void getSimilarTVShows(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getSimilarTVShows(id, language)
                .subscribe(movieResponses -> getViewState().showSimilarTVShowList(movieResponses.getResults()),
                        Throwable::printStackTrace));
    }

    private void getVideos(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getTVShowVideos(id, language)
                .subscribe(videoResponse -> getViewState().showVideos(videoResponse.getResults()),
                        Throwable::printStackTrace));
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

    public void addShowAsWatched(Show show) {
        watchedInteractor.addShow(show);
        getViewState().setSaveButtonEnabled(true);
    }

    public void addShowAsPlanned(Show show) {
        plannedInteractor.addShow(show);
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
}
