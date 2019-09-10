package az.amorphist.habitube.model.repository;

import javax.inject.Inject;

import az.amorphist.habitube.entities.movie.Movie;
import az.amorphist.habitube.entities.movielite.MovieResponse;
import az.amorphist.habitube.entities.person.Person;
import az.amorphist.habitube.entities.show.Show;
import az.amorphist.habitube.model.server.MovieDBApi;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemotePostRepository {

    private final MovieDBApi movieDBApi;

    @Inject
    public RemotePostRepository(MovieDBApi movieDBApi) {
        this.movieDBApi = movieDBApi;
    }

    public Single<Movie> getMovie(int movieId) {
        return movieDBApi.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Show> getShow(int showId) {
        return movieDBApi.getTVShow(showId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Person> getStar(int personId) {
        return movieDBApi.getMovieStar(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getSimilarMovies(int id) {
        return movieDBApi.getSimilarMovies(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getSimilarTVShows(int id) {
        return movieDBApi.getSimilarTVShow(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
