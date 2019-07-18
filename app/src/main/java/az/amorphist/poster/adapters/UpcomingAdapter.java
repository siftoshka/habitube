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

import java.util.ArrayList;
import java.util.List;

import az.amorphist.poster.R;
import az.amorphist.poster.entities.movielite.MovieLite;

import static az.amorphist.poster.App.IMAGE_URL;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingHolder> {

    public interface UpcomingItemClickListener {
        void onPostClicked(int position);
    }

    private List<MovieLite> movies;
    private UpcomingItemClickListener clickListener;

    public UpcomingAdapter(@NonNull UpcomingItemClickListener clickListener) {
        this.movies = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UpcomingHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new UpcomingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingHolder holder, final int position) {
        final MovieLite movie = this.movies.get(position);
        Glide.with(holder.itemView)
                .load(IMAGE_URL + movie.getMovieImage())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.progress_animation)
                .into(holder.posterImage);
        holder.posterTitle.setText(movie.getMovieTitle());
        holder.posterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onPostClicked(position);
            }
        });

    }

    @Override
    public void onViewRecycled(@NonNull UpcomingHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterLayout.setOnClickListener(null);
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

    static class UpcomingHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle;

        UpcomingHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
        }
    }
}
