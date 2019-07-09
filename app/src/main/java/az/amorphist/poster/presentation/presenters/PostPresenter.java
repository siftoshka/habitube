package az.amorphist.poster.presentation.presenters;

import android.util.Log;

import javax.inject.Inject;

import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowId;
import az.amorphist.poster.entities.Movie;
import az.amorphist.poster.entities.MoviePager;
import az.amorphist.poster.presentation.views.PostView;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.apiKey;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private ApiProvider provider;
    private final Integer postId;
    private final Integer showId;

    @Inject
    public PostPresenter(Router router, ApiProvider provider, @PostId Integer postId, @ShowId Integer showId) {
        this.router = router;
        this.provider = provider;
        this.postId = postId;
        this.showId = showId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if(showId != 0) {
            getTVShow();
        } else {
            getMovie();
        }
    }

    private void getMovie() {
        provider.get().getTrendingMovies(apiKey).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(Call<MoviePager> call, Response<MoviePager> response) {
                MoviePager pager = response.body();
                Movie movie = pager.getResults().get(postId);
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
            public void onFailure(Call<MoviePager> call, Throwable t) {
            }
        });
    }
    private void getTVShow() {
        provider.get().getTrendingTVShows(apiKey).enqueue(new Callback<MoviePager>() {
            @Override
            public void onResponse(Call<MoviePager> call, Response<MoviePager> response) {
                MoviePager pager = response.body();
                Movie movie = pager.getResults().get(showId);
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
            public void onFailure(Call<MoviePager> call, Throwable t) {
            }
        });
    }

    public void goBack() {
        router.exit();
    }

}
