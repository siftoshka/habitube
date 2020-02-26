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
import az.siftoshka.habitube.adapters.CrewAdapter;
import az.siftoshka.habitube.adapters.CrewPersonAdapter;
import az.siftoshka.habitube.entities.personcredits.Crew;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.terrakok.cicerone.Router;

public class CrewPersonBottomDialog extends BottomSheetDialogFragment {

    @BindView(R.id.bottom_dialog_layout) LinearLayout linearLayout;
    @BindView(R.id.recycler_view_credits) RecyclerView recyclerViewCrew;

    private final Router router;
    private List<Crew> crew;
    private CrewPersonAdapter crewAdapter;
    private Unbinder unbinder;

    public CrewPersonBottomDialog(Router router) {
        this.router = router;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            crew = bundle.getParcelableArrayList("CREW");
        }
        crewAdapter = new CrewPersonAdapter(id -> {
            dismiss();
            router.navigateTo(new Screens.SearchItemScreen(id, 3));});
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
        GridLayoutManager layoutManagerCrew = new GridLayoutManager(getContext(), 2);
        recyclerViewCrew.setLayoutManager(layoutManagerCrew);
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setHasFixedSize(true);
        recyclerViewCrew.setAdapter(crewAdapter);
    }

    private void setDialog() {
        crewAdapter.addAllPersons(crew);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
