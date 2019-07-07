package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.PostId;
import toothpick.config.Module;

public class PostModule extends Module {
    public PostModule(String postId) {
        bind(String.class).withName(PostId.class).toInstance(postId);
    }
}
