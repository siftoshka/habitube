package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.qualifiers.MoviePosition;
import az.amorphist.poster.di.qualifiers.ShowPosition;
import az.amorphist.poster.di.qualifiers.UpcomingMoviePosition;
import toothpick.config.Module;

public class MovieModule extends Module {

    public MovieModule(Integer moviePosition, Integer showPosition, Integer upcomingPosition) {
        bind(Integer.class).withName(MoviePosition.class).toInstance(moviePosition);
        bind(Integer.class).withName(ShowPosition.class).toInstance(showPosition);
        bind(Integer.class).withName(UpcomingMoviePosition.class).toInstance(upcomingPosition);
    }
}
