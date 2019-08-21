package az.amorphist.poster.presentation.navbar;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import az.amorphist.poster.utils.navigation.LocalRouter;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NavbarPresenter extends MvpPresenter<NavbarView> {

    private final Router router;
    private final NavigatorHolder navigatorHolder;
    private boolean isLibraryOpened = false;
    private boolean isExploreOpened = false;
    private boolean isAccountOpened = false;

    @Inject
    public NavbarPresenter(LocalRouter router) {
        Cicerone<Router> cicerone = router.getCicerone("NAVBAR");
        this.router = cicerone.getRouter();
        this.navigatorHolder = cicerone.getNavigatorHolder();
    }

    @Override
    protected void onFirstViewAttach() {
        goToLibrary();
    }

    public NavigatorHolder getNavigatorHolder() {
        return navigatorHolder;
    }

    public void goToLibrary() {
        if (!isLibraryOpened) {
            router.newRootScreen(new Screens.LibraryScreen());
            isLibraryOpened = true;
            isExploreOpened = false;
            isAccountOpened = false;
        }
    }

    public void goToExplore() {
        if (!isExploreOpened) {
            router.newRootScreen(new Screens.ExploreScreen());
            isLibraryOpened = false;
            isExploreOpened = true;
            isAccountOpened = false;
        }
    }

    public void goToAccount() {
        if (!isAccountOpened) {
            router.newRootScreen(new Screens.AccountScreen());
            isLibraryOpened = false;
            isExploreOpened = false;
            isAccountOpened = true;
        }
    }
}
