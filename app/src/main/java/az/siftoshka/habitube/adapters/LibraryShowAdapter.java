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
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.utils.ImageLoader;

public class LibraryShowAdapter extends RecyclerView.Adapter<LibraryShowAdapter.LibraryHolder> {

    public interface ShowItemClickListener {
        void onPostClicked(int postId);
    }

    public interface OnItemLongClickListener {
        void showPost(Show show, int position);
    }

    private List<Show> shows;
    private ShowItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;


    public LibraryShowAdapter(@NonNull ShowItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.shows = new ArrayList<>();
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public LibraryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_search, viewGroup, false);
        return new LibraryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryHolder holder, final int position) {
        final Show show = this.shows.get(position);
        ImageLoader.loadLocally(holder.itemView, show.getPosterImage(), holder.posterImage);
        holder.posterRate.setText(String.valueOf(show.getVoteAverage()));
        holder.posterImage.setOnClickListener(v -> clickListener.onPostClicked(show.getId()));
        holder.posterImage.setOnLongClickListener(view -> {
            longClickListener.showPost(show, position);
            return true;
        });
    }

    @Override
    public void onViewRecycled(@NonNull LibraryHolder holder) {
        holder.posterRate.setText(null);
        holder.posterImage.setOnClickListener(null);
        holder.posterImage.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public void addAllShows(List<Show> shows) {
        this.shows.clear();
        this.shows.addAll(0, shows);
        notifyDataSetChanged();
    }

    public void dataChanged(int position) {
        this.shows.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.shows.size());
    }

    public Show getShowAt(int position) {
        Show show = this.shows.get(position);
        this.shows.remove(position);
        notifyItemRemoved(position);
        return show;
    }

    static class LibraryHolder extends RecyclerView.ViewHolder {

        ImageView posterImage;
        TextView posterRate;

        LibraryHolder(@NonNull View itemView) {
            super(itemView);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterRate = itemView.findViewById(R.id.poster_rate);
        }
    }
}
