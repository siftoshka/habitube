package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.qualifiers.PostId;
import az.amorphist.poster.di.qualifiers.ShowId;
import az.amorphist.poster.di.qualifiers.UpcomingId;
import toothpick.config.Module;

public class MovieModule extends Module {

    public MovieModule(Integer postId, Integer showId, Integer upcomingId) {
        bind(Integer.class).withName(PostId.class).toInstance(postId);
        bind(Integer.class).withName(ShowId.class).toInstance(showId);
        bind(Integer.class).withName(UpcomingId.class).toInstance(upcomingId);
    }
}
