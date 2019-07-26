package az.amorphist.poster.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.show.Season;
import az.amorphist.poster.presentation.season.SeasonPresenter;
import az.amorphist.poster.presentation.season.SeasonView;
import az.amorphist.poster.utils.moxy.MvpBottomSheetDialogFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static az.amorphist.poster.App.IMAGE_URL;
import static az.amorphist.poster.di.DI.APP_SCOPE;

public class SeasonBottomDialog extends MvpBottomSheetDialogFragment implements SeasonView {

    @InjectPresenter SeasonPresenter seasonPresenter;

    private Season season;
    private LinearLayout linearLayout;
    private ImageView seasonImage;
    private TextView seasonName, seasonDate, seasonEpisodes, seasonOverview;

    @ProvidePresenter
    SeasonPresenter seasonPresenter() {
       return Toothpick.openScope(APP_SCOPE).getInstance(SeasonPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            season = bundle.getParcelable("SEASON");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_season_bottom, container, false);
        seasonImage = view.findViewById(R.id.poster_season_post);
        seasonName = view.findViewById(R.id.poster_season_name);
        seasonDate = view.findViewById(R.id.poster_season_air_date);
        seasonEpisodes = view.findViewById(R.id.poster_season_episode);
        seasonOverview = view.findViewById(R.id.poster_season_overview);
        linearLayout = view.findViewById(R.id.bottom_dialog_layout);
        linearLayout.setClipToOutline(true);
        setDialog();
        return view;
    }

    public void setDialog() {
        Glide.with(getContext())
                .load(IMAGE_URL + season.getPosterPath())
                .transition(new DrawableTransitionOptions().crossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_poster_name)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(seasonImage);
        seasonName.setText(season.getName());
        seasonDate.setText(season.getAirDate());
        seasonEpisodes.setText(String.valueOf(season.getEpisodeCount()));
        seasonOverview.setText(season.getOverview());
    }
}
