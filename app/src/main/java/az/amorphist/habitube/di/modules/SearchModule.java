package az.amorphist.habitube.di.modules;

import az.amorphist.habitube.di.qualifiers.MediaType;
import az.amorphist.habitube.di.qualifiers.PostId;
import toothpick.config.Module;

public class SearchModule extends Module {

    public SearchModule(Integer postId, Integer mediaType) {
        bind(Integer.class).withName(PostId.class).toInstance(postId);
        bind(Integer.class).withName(MediaType.class).toInstance(mediaType);
    }
}
