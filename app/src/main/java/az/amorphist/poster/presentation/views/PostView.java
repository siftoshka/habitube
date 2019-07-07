package az.amorphist.poster.presentation.views;

import moxy.MvpView;

public interface PostView extends MvpView {
    void getPost(String currentUserId,
                 String currentPostId,
                 String currentPostTitle,
                 String currentPostBody);
}
