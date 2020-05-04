package az.siftoshka.habitube.model.interactor;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.model.repository.RemoteExploreRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemoteExploreInteractor {

    private final RemoteExploreRepository repository;

    @Inject
    public RemoteExploreInteractor(RemoteExploreRepository repository) {
        this.repository = repository;
    }

    public Single<MovieResponse> getUpcomingMovies(int page, String language) {
        return repository.getUpcomingMovies(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getMovies(int page, String language) {
        return repository.getMovies(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getTVShows(int page, String language) {
        return repository.getTVShows(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getAirTodayShows(int page, String language) {
        return repository.getAirTodayShows(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getSearchResults(String queryName, int page, String language, boolean isAdult) {
        return repository.getSearchResults(queryName, page, language, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getMovieSearchResults(String queryName, int page, String language, boolean isAdult) {
        return repository.getMovieSearchResults(queryName, page, language, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getShowSearchResults(String queryName, int page, String language, boolean isAdult) {
        return repository.getShowSearchResults(queryName, page, language, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getPersonSearchResults(String queryName, int page, String language, boolean isAdult) {
        return repository.getPersonSearchResults(queryName, page, language, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> discoverMovies(int page, String language, String sortSelection, boolean isAdult, String yearIndex, int voteIndex) {
        return repository.getDiscoveredMovies(page, language, sortSelection, isAdult, yearIndex, voteIndex);
    }

    public Observable<MovieResponse> discoverShows(int page, String language, String sortSelection, boolean isAdult, String network, String yearIndex, int voteIndex, int voteCount) {
        return repository.getDiscoveredShows(page, language, sortSelection, isAdult, network, yearIndex, voteIndex, voteCount);
    }
}
