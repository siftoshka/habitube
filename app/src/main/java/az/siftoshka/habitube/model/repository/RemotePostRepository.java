package az.siftoshka.habitube.model.repository;

import javax.inject.Inject;

import az.siftoshka.habitube.entities.credits.Credits;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.movielite.MovieResponse;
import az.siftoshka.habitube.entities.person.Person;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.entities.video.VideoResponse;
import az.siftoshka.habitube.model.server.MovieDBApi;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RemotePostRepository {

    private final MovieDBApi movieDBApi;

    @Inject
    public RemotePostRepository(MovieDBApi movieDBApi) {
        this.movieDBApi = movieDBApi;
    }

    public Single<Movie> getMovie(int movieId, String language) {
        return movieDBApi.getMovie(movieId, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Show> getShow(int showId, String language) {
        return movieDBApi.getTVShow(showId, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Person> getStar(int personId, String language) {
        return movieDBApi.getMovieStar(personId, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getSimilarMovies(int id, int page, String language) {
        return movieDBApi.getSimilarMovies(id, page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<VideoResponse> getMovieVideos(int id, String language) {
        return movieDBApi.getMovieVideos(id, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<VideoResponse> getTVShowVideos(int id, String language) {
        return movieDBApi.getTVShowVideos(id, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<MovieResponse> getSimilarTVShows(int id, int page, String language) {
        return movieDBApi.getSimilarTVShow(id, page, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Credits> getCredits(int id) {
        return movieDBApi.getCredits(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<az.siftoshka.habitube.entities.personcredits.Credits> getPersonMovieCredits(int id, String language) {
        return movieDBApi.getPersonMovieCredits(id, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Credits> getShowCredits(int id) {
        return movieDBApi.getShowCredits(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<az.siftoshka.habitube.entities.personcredits.Credits> getPersonShowCredits(int id, String language) {
        return movieDBApi.getPersonShowCredits(id, language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
