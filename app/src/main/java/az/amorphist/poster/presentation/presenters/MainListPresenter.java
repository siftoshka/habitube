package az.amorphist.poster.presentation.presenters;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.Post;
import az.amorphist.poster.presentation.views.MainListView;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class MainListPresenter extends MvpPresenter<MainListView> {

    private final Router router;
    private ApiProvider provider;

    @Inject
    public MainListPresenter(Router router, ApiProvider provider) {
        this.router = router;
        this.provider = provider;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

    }

    public void addPost(int id) {
        provider.get().getPost(id).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                getViewState().addPost(post);
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                getViewState().unsuccessfulQueryError();
            }
        });
    }

    public void goToDetailedScreen(String id) {
        router.navigateTo(new Screens.PostScreen(id));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
