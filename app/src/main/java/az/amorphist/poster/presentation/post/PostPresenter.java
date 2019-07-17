package az.amorphist.poster.presentation.post;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowId;
import az.amorphist.poster.di.qualifiers.UpcomingId;
import az.amorphist.poster.entities.Movie;
import az.amorphist.poster.entities.MoviePager;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private ApiProvider provider;
    private final Integer upcomingId, postId, showId;

    @Inject
    public PostPresenter(Router router, ApiProvider provider, @UpcomingId Integer upcomingId, @PostId Integer postId, @ShowId Integer showId) {
        this.router = router;
        this.provider = provider;
        this.upcomingId = upcomingId;
        this.postId = postId;
        this.showId = showId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (upcomingId != 0) {
            getUpcomingMovie();
        } else if (postId != 0) {
            getMovie();
        } else {
            getTVShow();
        }
    }

    private void getUpcomingMovie() {
        provider.get().getUpcomingMovies(API_KEY).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(@NonNull Call<MoviePager> call, @NonNull Response<MoviePager> response) {
                MoviePager pager = response.body();
                Movie movie = pager.getResults().get(upcomingId - 1);
                getViewState().getMovie(
                        movie.getMovieImage(),
                        movie.getMovieBackgroundImage(),
                        movie.getMovieTitle(),
                        movie.getMovieDate(),
                        movie.getMovieRate(),
                        movie.getMovieViews(),
                        movie.getMovieBody()
                );
            }

            @Override
            public void onFailure(@NonNull Call<MoviePager> call, @NonNull Throwable t) {

            }
        });
    }

    private void getMovie() {
        provider.get().getTrendingMovies(API_KEY).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(@NonNull Call<MoviePager> call, @NonNull Response<MoviePager> response) {
                MoviePager pager = response.body();
                Movie movie = pager.getResults().get(postId - 1);
                getViewState().getMovie(
                        movie.getMovieImage(),
                        movie.getMovieBackgroundImage(),
                        movie.getMovieTitle(),
                        movie.getMovieDate(),
                        movie.getMovieRate(),
                        movie.getMovieViews(),
                        movie.getMovieBody()
                );
            }

            @Override
            public void onFailure(@NonNull Call<MoviePager> call, @NonNull Throwable t) {
            }
        });
    }

    private void getTVShow() {
        provider.get().getTrendingTVShows(API_KEY).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(@NonNull Call<MoviePager> call, @NonNull Response<MoviePager> response) {
                MoviePager pager = response.body();
                Movie movie = pager.getResults().get(showId - 1);
                getViewState().getMovie(
                        movie.getMovieImage(),
                        movie.getMovieBackgroundImage(),
                        movie.getShowTitle(),
                        movie.getShowDate(),
                        movie.getMovieRate(),
                        movie.getMovieViews(),
                        movie.getMovieBody()
                );
            }

            @Override
            public void onFailure(@NonNull Call<MoviePager> call, @NonNull Throwable t) {
            }
        });
    }

    public void goBack() {
        router.exit();
    }

}
