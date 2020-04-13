package az.siftoshka.habitube.ui.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.explore.NetflixDialogPresenter;
import az.siftoshka.habitube.presentation.explore.NetflixDialogView;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class NetflixDialog extends MvpBottomSheetDialogFragment implements NetflixDialogView {

    @InjectPresenter NetflixDialogPresenter discoverPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.popular) MaterialButton popularButton;
    @BindView(R.id.rating) MaterialButton bestButton;
    @BindView(R.id.date) MaterialButton newButton;

    private Unbinder unbinder;

    @ProvidePresenter
    NetflixDialogPresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(NetflixDialogPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_netflix, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDialog();
    }

    private void setDialog() {
        popularButton.setOnClickListener(view -> discoverPresenter.showNetflixPopular());
        bestButton.setOnClickListener(view -> discoverPresenter.showNetflixBest());
        newButton.setOnClickListener(view -> discoverPresenter.showNetflixNew());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
