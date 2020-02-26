package az.siftoshka.habitube.presentation.star;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.di.qualifiers.PostId;
import az.siftoshka.habitube.entities.personcredits.Cast;
import az.siftoshka.habitube.entities.personcredits.Credits;
import az.siftoshka.habitube.entities.personcredits.Crew;
import az.siftoshka.habitube.model.interactor.RemotePostInteractor;
import io.reactivex.disposables.CompositeDisposable;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class StarPresenter extends MvpPresenter<StarView> {

    private final Router router;
    private final Context context;
    private final Integer postId;
    private final RemotePostInteractor remotePostInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public StarPresenter(Router router, Context context,
                         RemotePostInteractor remotePostInteractor, @PostId Integer postId) {
        this.router = router;
        this.context = context;
        this.remotePostInteractor = remotePostInteractor;
        this.postId = postId;
    }

    @Override
    public void onFirstViewAttach() {
        getMovieStar(postId, context.getResources().getString(R.string.language));
    }

    private void getMovieStar(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getStar(id, language)
                .doOnSubscribe(disposable -> getViewState().showProgress(true))
                .doAfterTerminate(() -> getViewState().showProgress(false))
                .doAfterSuccess(person -> {getViewState().showPersonScreen();
                getMovieCredits(id, language); getShowCredits(id, language);})
                .subscribe(person -> getViewState().showPerson( person),
                        throwable -> getViewState().showErrorScreen()));
    }

    private void getMovieCredits(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getPersonMovieCredits(id, language)
        .subscribe(this::sendMovieCredit, Throwable::printStackTrace));
    }

    private void getShowCredits(int id, String language) {
        compositeDisposable.add(remotePostInteractor.getPersonShowCredits(id, language)
                .subscribe(this::sendShowCredit, Throwable::printStackTrace));
    }

    private void sendMovieCredit(Credits credits) {
        List<Cast> newCastList = new ArrayList<>();
        List<Crew> newCrewList = new ArrayList<>();
        for (int number = 0; number < credits.getCrew().size(); number++) {
            if (number < 10) newCrewList.add(credits.getCrew().get(number));
        }
        if (credits.getCrew().size() > 10) {
            getViewState().showMovieCrewExpandButton(credits.getCrew());
        }
        for (int number = 0; number < credits.getCast().size(); number++) {
            if (number < 10) newCastList.add(credits.getCast().get(number));
        }
        if (credits.getCast().size() > 10) {
            getViewState().showMovieCastExpandButton(credits.getCast());
        }
        getViewState().showMovieCrew(newCrewList);
        getViewState().showMovieCast(newCastList);
    }

    private void sendShowCredit(Credits credits) {
        List<Cast> newCastList = new ArrayList<>();
        List<Crew> newCrewList = new ArrayList<>();
        for (int number = 0; number < credits.getCrew().size(); number++) {
            if (number < 10) newCrewList.add(credits.getCrew().get(number));
        }
        if (credits.getCrew().size() > 10) {
            getViewState().showTVShowCrewExpandButton(credits.getCrew());
        }
        for (int number = 0; number < credits.getCast().size(); number++) {
            if (number < 10) newCastList.add(credits.getCast().get(number));
        }
        if (credits.getCast().size() > 10) {
            getViewState().showTVShowCastExpandButton(credits.getCast());
        }
        getViewState().showTVShowCrew(newCrewList);
        getViewState().showTVShowCast(newCastList);
    }

    public void goBack() {
        router.exit();
    }

    public Router getRouter() {
        return router;
    }

    public void goToDetailedMovieScreen(int id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
    }

    public void goToDetailedShowScreen(int id) {
        router.navigateTo(new Screens.PostShowScreen(id));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }
}
