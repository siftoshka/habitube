package az.amorphist.poster;

import android.os.Bundle;

import javax.inject.Inject;

import moxy.MvpAppCompatActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

public class MainActivity extends MvpAppCompatActivity {

    @Inject NavigatorHolder navigatorHolder;
    @Inject Router router;

    private final Navigator navigator = new SupportAppNavigator(this, R.id.fragment_container);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toothpick.inject(this, Toothpick.openScope("APP_SCOPE"));
        router.newRootScreen(new Screens.ExploreScreen());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }


}
