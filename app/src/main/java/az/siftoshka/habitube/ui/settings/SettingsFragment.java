package az.siftoshka.habitube.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.settings.SettingsPresenter;
import az.siftoshka.habitube.presentation.settings.SettingsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_GITHUB;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_INSTAGRAM;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_TELEGRAM;

public class SettingsFragment extends MvpAppCompatFragment implements SettingsView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter SettingsPresenter settingsPresenter;

    @BindView(R.id.telegram_contact) ImageView telegramButton;
    @BindView(R.id.github_contact) ImageView githubButton;
    @BindView(R.id.instagram_contact) ImageView instagramButton;
    @BindView(R.id.credits_designer) TextView creditsDesigner;


    private Unbinder unbinder;

    @ProvidePresenter
    SettingsPresenter accountPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(SettingsPresenter.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        telegramButton.setOnClickListener(v -> showTelegramPage());
        githubButton.setOnClickListener(v -> showGithubPage());
        instagramButton.setOnClickListener(v -> showInstagramPage());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.search_movies) {
            settingsPresenter.goToSearchScreen();
        }
        return false;
    }

    private void showTelegramPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(DEV_TELEGRAM));
        startActivity(intent);
    }

    private void showGithubPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(DEV_GITHUB));
        startActivity(intent);
    }

    private void showInstagramPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(DEV_INSTAGRAM));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}