package az.siftoshka.habitube.presentation.explore;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import javax.inject.Inject;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.entities.movielite.MovieLite;
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

    public void discoverMovies(int page, String sortSelection, String yearIndex, int voteIndex) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverMovies(page, language, sortSelection, isAdult, yearIndex, voteIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> goToDiscoverScreen(movieResponse.getResults()), Throwable::printStackTrace));
    }

    public void discoverShows(int page, String yearIndex, int voteIndex) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, null, yearIndex, voteIndex, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> goToDiscoverShowScreen(movieResponse.getResults()), Throwable::printStackTrace));
    }

    public void showNetflixPopular(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "popularity.desc", isAdult, "213", null, 0, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> goToNetflixScreen(movieResponse.getResults(), 0), Throwable::printStackTrace));
    }

    public void showNetflixBest(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "vote_average.desc", isAdult, "213", null, 0, 300)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> goToNetflixScreen(movieResponse.getResults(), 1), Throwable::printStackTrace));
    }

    public void showNetflixNew(int page) {
        String language = context.getResources().getString(R.string.language);
        SharedPreferences prefs = context.getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);
        boolean isAdult = idAdult == 1;
        compositeDisposable.add(remoteExploreInteractor.discoverShows(page, language, "first_air_date.desc", isAdult, "213", null, 0, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieResponse -> goToNetflixScreen(movieResponse.getResults(), 2), Throwable::printStackTrace));
    }

    private void goToDiscoverScreen(List<MovieLite> movies) {
        router.navigateTo(new Screens.DiscoverScreen(movies));
    }

    private void goToDiscoverShowScreen(List<MovieLite> shows) {
        router.navigateTo(new Screens.DiscoverShowScreen(shows));
    }

    private void goToNetflixScreen(List<MovieLite> shows, int index) {
        router.navigateTo(new Screens.NetflixDiscoverScreen(shows, index));
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
