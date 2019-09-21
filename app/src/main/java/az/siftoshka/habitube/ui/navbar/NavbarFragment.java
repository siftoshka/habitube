package az.siftoshka.habitube.ui.navbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.navbar.NavbarPresenter;
import az.siftoshka.habitube.presentation.navbar.NavbarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.android.support.SupportAppNavigator;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;

public class NavbarFragment extends MvpAppCompatFragment implements NavbarView {

    @InjectPresenter NavbarPresenter navbarPresenter;

    @BindView(R.id.navigation_bar) BottomNavigationView bottomNavigationView;

    private Navigator navigator;
    private Unbinder unbinder;

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
        final View view = inflater.inflate(R.layout.fragment_navbar, container, false);
        unbinder = ButterKnife.bind(this, view);
        initNavBar();
        return view;
    }

    private void initNavBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.isChecked()) return false;
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

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
