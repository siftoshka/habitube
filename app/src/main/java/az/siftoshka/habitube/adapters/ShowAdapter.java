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
import az.siftoshka.habitube.entities.movielite.MovieLite;
import az.siftoshka.habitube.utils.ImageLoader;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowHolder> {

    public interface ShowItemClickListener {
        void onPostClicked(int showId);
    }

    private List<MovieLite> movies;
    private ShowItemClickListener clickListener;

    public ShowAdapter(@NonNull ShowItemClickListener clickListener) {
        this.movies = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ShowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new ShowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowHolder holder, final int position) {
        final MovieLite movie = this.movies.get(position);
        ImageLoader.load(holder.itemView, movie.getMovieImage(), holder.posterImage);
        holder.posterTitle.setText(movie.getShowTitle());
        holder.posterRate.setText(String.valueOf(movie.getVoteAverage()));
        holder.posterLayout.setOnClickListener(v -> clickListener.onPostClicked(movie.getMovieId()));
    }

    @Override
    public void onViewRecycled(@NonNull ShowHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterRate.setText(null);
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

    static class ShowHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
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
