package az.siftoshka.habitube.presentation.explore.dialog;

import javax.inject.Inject;

import az.siftoshka.habitube.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class NetflixDialogPresenter extends MvpPresenter<NetflixDialogView> {

    private final Router router;

    @Inject
    public NetflixDialogPresenter(Router router) {
        this.router = router;
    }

    public void showNetflixPopular() {
        goToNetflixScreen(0);
    }

    public void showNetflixBest() {
        goToNetflixScreen(1);
    }

    public void showNetflixNew() {
        goToNetflixScreen(2);
    }

    private void goToNetflixScreen(int index) {
        router.navigateTo(new Screens.NetflixDiscoverScreen(index));
    }

    public void goBack() {
        router.exit();
    }
}
