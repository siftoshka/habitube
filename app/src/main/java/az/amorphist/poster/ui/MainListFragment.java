package az.amorphist.poster.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.adapters.PostAdapter;
import az.amorphist.poster.entities.Post;
import az.amorphist.poster.presentation.views.MainListView;
import az.amorphist.poster.presentation.presenters.MainListPresenter;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;
import toothpick.Toothpick;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MainListFragment extends MvpAppCompatFragment implements MainListView {

    @Inject Context context;
    @InjectPresenter MainListPresenter mainListPresenter;

    RecyclerView recyclerView;
    TextInputEditText idText;
    Button addButton;

    private PostAdapter postAdapter;

    @ProvidePresenter
    MainListPresenter mainListPresenter() {
        return Toothpick.openScope("APP_SCOPE").getInstance(MainListPresenter.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postAdapter = new PostAdapter(new PostAdapter.ItemClickListener() {
            @Override
            public void onPostClicked(Post post) {
                mainListPresenter.goToDetailedScreen(String.valueOf(post.getPostId()));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        idText = view.findViewById(R.id.input_id_text);
        addButton = view.findViewById(R.id.add_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(postAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainListPresenter.addPost(Integer.parseInt(idText.getText().toString()));
                idText.setText(null);
                hideSoftKeyBoard();
            }
        });
    }

    @Override
    public void addPost(Post post) {
        postAdapter.addPost(post);
    }

    //Not using in Presenter
    @Override
    public void showPosts(List<Post> posts) {
        postAdapter.addAllPosts(posts);
    }

    @Override
    public void unsuccessfulQueryError() {
        Toast.makeText(context,"Unsuccessful request", Toast.LENGTH_SHORT).show();
    }

    private void hideSoftKeyBoard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(addButton.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
