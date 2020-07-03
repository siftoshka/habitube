package az.siftoshka.habitube.ui.explore.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.RangeSlider;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.explore.dialog.DiscoverDialogPresenter;
import az.siftoshka.habitube.presentation.explore.dialog.DiscoverDialogView;
import az.siftoshka.habitube.presentation.explore.dialog.GenresDialogPresenter;
import az.siftoshka.habitube.presentation.explore.dialog.GenresDialogView;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class GenresDialog extends MvpBottomSheetDialogFragment implements GenresDialogView {

    @InjectPresenter GenresDialogPresenter genresDialogPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.slider_vote) RangeSlider sliderVote;
    @BindView(R.id.slider_year) RangeSlider sliderYear;
    @BindView(R.id.text_year) TextView textYear;
    @BindView(R.id.text_vote) TextView textVote;
    @BindView(R.id.discover) MaterialButton discoverButton;

    private Unbinder unbinder;
    private short index;
    private int voteIndexUp, voteIndexDown;
    private String yearIndexUp, yearIndexDown, genreId;

    @ProvidePresenter
    GenresDialogPresenter genresDialogPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(GenresDialogPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            genreId = bundle.getString("DISCOVER");
            if (bundle.getInt("DISCOVER-ID") == 0) index = 0;
            else if (bundle.getInt("DISCOVER-ID") == 1) index = 1;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.dialog_genres, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDialog(index);
    }

    @SuppressLint("SetTextI18n")
    private void setDialog(int index) {
        if (index == 0) {
            defaultSorting();
            sliderYear.addOnChangeListener((slider1, value, fromUser) -> {
                yearIndexUp = slider1.getValues().get(0).toString();
                yearIndexUp = yearIndexUp.substring(0, yearIndexUp.length() - 2);
                yearIndexDown = slider1.getValues().get(1).toString();
                yearIndexDown = yearIndexDown.substring(0, yearIndexDown.length() - 2);
                textYear.setText(yearIndexUp + " - " + yearIndexDown);
            });
            sliderVote.addOnChangeListener((slider1, value, fromUser) -> {
                voteIndexUp = slider1.getValues().get(0).intValue();
                voteIndexDown = slider1.getValues().get(1).intValue();
                textVote.setText(voteIndexUp + " - " + voteIndexDown);
            });
            discoverButton.setOnClickListener(view -> genresDialogPresenter.goToDiscoverScreen(yearIndexUp + "-01-01", yearIndexDown + "-01-01", voteIndexUp, voteIndexDown, genreId));
        }

        if (index == 1) {
            defaultShowSorting();
            sliderYear.addOnChangeListener((slider1, value, fromUser) -> {
                yearIndexUp = slider1.getValues().get(0).toString();
                yearIndexUp = yearIndexUp.substring(0, yearIndexUp.length() - 2);
                yearIndexDown = slider1.getValues().get(1).toString();
                yearIndexDown = yearIndexDown.substring(0, yearIndexDown.length() - 2);
                textYear.setText(yearIndexUp + " - " + yearIndexDown);
            });
            sliderVote.addOnChangeListener((slider1, value, fromUser) -> {
                voteIndexUp = slider1.getValues().get(0).intValue();
                voteIndexDown = slider1.getValues().get(1).intValue();
                textVote.setText(voteIndexUp + " - " + voteIndexDown);
            });
            discoverButton.setOnClickListener(view -> genresDialogPresenter.goToDiscoverShowScreen(yearIndexUp + "-01-01",
                    yearIndexDown + "-01-01", voteIndexUp, voteIndexDown, genreId));
        }
    }

    @SuppressLint("SetTextI18n")
    private void defaultSorting() {
        sliderYear.setValues(2010.0f,2020.0f);
        sliderVote.setValues(4.0f, 10.0f);
        yearIndexUp = String.valueOf(sliderYear.getValues().get(0).intValue());
        yearIndexDown = String.valueOf(sliderYear.getValues().get(1).intValue());
        voteIndexUp = sliderVote.getValues().get(0).intValue();
        voteIndexDown = sliderVote.getValues().get(1).intValue();
        textVote.setText(sliderVote.getValues().get(0).intValue() + " - " + sliderVote.getValues().get(1).intValue());
        textYear.setText(sliderYear.getValues().get(0).intValue() + " - " + sliderYear.getValues().get(1).intValue());
    }

    @SuppressLint("SetTextI18n")
    private void defaultShowSorting() {
        sliderYear.setValues(2010.0f,2020.0f);
        sliderVote.setValues(4.0f, 10.0f);
        yearIndexUp = String.valueOf(sliderYear.getValues().get(0).intValue());
        yearIndexDown = String.valueOf(sliderYear.getValues().get(1).intValue());
        voteIndexUp = sliderVote.getValues().get(0).intValue();
        voteIndexDown = sliderVote.getValues().get(1).intValue();
        textVote.setText(sliderVote.getValues().get(0).intValue() + " - " + sliderVote.getValues().get(1).intValue());
        textYear.setText(sliderYear.getValues().get(0).intValue() + " - " + sliderYear.getValues().get(1).intValue());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
