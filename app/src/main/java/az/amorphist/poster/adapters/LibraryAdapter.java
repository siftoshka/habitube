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

import javax.inject.Inject;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.movie.Movie;
import az.amorphist.poster.entities.movielite.MovieLite;
import az.amorphist.poster.utils.GlideLoader;
import toothpick.Toothpick;

import static az.amorphist.poster.App.IMAGE_URL;
import static az.amorphist.poster.di.DI.APP_SCOPE;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryHolder> {

    public interface MovieItemClickListener {
        void onPostClicked(int postId);
    }

    private List<Movie> movies;
    private MovieItemClickListener clickListener;

    public LibraryAdapter(@NonNull MovieItemClickListener clickListener) {
        this.movies = new ArrayList<>();
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
        GlideLoader.load(holder.itemView, movie.getPosterPath(), holder.posterImage);
        holder.posterTitle.setText(movie.getTitle());
        holder.posterLayout.setOnClickListener(v -> clickListener.onPostClicked(movie.getId()));
    }

    @Override
    public void onViewRecycled(@NonNull LibraryHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterLayout.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void addAllMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    static class LibraryHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle;

        LibraryHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
        }
    }
}
