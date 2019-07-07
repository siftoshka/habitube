package az.amorphist.poster.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import az.amorphist.poster.R;
import az.amorphist.poster.di.PostId;
import az.amorphist.poster.di.modules.PostModule;
import az.amorphist.poster.presentation.presenters.PostPresenter;
import az.amorphist.poster.presentation.views.PostView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PostFragment extends MvpAppCompatFragment implements PostView {

    Toolbar toolbar;
    TextView userId, postId, postTitle, postBody;

    @InjectPresenter PostPresenter postPresenter;

    @ProvidePresenter
    PostPresenter postPresenter() {
        final Bundle bundle = getArguments();
        final String postId = bundle.getString("postID");

        final Scope temporaryPostScope = Toothpick.openScopes( "APP_SCOPE", "INT_SCOPE");
        temporaryPostScope.installModules(new PostModule(postId));
        final PostPresenter postPresenter = temporaryPostScope.getInstance(PostPresenter.class);
        Toothpick.closeScope("INT_SCOPE");
        return postPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        toolbar = view.findViewById(R.id.post_toolbar);
        userId = view.findViewById(R.id.post_user_id);
        postId = view.findViewById(R.id.post_post_id);
        postTitle = view.findViewById(R.id.post_title);
        postBody = view.findViewById(R.id.post_body);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postPresenter.goBack();
            }
        });
    }

    @Override
    public void getPost(String currentUserId, String currentPostId, String currentPostTitle, String currentPostBody) {
        userId.setText(currentUserId);
        postId.setText(currentPostId);
        postTitle.setText(currentPostTitle);
        postBody.setText(currentPostBody);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
