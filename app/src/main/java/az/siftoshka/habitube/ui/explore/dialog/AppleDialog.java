package az.siftoshka.habitube.ui.explore.dialog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.model.system.MessageListener;
import az.siftoshka.habitube.presentation.explore.dialog.apple.AppleDialogPresenter;
import az.siftoshka.habitube.presentation.explore.dialog.apple.AppleDialogView;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class AppleDialog extends MvpBottomSheetDialogFragment implements AppleDialogView {

    @InjectPresenter AppleDialogPresenter discoverPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.popular) MaterialButton popularButton;
    @BindView(R.id.rating) MaterialButton bestButton;
    @BindView(R.id.date) MaterialButton newButton;

    private Unbinder unbinder;
    private MessageListener messageListener;

    @ProvidePresenter
    AppleDialogPresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(AppleDialogPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MessageListener) this.messageListener = (MessageListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_media, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDialog();
    }

    private void setDialog() {
        popularButton.setOnClickListener(view -> showApplePopular());
        bestButton.setOnClickListener(view -> showAppleBest());
        newButton.setOnClickListener(view -> showAppleNew());
    }

    private void showApplePopular() {
        if (haveNetworkConnection()) {
            discoverPresenter.showApplePopular();
            dismiss();
        }
        else messageListener.showInternetError(getResources().getString(R.string.error_text_body));
    }

    private void showAppleBest() {
        if (haveNetworkConnection()) {
            discoverPresenter.showAppleBest();
            dismiss();
        }
        else messageListener.showInternetError(getResources().getString(R.string.error_text_body));
    }

    private void showAppleNew() {
        if (haveNetworkConnection()) {
            discoverPresenter.showAppleNew();
            dismiss();
        }
        else messageListener.showInternetError(getResources().getString(R.string.error_text_body));
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm != null ? cm.getAllNetworkInfo() : new NetworkInfo[0];
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
