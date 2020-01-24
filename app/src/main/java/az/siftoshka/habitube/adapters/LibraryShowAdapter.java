package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.entities.show.Show;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;

public class LibraryShowAdapter extends RecyclerView.Adapter<LibraryShowAdapter.LibraryHolder> {

    public interface ShowItemClickListener {
        void onPostClicked(int postId);
    }

    private List<Show> shows;
    private ShowItemClickListener clickListener;
    private DateChanger dateChanger;

    public LibraryShowAdapter(@NonNull ShowItemClickListener clickListener) {
        this.shows = new ArrayList<>();
        this.dateChanger = new DateChanger();
        this.clickListener = clickListener;
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
        holder.posterTitle.setText(show.getName());
        holder.posterDate.setText(dateChanger.changeDate(show.getFirstAirDate()));
        holder.posterLayout.setOnClickListener(v -> clickListener.onPostClicked(show.getId()));
    }

    @Override
    public void onViewRecycled(@NonNull LibraryHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterDate.setText(null);
        holder.posterLayout.setOnClickListener(null);
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

    public Show getShowAt(int position) {
        Show show = this.shows.get(position);
        this.shows.remove(position);
        notifyItemRemoved(position);
        return show;
    }

    static class LibraryHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle, posterDate;

        LibraryHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout_search);
            this.posterImage = itemView.findViewById(R.id.poster_image_search);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text_search);
            this.posterDate = itemView.findViewById(R.id.poster_main_date_search);
        }
    }
}
