package az.amorphist.poster.model.interactors;

import javax.inject.Inject;

import az.amorphist.poster.entities.movielite.MovieResponse;
import az.amorphist.poster.model.repository.RemoteExploreRepository;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RemoteExploreInteractor {

    private final RemoteExploreRepository repository;

    @Inject
    public RemoteExploreInteractor(RemoteExploreRepository repository) {
        this.repository = repository;
    }

    public Single<MovieResponse> getUpcomingMovies() {
        return repository.getUpcomingMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getMovies() {
        return repository.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getTVShows() {
        return repository.getTVShows()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<MovieResponse> getSearchResults(String queryName) {
        return repository.getSearchResults(queryName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
