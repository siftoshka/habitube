package az.amorphist.poster.model.repository;

import javax.inject.Inject;

import az.amorphist.poster.entities.movielite.MovieResponse;
import az.amorphist.poster.server.MovieDBApi;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static az.amorphist.poster.Constants.SYSTEM.APP_LANG;

public class RemoteExploreRepository {

    private final MovieDBApi movieDBApi;

    @Inject
    public RemoteExploreRepository(MovieDBApi movieDBApi) {
        this.movieDBApi = movieDBApi;
    }

    public Single<MovieResponse> getUpcomingMovies() {
        return movieDBApi.getUpcomingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getMovies() {
        return movieDBApi.getTrendingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getTVShows() {
        return movieDBApi.getTrendingTVShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getSearchResults(String queryName) {
        return movieDBApi.getSearchResults(APP_LANG, queryName, 1, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
