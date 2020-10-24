package az.siftoshka.habitube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import javax.inject.Inject;

import az.siftoshka.habitube.model.system.KeyboardBehavior;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.utils.navigation.SupremeNavigator;
import moxy.MvpAppCompatActivity;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class MainActivity extends MvpAppCompatActivity implements MessageListener, KeyboardBehavior {

    @Inject NavigatorHolder navigatorHolder;
    @Inject Router router;

    private final Navigator navigator = new SupremeNavigator(this, R.id.fragment_container) {
        @Override
        protected void setupFragmentTransaction(@NonNull Command command, Fragment currentFragment, Fragment nextFragment, @NonNull FragmentTransaction fragmentTransaction) {
            if (command instanceof Forward)
                fragmentTransaction.setCustomAnimations(R.animator.slide_out_right, R.animator.slide_in_left);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toothpick.inject(this, Toothpick.openScope(APP_SCOPE));
        router.newRootScreen(new Screens.NavbarScreen());
        SharedPreferences prefs = getSharedPreferences("Dark-Mode", MODE_PRIVATE);
        int idTheme = prefs.getInt("Dark", 0);
        if (idTheme == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                getWindow().setNavigationBarColor(getResources().getColor(R.color.mainBackground));
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    public void showInternetError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showText(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void hideKeyboard() {
        final View view = findViewById(R.id.fragment_container);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        navigatorHolder.removeNavigator();
    }
}
