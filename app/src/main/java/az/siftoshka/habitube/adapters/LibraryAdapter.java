package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movie.Movie;
import az.siftoshka.habitube.utils.DateChanger;
import az.siftoshka.habitube.utils.ImageLoader;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int postId);
    }

    private List<Movie> movies;
    private MovieItemClickListener clickListener;
    private DateChanger dateChanger;

    public LibraryAdapter(@NonNull MovieItemClickListener clickListener) {
        this.movies = new ArrayList<>();
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
        final Movie movie = this.movies.get(position);
        ImageLoader.loadLocally(holder.itemView, movie.getPosterImage(), holder.posterImage);
        holder.posterTitle.setText(movie.getTitle());
        holder.posterDate.setText(dateChanger.changeDate(movie.getReleaseDate()));
        holder.posterLayout.setOnClickListener(v -> clickListener.onPostClicked(movie.getId()));
    }

    @Override
    public void onViewRecycled(@NonNull LibraryHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterDate.setText(null);
        holder.posterLayout.setOnClickListener(null);
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
