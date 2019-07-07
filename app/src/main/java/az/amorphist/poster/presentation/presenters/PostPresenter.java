package az.amorphist.poster.presentation.presenters;

import javax.inject.Inject;

import az.amorphist.poster.di.PostId;
import az.amorphist.poster.di.providers.ApiProvider;
import az.amorphist.poster.entities.Post;
import az.amorphist.poster.presentation.views.PostView;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class PostPresenter extends MvpPresenter<PostView> {

    private final Router router;
    private ApiProvider provider;
    private final String postId;

    @Inject
    public PostPresenter(Router router, ApiProvider provider, @PostId String postId) {
        this.router = router;
        this.provider = provider;
        this.postId = postId;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getPostById();
    }

    public void getPostById() {
        provider.get().getPost(Integer.parseInt(postId)).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                getViewState().getPost(post.getUserId(), String.valueOf(post.getPostId()), post.getPostTitle(), post.getPostBody());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
            }
        });
    }

    public void goBack() {
        router.exit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
