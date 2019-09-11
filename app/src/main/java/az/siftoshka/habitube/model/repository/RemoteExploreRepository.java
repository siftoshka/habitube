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

    public Single<MovieResponse> getUpcomingMovies(String language) {
        return movieDBApi.getUpcomingMovies(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getMovies(String language) {
        return movieDBApi.getTrendingMovies(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getTVShows(String language) {
        return movieDBApi.getTrendingTVShows(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MovieResponse> getSearchResults(String queryName, String language) {
        return movieDBApi.getSearchResults(language, queryName, 1, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
