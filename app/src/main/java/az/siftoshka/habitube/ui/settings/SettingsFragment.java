package az.siftoshka.habitube.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
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

import static android.content.Context.MODE_PRIVATE;
import static az.siftoshka.habitube.Constants.DI.APP_SCOPE;
import static az.siftoshka.habitube.Constants.SYSTEM.DESIGNER_FREEPIK;
import static az.siftoshka.habitube.Constants.SYSTEM.DESIGNER_OKTAY;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_GITHUB;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_INSTAGRAM;
import static az.siftoshka.habitube.Constants.SYSTEM.DEV_TELEGRAM;
import static az.siftoshka.habitube.Constants.SYSTEM.FLATICON;

public class SettingsFragment extends MvpAppCompatFragment implements SettingsView, Toolbar.OnMenuItemClickListener {

    @InjectPresenter SettingsPresenter settingsPresenter;

    @BindView(R.id.telegram_contact) ImageView telegramButton;
    @BindView(R.id.github_contact) ImageView githubButton;
    @BindView(R.id.instagram_contact) ImageView instagramButton;
    @BindView(R.id.credits_oktay) TextView creditsOktay;
    @BindView(R.id.credits_freepik) TextView creditsFreepik;
    @BindView(R.id.theme_switcher) SwitchCompat themeSwithcer;
    @BindView(R.id.dark_mode_layout) LinearLayout darkModeCard;

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

        spannableCreditOktay();
        spannableCreditFreepik();

        checkAndroidVersion();

        SharedPreferences prefs = requireContext().getSharedPreferences("Dark-Mode", MODE_PRIVATE);
        int id = prefs.getInt("Dark", 0);

        if (id == 101) {
            themeSwithcer.setChecked(true);
        } else {
            themeSwithcer.setChecked(false);
        }

        themeSwithcer.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("Dark-Mode", MODE_PRIVATE).edit();
                editor.putInt("Dark", 101);
                editor.apply();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("Dark-Mode", MODE_PRIVATE).edit();
                editor.putInt("Dark", 100);
                editor.apply();
            }
        });
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

    private void spannableCreditOktay() {
        String cOktay = creditsOktay.getText().toString();
        SpannableString spannableStringOktay = new SpannableString(cOktay);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(DESIGNER_OKTAY));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableStringOktay.setSpan(clickableSpan,25,33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        creditsOktay.setText(spannableStringOktay);
        creditsOktay.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void spannableCreditFreepik() {
        String cFreepik = creditsFreepik.getText().toString();
        SpannableString spannableStringFreepik = new SpannableString(cFreepik);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(DESIGNER_FREEPIK));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(FLATICON));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableStringFreepik.setSpan(clickableSpan1,14,21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFreepik.setSpan(clickableSpan2,27,43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        creditsFreepik.setText(spannableStringFreepik);
        creditsFreepik.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void checkAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            darkModeCard.setVisibility(View.GONE);
        } else {
            darkModeCard.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
