package az.amorphist.habitube.di.modules;

import az.amorphist.habitube.di.qualifiers.MoviePosition;
import az.amorphist.habitube.di.qualifiers.ShowPosition;
import az.amorphist.habitube.di.qualifiers.UpcomingMoviePosition;
import toothpick.config.Module;

public class MovieModule extends Module {

    public MovieModule(Integer moviePosition, Integer showPosition, Integer upcomingPosition) {
        bind(Integer.class).withName(MoviePosition.class).toInstance(moviePosition);
        bind(Integer.class).withName(ShowPosition.class).toInstance(showPosition);
        bind(Integer.class).withName(UpcomingMoviePosition.class).toInstance(upcomingPosition);
    }
}
