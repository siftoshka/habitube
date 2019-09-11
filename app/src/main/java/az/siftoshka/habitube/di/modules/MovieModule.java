package az.siftoshka.habitube.di.modules;

import az.siftoshka.habitube.di.qualifiers.MoviePosition;
import az.siftoshka.habitube.di.qualifiers.ShowPosition;
import az.siftoshka.habitube.di.qualifiers.UpcomingMoviePosition;
import toothpick.config.Module;

public class MovieModule extends Module {

    public MovieModule(Integer moviePosition, Integer showPosition, Integer upcomingPosition) {
        bind(Integer.class).withName(MoviePosition.class).toInstance(moviePosition);
        bind(Integer.class).withName(ShowPosition.class).toInstance(showPosition);
        bind(Integer.class).withName(UpcomingMoviePosition.class).toInstance(upcomingPosition);
    }
}
