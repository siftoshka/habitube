package az.siftoshka.habitube.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.model.system.MessageListener;
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
    @BindView(R.id.theme_switcher) SwitchCompat themeSwither;
    @BindView(R.id.adult_switcher) SwitchCompat adultSwitcher;
    @BindView(R.id.dark_mode_layout) LinearLayout darkModeCard;
    @BindView(R.id.radio_sort) RadioGroup radioSorting;
    @BindView(R.id.radio_recent) RadioButton radioRecent;
    @BindView(R.id.radio_name) RadioButton radioName;
    @BindView(R.id.radio_year) RadioButton radioYear;
    @BindView(R.id.google_auth) MaterialButton googleAuthButton;
    @BindView(R.id.sign_out_layout) LinearLayout userLayout;
    @BindView(R.id.user_text) TextView userText;
    @BindView(R.id.warning_text) TextView warningText;
    @BindView(R.id.sign_out) ImageView signOutButton;

    private Unbinder unbinder;
    private MessageListener messageListener;
    private GoogleSignInClient signInClient;
    private FirebaseAuth firebaseAuth;

    @ProvidePresenter
    SettingsPresenter accountPresenter() {
        return Toothpick.openScope(APP_SCOPE).getInstance(SettingsPresenter.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        checkTheme();
        checkAdult();
        checkSort();

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(requireActivity(), gso);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        telegramButton.setOnClickListener(v -> showTelegramPage());
        githubButton.setOnClickListener(v -> showGithubPage());
        instagramButton.setOnClickListener(v -> showInstagramPage());
        googleAuthButton.setOnClickListener(view1 -> signIn());
        signOutButton.setOnClickListener(view1 -> {
            firebaseAuth.signOut();
            signInClient.signOut().addOnCompleteListener(runnable -> showGoogleSignIn()); });

        spannableCreditOktay();
        spannableCreditFreepik();
        checkDarkModeVisibility();
        radioListener();

        themeSwither.setOnCheckedChangeListener((compoundButton, b) -> {
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
        adultSwitcher.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("Adult-Mode", MODE_PRIVATE).edit();
                editor.putInt("Adult", 301);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = requireContext().getSharedPreferences("Adult-Mode", MODE_PRIVATE).edit();
                editor.putInt("Adult", 300);
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

    private void radioListener() {
        radioRecent.setOnClickListener(view -> {
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE).edit();
            editor.putInt("Radio", 200);
            editor.apply();
        });
        radioName.setOnClickListener(view -> {
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE).edit();
            editor.putInt("Radio", 201);
            editor.apply();
        });
        radioYear.setOnClickListener(view -> {
            SharedPreferences.Editor editor = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE).edit();
            editor.putInt("Radio", 202);
            editor.apply();
        });
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
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(DESIGNER_OKTAY));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
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
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(DESIGNER_FREEPIK));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(FLATICON));
                startActivity(intent);
            }

            @NonNull
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        spannableStringFreepik.setSpan(clickableSpan1,14,21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringFreepik.setSpan(clickableSpan2,27,43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        creditsFreepik.setText(spannableStringFreepik);
        creditsFreepik.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void checkDarkModeVisibility() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            darkModeCard.setVisibility(View.GONE);
        } else {
            darkModeCard.setVisibility(View.VISIBLE);
        }
    }

    private void checkTheme() {
        SharedPreferences prefs = requireContext().getSharedPreferences("Dark-Mode", MODE_PRIVATE);
        int idTheme = prefs.getInt("Dark", 0);

        if (idTheme == 101) {
            themeSwither.setChecked(true);
        } else {
            themeSwither.setChecked(false);
        }
    }

    private void checkAdult() {
        SharedPreferences prefs = requireContext().getSharedPreferences("Adult-Mode", MODE_PRIVATE);
        int idAdult = prefs.getInt("Adult", 0);

        if(idAdult == 301) {
            adultSwitcher.setChecked(true);
        } else {
            adultSwitcher.setChecked(false);
        }
    }

    private void checkSort() {
        SharedPreferences prefsRadio = requireContext().getSharedPreferences("Radio-Sort", MODE_PRIVATE);
        int idRadio = prefsRadio.getInt("Radio", 0);
        switch (idRadio) {
            case 200:
                radioRecent.setChecked(true);
                break;
            case 201:
                radioName.setChecked(true);
                break;
            case 202:
                radioYear.setChecked(true);
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handlingSignInResult(accountTask);
        }
    }

    private void handlingSignInResult(Task<GoogleSignInAccount> accountTask) {
        try {
            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
            messageListener.showInternetError("Sign In Successfully");
            if (signInAccount != null) firebaseGoogleAuth(signInAccount);
        } catch (ApiException e) {
            messageListener.showInternetError("FAIL");
        }
    }

    private void firebaseGoogleAuth(GoogleSignInAccount signInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateUI(user);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            userLayout.setVisibility(View.VISIBLE);
            googleAuthButton.setVisibility(View.GONE);
            warningText.setVisibility(View.GONE);
            userText.setText(user.getDisplayName() + " (" + user.getEmail() + ")");
        }
    }

    @Override
    public void showGoogleSignIn() {
        userLayout.setVisibility(View.GONE);
        googleAuthButton.setVisibility(View.VISIBLE);
        warningText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUser(FirebaseUser user) {
        updateUI(user);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
