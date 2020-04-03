package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.utils.ImageLoader;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowHolder> {

    public interface ShowItemClickListener {
        void onPostClicked(int showId);
    }

    public interface OnItemLongClickListener {
        void showPostName(String postName);
    }

    private List<MovieLite> shows;
    private ShowItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;


    public ShowAdapter(@NonNull ShowItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.shows = new ArrayList<>();
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ShowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new ShowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHolder holder, final int position) {
        final MovieLite shows = this.shows.get(position);
        ImageLoader.load(holder.itemView, shows.getMovieImage(), holder.posterImage);
        holder.posterRate.setText(String.valueOf(shows.getVoteAverage()));
        holder.itemView.setOnClickListener(v -> clickListener.onPostClicked(shows.getMovieId()));
        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.showPostName(shows.getShowTitle());
            return true;
        });
    }

    @Override
    public void onViewRecycled(@NonNull ShowHolder holder) {
        holder.posterRate.setText(null);
        holder.itemView.setOnClickListener(null);
        holder.itemView.setOnLongClickListener(null);
    }

    @Override
    public int getItemCount() {
        return shows.size();
    }

    public void addAllShows(List<MovieLite> shows) {
        this.shows.clear();
        this.shows.addAll(shows);
        notifyDataSetChanged();
    }

    public void showMoreShows(List<MovieLite> shows) {
        int position = this.shows.size();
        this.shows.addAll(shows);
        notifyItemRangeInserted(position, shows.size());
    }

    static class ShowHolder extends RecyclerView.ViewHolder {

        RelativeLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle, posterRate;

        ShowHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
            this.posterRate = itemView.findViewById(R.id.poster_rate);
        }
    }
}
