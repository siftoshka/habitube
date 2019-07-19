package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.qualifiers.MediaType;
import az.amorphist.poster.di.qualifiers.PostId;
import toothpick.config.Module;

public class SearchModule extends Module {

    public SearchModule(Integer postId, Integer mediaType) {
        bind(Integer.class).withName(PostId.class).toInstance(postId);
        bind(Integer.class).withName(MediaType.class).toInstance(mediaType);
    }
}
