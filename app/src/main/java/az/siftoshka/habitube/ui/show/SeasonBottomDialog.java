package az.siftoshka.habitube.ui.show;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.show.Season;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.DateConverter;
import az.siftoshka.habitube.utils.ImageLoader;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SeasonBottomDialog extends BottomSheetDialogFragment {

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.poster_season_post) ImageView seasonImage;
    @BindView(R.id.poster_season_name) TextView seasonName;
    @BindView(R.id.poster_season_air_date) TextView seasonDate;
    @BindView(R.id.poster_season_episode) TextView seasonEpisodes;
    @BindView(R.id.poster_season_overview) TextView seasonOverview;

    private Season season;
    private Unbinder unbinder;
    private DateChanger dateChanger = new DateChanger();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            season = bundle.getParcelable("SEASON");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_season_bottom, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        setDialog();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setDialog() {
        ImageLoader.load(getContext(), season.getPosterPath(), seasonImage);
        seasonName.setText(season.getName());
        seasonDate.setText(dateChanger.changeDate(season.getAirDate()));
        seasonEpisodes.setText(season.getEpisodeCount() + " " + getResources().getString(R.string.episodes));
        seasonOverview.setText(season.getOverview());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
