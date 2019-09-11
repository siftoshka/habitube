package az.siftoshka.habitube.model.interactor;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.entities.person.Person;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.model.repository.RemotePostRepository;
import io.reactivex.Single;

public class RemotePostInteractor {

    private final RemotePostRepository repository;

    @Inject
    public RemotePostInteractor(RemotePostRepository repository) {
        this.repository = repository;
    }

    public Single<Movie> getMovie(int movieId) {
        return repository.getMovie(movieId);
    }

    public Single<Show> getTVShow(int showId) {
        return repository.getShow(showId);
    }

    public Single<Person> getStar(int personId) {
        return repository.getStar(personId);
    }

    public Single<MovieResponse> getSimilarMovies(int id) {
        return repository.getSimilarMovies(id);
    }

    public Single<MovieResponse> getSimilarTVShows(int id) {
        return repository.getSimilarTVShows(id);
    }
}
