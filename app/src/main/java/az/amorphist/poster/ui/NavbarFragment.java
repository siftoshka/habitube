package az.amorphist.poster.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.Screens;
import az.amorphist.poster.presentation.navbar.NavbarPresenter;
import az.amorphist.poster.presentation.navbar.NavbarView;
import az.amorphist.poster.utils.navigation.LocalRouter;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

import static az.amorphist.poster.di.DI.APP_SCOPE;

public class NavbarFragment extends MvpAppCompatFragment implements NavbarView {

    @InjectPresenter NavbarPresenter navbarPresenter;

    private Navigator navigator;
    private BottomNavigationView bottomNavigationView;

    @ProvidePresenter
    NavbarPresenter navbarPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(NavbarPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar, container, false);
        bottomNavigationView = view.findViewById(R.id.navigation_bar);
        initNavBar();
        return view;
    }

    private void initNavBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.nav_library:
                    navbarPresenter.goToLibrary();
                    break;
                case R.id.nav_explore:
                    navbarPresenter.goToExplore();
                    break;
                case R.id.nav_account:
                    navbarPresenter.goToAccount();
                    break;
            }
            return true;
        });
    }

    private Navigator getNavigator() {
        if (navigator == null) {
            navigator = new SupportAppNavigator(getActivity(), getChildFragmentManager(), R.id.fragment_main_container);
        }
        return navigator;
    }

    @Override
    public void onResume() {
        super.onResume();
        navbarPresenter.getNavigatorHolder().setNavigator(getNavigator());
    }

    @Override
    public void onPause() {
        super.onPause();
        navbarPresenter.getNavigatorHolder().removeNavigator();
    }
}
