package az.siftoshka.habitube.presentation.explore;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.model.interactor.RemoteExploreInteractor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

import static android.content.Context.MODE_PRIVATE;

@InjectViewState
public class DisneyPresenter extends MvpPresenter<DisneyView> {

    private final Router router;
    private final Context context;
    private final RemoteExploreInteractor remoteExploreInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DisneyPresenter(Router router, Context context, RemoteExploreInteractor remoteExploreInteractor) {
        this.router = router;
        this.context = context;
        this.remoteExploreInteractor = remoteExploreInteractor;
    }

    public void showDisneyPopular(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, "2739", null, null, 0, 10, 100, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMedia(movieResponse.getResults())));
    }

    public void showDisneyBest(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "vote_average.desc", isAdult, "2739", null, null, 0, 10, 300, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMedia(movieResponse.getResults())));
    }

    public void showDisneyNew(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "first_air_date.desc", isAdult, "2739", null, null, 0, 10, 100, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMedia(movieResponse.getResults())));
    }


    public void showMoreDisneyPopular(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, "2739", null, null, 0, 10, 100, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMoreMedia(movieResponse.getResults())));
    }

    public void showMoreDisneyBest(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "vote_average.desc", isAdult, "2739", null, null, 0, 10, 300, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMoreMedia(movieResponse.getResults())));
    }

    public void showMoreDisneyNew(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "first_air_date.desc", isAdult, "2739", null, null, 0, 10, 100, null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMoreMedia(movieResponse.getResults())));
    }

    public void goToShowScreen(int id) {
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
