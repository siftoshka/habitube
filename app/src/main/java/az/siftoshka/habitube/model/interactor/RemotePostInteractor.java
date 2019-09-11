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

    public Single<Movie> getMovie(int movieId, String language) {
        return repository.getMovie(movieId, language);
    }

    public Single<Show> getTVShow(int showId, String language) {
        return repository.getShow(showId, language);
    }

    public Single<Person> getStar(int personId, String language) {
        return repository.getStar(personId, language);
    }

    public Single<MovieResponse> getSimilarMovies(int id, String language) {
        return repository.getSimilarMovies(id, language);
    }

    public Single<MovieResponse> getSimilarTVShows(int id, String language) {
        return repository.getSimilarTVShows(id, language);
    }
}
