package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowId;
import toothpick.config.Module;

public class MovieModule extends Module {

    public MovieModule(Integer postId) {
        bind(Integer.class).withName(PostId.class).toInstance(postId);
    }
}
