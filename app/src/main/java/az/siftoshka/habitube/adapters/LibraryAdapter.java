package az.siftoshka.habitube.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.movie.Movie;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int postId);
    }

    public interface OnItemLongClickListener {
        void showPost(Movie movie, int position);
    }

    private List<Movie> movies;
    private final Context context;
    private MovieItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;


    public LibraryAdapter(Context context, @NonNull MovieItemClickListener clickListener, @NonNull OnItemLongClickListener longClickListener) {
        this.movies = new ArrayList<>();
        this.context = context;
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
        Glide.with(holder.itemView)
                .load(new File(context.getFilesDir().getPath()) + movie.getPosterPath())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.posterImage);
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

    public boolean addAllMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(0, movies);
        notifyDataSetChanged();
        return true;
    }

    public void removedItem(int position) {
        this.movies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.movies.size());
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
