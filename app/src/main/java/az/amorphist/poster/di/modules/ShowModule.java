package az.amorphist.poster.di.modules;

import az.amorphist.poster.di.qualifiers.ShowId;
import toothpick.config.Module;

public class ShowModule extends Module {

    public ShowModule(Integer showId) {
        bind(Integer.class).withName(ShowId.class).toInstance(showId);
    }
}
