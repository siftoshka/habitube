package az.siftoshka.habitube.ui.library.dialogs;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.presentation.library.LibraryPlanningPresenter;
import az.siftoshka.habitube.presentation.library.LibraryWatchedPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OptionMenuDialog extends BottomSheetDialogFragment {

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.poster_post) ImageView image;
    @BindView(R.id.poster_title) TextView title;
    @BindView(R.id.remove_watched_button) MaterialButton removeWatchedButton;
    @BindView(R.id.remove_planning_button) MaterialButton removePLanningButton;

    private Movie movieW, movieP;
    private Show showW, showP;
    private int position;
    private Unbinder unbinder;
    private LibraryPlanningPresenter planningPresenter;
    private LibraryWatchedPresenter watchedPresenter;

    public OptionMenuDialog(LibraryPlanningPresenter planningPresenter, LibraryWatchedPresenter watchedPresenter) {
        this.planningPresenter = planningPresenter;
        this.watchedPresenter = watchedPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getParcelable("movie-library-planning") != null) {
                movieP = bundle.getParcelable("movie-library-planning");
                position = bundle.getInt("position");
            } else if(bundle.getParcelable("show-library-planning") != null) {
                showP = bundle.getParcelable("show-library-planning");
                position = bundle.getInt("position");
            } else if(bundle.getParcelable("movie-library-watched") != null) {
                movieW = bundle.getParcelable("movie-library-watched");
                position = bundle.getInt("position");
            } else if(bundle.getParcelable("show-library-watched") != null) {
                showW = bundle.getParcelable("show-library-watched");
                position = bundle.getInt("position");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_post, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        setDialog(view);
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void setDialog(View view) {
        if (movieP != null) {
            try {
                File f = new File(requireContext().getFilesDir().getPath() + movieP.getPosterPath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            title.setText(movieP.getTitle());
            removeWatchedButton.setVisibility(View.GONE);
            removePLanningButton.setVisibility(View.VISIBLE);
            removePLanningButton.setOnClickListener(view1 -> {planningPresenter.removeFromLocal(movieP, position);dismiss();});
        } else if (movieW != null) {
            try {
                File f = new File(requireContext().getFilesDir().getPath() + movieW.getPosterPath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            title.setText(movieW.getTitle());
            removeWatchedButton.setVisibility(View.VISIBLE);
            removePLanningButton.setVisibility(View.GONE);
            removeWatchedButton.setOnClickListener(view1 -> {watchedPresenter.removeFromLocal(movieW, position);dismiss();});
        } else if (showP != null) {
            try {
                File f = new File(requireContext().getFilesDir().getPath() + showP.getPosterPath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            title.setText(showP.getName());
            removeWatchedButton.setVisibility(View.GONE);
            removePLanningButton.setVisibility(View.VISIBLE);
            removePLanningButton.setOnClickListener(view1 -> {planningPresenter.removeFromLocal(showP, position);dismiss();});
        } else if (showW != null) {
            try {
                File f = new File(requireContext().getFilesDir().getPath() + showW.getPosterPath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                image.setImageBitmap(b);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            title.setText(showW.getName());
            removeWatchedButton.setVisibility(View.VISIBLE);
            removePLanningButton.setVisibility(View.GONE);
            removeWatchedButton.setOnClickListener(view1 -> {watchedPresenter.removeFromLocal(showW, position);dismiss();});
          }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
