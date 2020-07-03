package az.siftoshka.habitube.model.repository;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.model.server.MovieDBApi;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemoteExploreRepository {

    private final MovieDBApi movieDBApi;

    @Inject
    public RemoteExploreRepository(MovieDBApi movieDBApi) {
        this.movieDBApi = movieDBApi;
    }

    public Single<MovieResponse> getUpcomingMovies(int page, String language) {
        return movieDBApi.getUpcomingMovies(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getMovies(int page, String language) {
        return movieDBApi.getTrendingMovies(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getTVShows(int page, String language) {
        return movieDBApi.getTrendingTVShows(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getAirTodayShows(int page, String language) {
        return movieDBApi.getAirTodayShows(page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getSearchResults(String queryName, int page,  String language, boolean isAdult) {
        return movieDBApi.getSearchResults(language, queryName, page, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getMovieSearchResults(String queryName, int page, String language, boolean isAdult) {
        return movieDBApi.getMovieSearchResults(language, queryName, page, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getShowSearchResults(String queryName, int page, String language, boolean isAdult) {
        return movieDBApi.getShowSearchResults(language, queryName, page, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getPersonSearchResults(String queryName, int page, String language, boolean isAdult) {
        return movieDBApi.getPersonSearchResults(language, queryName, page, isAdult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getDiscoveredMovies(int page, String language, String sortSelection, boolean isAdult,
                                                         String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, String genre) {
        return movieDBApi.getDiscoverMovies(language, sortSelection, page, isAdult, yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, 10, genre)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getDiscoveredShows(int page, String language, String sortSelection, boolean isAdult,
                                                        String network, String yearIndexUp, String yearIndexDown, int voteIndexUp, int voteIndexDown, int voteCount, String genre) {
        return movieDBApi.getDiscoverShows(language, sortSelection, page, isAdult, network,  yearIndexUp, yearIndexDown, voteIndexUp, voteIndexDown, voteCount, genre)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
