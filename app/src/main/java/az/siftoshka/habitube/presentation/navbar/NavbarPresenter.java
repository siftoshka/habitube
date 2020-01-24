package az.siftoshka.habitube.presentation.navbar;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.utils.navigation.LocalRouter;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NavbarPresenter extends MvpPresenter<NavbarView> {

    private final Router router;
    private final NavigatorHolder navigatorHolder;

    @Inject
    public NavbarPresenter(LocalRouter router) {
        Cicerone<Router> cicerone = router.getCicerone("NAVBAR");
        this.router = cicerone.getRouter();
        this.navigatorHolder = cicerone.getNavigatorHolder();
    }

    @Override
    protected void onFirstViewAttach() {
        goToExplore();
    }

    public NavigatorHolder getNavigatorHolder() {
        return navigatorHolder;
    }

    public void goToLibrary() { router.newRootScreen(new Screens.LibraryScreen()); }

    public void goToExplore() { router.newRootScreen(new Screens.ExploreScreen()); }

    public void goToAccount() { router.newRootScreen(new Screens.AccountScreen()); }
}
