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
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int postId);
    }

    public interface OnItemLongClickListener {
        void showPost(Movie movie, int position);
    }

    private List<Movie> movies;
    private MovieItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;


    public LibraryAdapter(@NonNull MovieItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.movies = new ArrayList<>();
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
        final Movie movie = this.movies.get(position);
        ImageLoader.loadLocally(holder.itemView, movie.getPosterImage(), holder.posterImage);
        holder.posterRate.setText(String.valueOf(movie.getVoteAverage()));
        holder.posterImage.setOnClickListener(v -> clickListener.onPostClicked(movie.getId()));
        holder.posterImage.setOnLongClickListener(view -> {
            longClickListener.showPost(movie, position);
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
        return movies.size();
    }

    public void addAllMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(0, movies);
        notifyDataSetChanged();
    }

    public void dataChanged(int position) {
        this.movies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.movies.size());
    }

    public Movie getMovieAt(int position) {
        Movie movie = this.movies.get(position);
        this.movies.remove(position);
        notifyItemRemoved(position);
        return movie;
    }

    public Movie getMovie(int position) {
        return this.movies.get(position);
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
