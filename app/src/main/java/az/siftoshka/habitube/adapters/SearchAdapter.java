package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.utils.ImageLoader;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    public interface SearchItemClickListener {
        void onPostClicked(int id, int mediaType);
    }

    public interface OnItemLongClickListener {
        void showPostName(String postName);
    }

    private List<MovieLite> searchMedia;
    private SearchItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private int mediaState;

    public SearchAdapter(@NonNull SearchItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.searchMedia = new ArrayList<>();
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_search, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, final int position) {
        final MovieLite post = this.searchMedia.get(position);

        if (post.getMovieImage() == null) {
            ImageLoader.load(holder.itemView, post.getStarImage(), holder.posterImage);
        } else {
            ImageLoader.load(holder.itemView, post.getMovieImage(), holder.posterImage);
        }
        holder.posterRate.setText(String.valueOf(post.getVoteAverage()));
        holder.posterImage.setOnClickListener(v -> {
            switch (post.getMediaType()) {
                case "movie": mediaState = 1;break;
                case "tv": mediaState = 2;break;
                case "person": mediaState = 3;break;
            }
            clickListener.onPostClicked(post.getMovieId(), mediaState);
        });
        holder.posterImage.setOnLongClickListener(view -> {
            if(post.getMovieTitle() == null) {
                longClickListener.showPostName(post.getShowTitle());
            } else {
                longClickListener.showPostName(post.getMovieTitle());
            }
            return true;
        });
    }

    @Override
    public void onViewRecycled(@NonNull SearchHolder holder) {
        holder.posterRate.setText(null);
        holder.posterImage.setOnClickListener(null);
        holder.posterImage.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return searchMedia == null ? 0 : searchMedia.size();
    }

    public void addAllMedia(List<MovieLite> searchMedia) {
        this.searchMedia.clear();
        this.searchMedia.addAll(searchMedia);
        notifyDataSetChanged();
    }

    public void clean() {
        this.searchMedia.clear();
        notifyDataSetChanged();
    }

    static class SearchHolder extends RecyclerView.ViewHolder {

        ImageView posterImage;
        TextView posterRate;

        SearchHolder(@NonNull View itemView) {
            super(itemView);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterRate = itemView.findViewById(R.id.poster_rate);
        }
    }
}