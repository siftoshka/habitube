package az.amorphist.poster.presentation.views;

import java.util.List;

import az.amorphist.poster.entities.Post;
import moxy.MvpView;

public interface MainListView extends MvpView {
    void addPost(Post post);
    void showPosts(List<Post> posts);
    void unsuccessfulQueryError();
}
