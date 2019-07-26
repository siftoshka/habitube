package az.amorphist.poster.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import az.amorphist.poster.R;
import az.amorphist.poster.presentation.account.AccountPresenter;
import az.amorphist.poster.presentation.account.AccountView;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.di.DI.APP_SCOPE;

public class AccountFragment extends MvpAppCompatFragment implements AccountView{

    @InjectPresenter AccountPresenter accountPresenter;

    @ProvidePresenter
    AccountPresenter accountPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(AccountPresenter.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        return view;
    }
}
