package az.amorphist.poster.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    public interface ItemClickListener {
        void onPostClicked(Post post);
    }

    private List<Post> posts;
    private ItemClickListener clickListener;

    public PostAdapter(@NonNull ItemClickListener clickListener) {
        this.posts = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        final Post post = this.posts.get(position);
        holder.titleText.setText(post.getPostTitle());
        holder.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onPostClicked(post);
            }
        });

    }

    @Override
    public void onViewRecycled(@NonNull PostHolder holder) {
        holder.titleText.setText(null);
        holder.titleText.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAllPosts(List<Post> posts) {
        this.posts.clear();
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    public void addPost(Post note) {
        this.posts.add(note);
        notifyItemInserted(getItemCount());
    }

    static class PostHolder extends RecyclerView.ViewHolder {

        TextView titleText;

        PostHolder(@NonNull View itemView) {
            super(itemView);
            this.titleText = (TextView) itemView;
        }
    }
}
