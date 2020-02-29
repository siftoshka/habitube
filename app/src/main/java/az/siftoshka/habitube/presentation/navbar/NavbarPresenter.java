package az.siftoshka.habitube.presentation.navbar;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    private final Context context;
    private final NavigatorHolder navigatorHolder;

    @Inject
    public NavbarPresenter(LocalRouter router, Context context) {
        Cicerone<Router> cicerone = router.getCicerone("NAVBAR");
        this.router = cicerone.getRouter();
        this.context = context;
        this.navigatorHolder = cicerone.getNavigatorHolder();
    }

    @Override
    protected void onFirstViewAttach() {
        if (!haveNetworkConnection()) {
            goToLibrary();
        } else {
            goToExplore();
        }
    }

    public NavigatorHolder getNavigatorHolder() {
        return navigatorHolder;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm != null ? cm.getAllNetworkInfo() : new NetworkInfo[0];
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void goToLibrary() { router.newRootScreen(new Screens.LibraryScreen()); }

    public void goToExplore() { router.newRootScreen(new Screens.ExploreScreen()); }

    public void goToAccount() { router.newRootScreen(new Screens.AccountScreen()); }
}
