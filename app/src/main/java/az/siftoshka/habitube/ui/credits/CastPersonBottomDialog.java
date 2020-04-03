package az.siftoshka.habitube.ui.credits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.Screens;
import az.siftoshka.habitube.adapters.CastAdapter;
import az.siftoshka.habitube.adapters.CastPersonAdapter;
import az.siftoshka.habitube.entities.personcredits.Cast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.terrakok.cicerone.Router;

public class CastPersonBottomDialog extends BottomSheetDialogFragment {

    @BindView(R.id.bottom_dialog_layout)
    LinearLayout linearLayout;
    @BindView(R.id.recycler_view_credits)
    RecyclerView recyclerViewCast;

    private final Router router;
    private List<Cast> cast;
    private CastPersonAdapter castAdapter;
    private Unbinder unbinder;

    public CastPersonBottomDialog(Router router) {
        this.router = router;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cast = bundle.getParcelableArrayList("MCAST");
            if (cast != null) {
                castAdapter = new CastPersonAdapter(id -> {
                    dismiss();
                    router.navigateTo(new Screens.PostMovieScreen(id));
                });
            } else {
                cast = bundle.getParcelableArrayList("SCAST");
                castAdapter = new CastPersonAdapter(id -> {
                    dismiss();
                    router.navigateTo(new Screens.PostShowScreen(id));
                });
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_credits, container, false);
        unbinder = ButterKnife.bind(this, view);
        linearLayout.setClipToOutline(true);
        setDialog();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GridLayoutManager layoutManagerCasts = new GridLayoutManager(getContext(), 2);
        recyclerViewCast.setLayoutManager(layoutManagerCasts);
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setHasFixedSize(true);
        recyclerViewCast.setAdapter(castAdapter);
    }

    private void setDialog() {
        castAdapter.addAllPersons(cast);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
