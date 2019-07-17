package az.amorphist.poster.presentation.search;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.MovieLite;
import az.amorphist.poster.entities.MoviePagerLite;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

import static az.amorphist.poster.App.API_KEY;
import static az.amorphist.poster.App.APP_LANG;

@InjectViewState
public class SearchPresenter extends MvpPresenter<SearchView> {

    private final Router router;
    private ApiProvider provider;

    @Inject
    public SearchPresenter(Router router, ApiProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    public void searchMedia(String queryName) {
        provider.get().getSearchResults(API_KEY, APP_LANG, queryName, 1, false).enqueue(new Callback<MoviePagerLite>() {
            @Override
            public void onResponse(@NonNull Call<MoviePagerLite> call, @NonNull Response<MoviePagerLite> response) {
                if (response.isSuccessful()) {
                    MoviePagerLite pager = response.body();
                    List<MovieLite> results = pager.getResults();
                    getViewState().getSearchedMediaList(results);
                } else {
                    getViewState().unsuccessfulQueryError();
                }

            }

            @Override
            public void onFailure(Call<MoviePagerLite> call, Throwable t) {

            }
        });
    }

    public void goBack() {
        router.exit();
    }
}
