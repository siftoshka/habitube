package az.amorphist.poster.presentation.account;

import javax.inject.Inject;

import az.amorphist.poster.Screens;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AccountPresenter extends MvpPresenter<AccountView> {

    @Inject
    public AccountPresenter() {

    }
}
