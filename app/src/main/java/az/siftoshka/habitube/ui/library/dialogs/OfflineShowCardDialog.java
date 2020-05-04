package az.siftoshka.habitube.ui.library.dialogs;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.utils.DateChanger;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OfflineShowCardDialog extends DialogFragment {

    @BindView(R.id.poster_movie_post) ImageView posterMain;
    @BindView(R.id.poster_title) TextView posterTitle;
    @BindView(R.id.poster_date) TextView posterDate;
    @BindView(R.id.poster_last_date) TextView posterLastDate;
    @BindView(R.id.poster_rate) TextView posterRate;
    @BindView(R.id.poster_views) TextView posterViews;
    @BindView(R.id.poster_duration) TextView posterDuration;
    @BindView(R.id.poster_desc) TextView posterDesc;
    @BindView(R.id.close_button) MaterialButton closeButton;

    private Show show;
    private Unbinder unbinder;
    private DateChanger dateChanger = new DateChanger();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            show = bundle.getParcelable("Shows");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_show_offline, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setDialog();

        closeButton.setOnClickListener(view1 -> dismiss());
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setDialog() {
        try {
            File f = new File(requireContext().getFilesDir().getPath() + show.getPosterPath());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            posterMain.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        posterTitle.setText(show.getName());
        posterDate.setText(dateChanger.changeDate(show.getFirstAirDate()));
        posterLastDate.setText(dateChanger.changeDate(show.getLastAirDate()));
        posterRate.setText(String.valueOf(show.getVoteAverage()));
        posterViews.setText("(" + show.getVoteCount() + ")");
        posterDuration.setText(show.getNumberOfEpisodes() + " " + getResources().getString(R.string.episodes));
        posterDesc.setText(show.getOverview());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
