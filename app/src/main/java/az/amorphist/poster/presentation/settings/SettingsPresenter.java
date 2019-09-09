package az.amorphist.poster.presentation.settings;

import javax.inject.Inject;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    @Inject
    public SettingsPresenter() {

    }
}
