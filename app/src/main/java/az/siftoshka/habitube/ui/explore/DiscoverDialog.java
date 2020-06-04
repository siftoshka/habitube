package az.siftoshka.habitube.ui.explore;

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
import com.xw.repo.BubbleSeekBar;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.presentation.explore.DiscoverDialogPresenter;
import az.siftoshka.habitube.presentation.explore.DiscoverDialogView;
import az.siftoshka.habitube.utils.moxy.MvpBottomSheetDialogFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

public class DiscoverDialog extends MvpBottomSheetDialogFragment implements DiscoverDialogView {

    @InjectPresenter DiscoverDialogPresenter discoverPresenter;

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @Nullable @BindView(R.id.sort_buttons) MaterialButtonToggleGroup toggleGroup;
    @Nullable @BindView(R.id.popularity) MaterialButton sortPopularity;
    @Nullable @BindView(R.id.revenue) MaterialButton sortRevenue;
    @BindView(R.id.slider_vote) RangeSlider sliderVote;
    @BindView(R.id.slider_year) RangeSlider sliderYear;
    @BindView(R.id.text_year) TextView textYear;
    @BindView(R.id.text_vote) TextView textVote;
    @BindView(R.id.discover) MaterialButton discoverButton;

    private Unbinder unbinder;
    private short index;
    private int voteIndexUp, voteIndexDown;
    private String yearIndexUp, yearIndexDown;
    private String sortSelection;

    @ProvidePresenter
    DiscoverDialogPresenter discoverPresenter() {
        return Toothpick.openScope(Constants.DI.APP_SCOPE).getInstance(DiscoverDialogPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getInt("DISCOVER") == 0)
                index = 0;
            else if (bundle.getInt("DISCOVER") == 1)
                index = 1;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (index == 0) view = inflater.inflate(R.layout.dialog_discover, container, false);
        else if (index == 1) view = inflater.inflate(R.layout.dialog_show_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDialog(index);
    }

    private void setDialog(int index) {
        if (index == 0) {
            defaultSorting();
            sortPopularity.addOnCheckedChangeListener((button, isChecked) -> {
                if (isChecked) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button.setTextColor(getResources().getColor(R.color.white_is_white));
                    sortRevenue.setBackgroundColor(getResources().getColor(R.color.background));
                    sortRevenue.setTextColor(getResources().getColor(R.color.dark_800));
                    sortSelection = "popularity.desc";
                }
            });
            sortRevenue.addOnCheckedChangeListener((button, isChecked) -> {
                if (isChecked) {
                    button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    button.setTextColor(getResources().getColor(R.color.white_is_white));
                    sortPopularity.setBackgroundColor(getResources().getColor(R.color.background));
                    sortPopularity.setTextColor(getResources().getColor(R.color.dark_800));
                    sortSelection = "revenue.desc";
                }
            });
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
            discoverButton.setOnClickListener(view -> discoverPresenter.discoverMovies(sortSelection,
                    yearIndexUp + "-01-01", yearIndexDown + "-01-01", voteIndexUp, voteIndexDown));
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
            discoverButton.setOnClickListener(view -> discoverPresenter.discoverShows(yearIndexUp + "-01-01",
                    yearIndexDown + "-01-01", voteIndexUp, voteIndexDown));
        }
    }

    private void defaultSorting() {
        sliderYear.setValues(2010.0f,2020.0f);
        sliderVote.setValues(4.0f, 10.0f);
        sortPopularity.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sortPopularity.setTextColor(getResources().getColor(R.color.white_is_white));
        sortRevenue.setBackgroundColor(getResources().getColor(R.color.background));
        sortRevenue.setTextColor(getResources().getColor(R.color.dark_800));
        sortSelection = "popularity.desc";
        textVote.setText(sliderVote.getValues().get(0).intValue() + " - " + sliderVote.getValues().get(1).intValue());
        textYear.setText(sliderYear.getValues().get(0).intValue() + " - " + sliderYear.getValues().get(1).intValue());
    }

    private void defaultShowSorting() {
        sliderYear.setValues(2010.0f,2020.0f);
        sliderVote.setValues(4.0f, 10.0f);
        textVote.setText(sliderVote.getValues().get(0).intValue() + " - " + sliderVote.getValues().get(1).intValue());
        textYear.setText(sliderYear.getValues().get(0).intValue() + " - " + sliderYear.getValues().get(1).intValue());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
