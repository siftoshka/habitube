package az.amorphist.poster.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.movielite.MovieLite;

import static az.amorphist.poster.App.IMAGE_URL;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    public interface SearchItemClickListener {
        void onPostClicked(int id, int mediaType);
    }

    private List<MovieLite> searchMedia;
    private SearchItemClickListener clickListener;
    private int mediaState;

    public SearchAdapter(@NonNull SearchItemClickListener clickListener) {
        this.searchMedia = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_search, viewGroup, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, final int position) {
        final MovieLite post = this.searchMedia.get(position);
        if (post.getMovieImage() == null) {
            Glide.with(holder.itemView)
                    .load(IMAGE_URL + post.getStarImage())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_poster_name)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(holder.posterImage);
        } else {
            Glide.with(holder.itemView)
                    .load(IMAGE_URL + post.getMovieImage())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.ic_poster_name)
                    .transform(new CenterCrop(), new RoundedCorners(16))
                    .into(holder.posterImage);
        }

        if (post.getMovieTitle() == null) {
            holder.posterTitle.setText(post.getShowTitle());
        } else {
            holder.posterTitle.setText(post.getMovieTitle());
        }
        holder.posterLayout.setOnClickListener(v -> {
            switch (post.getMediaType()) {
                case "movie": mediaState = 1; break;
                case "tv": mediaState = 2; break;
                case "person": mediaState = 3; break;
            }
            clickListener.onPostClicked(post.getMovieId(), mediaState);
        });
    }

    @Override
    public void onViewRecycled(@NonNull SearchHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterLayout.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return searchMedia.size();
    }

    public void addAllMedia(List<MovieLite> searchMedia) {
        this.searchMedia.clear();
        this.searchMedia.addAll(searchMedia);
        notifyDataSetChanged();
    }

    static class SearchHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle;

        SearchHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout_search);
            this.posterImage = itemView.findViewById(R.id.poster_image_search);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text_search);
        }
    }
}