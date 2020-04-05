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

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int id);
    }

    private List<MovieLite> movies;
    private MovieItemClickListener clickListener;

    public DiscoverAdapter(@NonNull MovieItemClickListener clickListener) {
        this.movies = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public DiscoverHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_discover, viewGroup, false);
        return new DiscoverHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverHolder holder, final int position) {
        final MovieLite movie = this.movies.get(position);
        if (movie.getMovieTitle() != null) {
            holder.posterTitle.setText(movie.getMovieTitle());
        } else {
            holder.posterTitle.setText(movie.getShowTitle());
        }
        ImageLoader.loadBackground(holder.itemView, movie.getBackdropPath(), holder.posterImage);
        holder.posterImage.setOnClickListener(v -> clickListener.onPostClicked(movie.getMovieId()));
    }

    @Override
    public void onViewRecycled(@NonNull DiscoverHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterImage.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addAllMovies(List<MovieLite> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMoreContent(List<MovieLite> media) {
        int position = this.movies.size();
        this.movies.addAll(media);
        notifyItemRangeInserted(position, media.size());
    }


    static class DiscoverHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView posterTitle;

        DiscoverHolder(@NonNull View itemView) {
            super(itemView);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
        }
    }
}
