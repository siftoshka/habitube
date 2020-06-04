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
public class DiscoverPresenter extends MvpPresenter<DiscoverView> {

    private final Router router;
    private final Context context;
    private final RemoteExploreInteractor remoteExploreInteractor;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public DiscoverPresenter(Router router, Context context, RemoteExploreInteractor remoteExploreInteractor) {
        this.router = router;
        this.context = context;
        this.remoteExploreInteractor = remoteExploreInteractor;
    }

    public void discoverMovies(int page, String sortSelection, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverMovies(page, language, sortSelection, isAdult, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMedia(movieResponse.getResults())));
    }

    public void discoverShows(int page, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, null, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMedia(movieResponse.getResults())));
    }

    public void getMoreMovies(int page, String sortSelection, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverMovies(page, language, sortSelection, isAdult, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMoreMedia(movieResponse.getResults())));
    }

    public void getMoreShows(int page, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, null, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> getViewState().showMoreMedia(movieResponse.getResults())));
    }

    public void goToMovieScreen(int id) {
        router.navigateTo(new Screens.PostMovieScreen(id));
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
