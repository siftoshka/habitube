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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int postId);
    }

    public interface OnItemLongClickListener {
        void showPostName(String postName);
    }

    private List<MovieLite> movies;
    private MovieItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public MovieAdapter(@NonNull MovieItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.movies = new ArrayList<>();
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, final int position) {
        final MovieLite movie = this.movies.get(position);
        ImageLoader.load(holder.itemView, movie.getMovieImage(), holder.posterImage);
        holder.posterRate.setText(String.valueOf(movie.getVoteAverage()));
        holder.itemView.setOnClickListener(view -> clickListener.onPostClicked(movie.getMovieId()));
        holder.itemView.setOnLongClickListener(view -> {
            longClickListener.showPostName(movie.getMovieTitle());
            return true;
        });
    }

    @Override
    public void onViewRecycled(@NonNull MovieHolder holder) {
        holder.posterRate.setText(null);
        holder.itemView.setOnClickListener(null);
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

    public void showMoreMovies(List<MovieLite> movies) {
        int position = this.movies.size();
        this.movies.addAll(movies);
        notifyItemRangeInserted(position, movies.size());
    }

    static class MovieHolder extends RecyclerView.ViewHolder {

        RelativeLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle, posterRate;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
            this.posterRate = itemView.findViewById(R.id.poster_rate);
        }
    }
}
