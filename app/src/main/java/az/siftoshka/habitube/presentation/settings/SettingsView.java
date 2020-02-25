package az.siftoshka.habitube.presentation.settings;

import com.google.firebase.auth.FirebaseUser;

import moxy.MvpView;

public interface SettingsView extends MvpView {

    void showGoogleSignIn();
    void showUser(FirebaseUser user);
}
