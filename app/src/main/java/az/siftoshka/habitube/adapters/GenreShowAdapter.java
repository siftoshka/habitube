package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.show.ShowGenre;

public class GenreShowAdapter extends RecyclerView.Adapter<GenreShowAdapter.GenresHolder> {

    public interface ItemClickListener {
        void onPostClicked(String id);
    }

    private List<ShowGenre> genres;
    private ItemClickListener clickListener;

    public GenreShowAdapter(@NonNull ItemClickListener clickListener) {
        this.genres = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GenresHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_genres, viewGroup, false);
        return new GenresHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresHolder holder, final int position) {
        final ShowGenre showGenre = this.genres.get(position);
        holder.posterTitle.setText(showGenre.getName());
        holder.posterTitle.setOnClickListener(view -> clickListener.onPostClicked(String.valueOf(showGenre.getId())));
    }

    @Override
    public void onViewRecycled(@NonNull GenresHolder holder) {
        holder.posterTitle.setText(null);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void addAllGenres(List<ShowGenre> showGenres) {
        this.genres.clear();
        this.genres.addAll(showGenres);
        notifyDataSetChanged();
    }

    static class GenresHolder extends RecyclerView.ViewHolder {
        TextView posterTitle;

        GenresHolder(@NonNull View itemView) {
            super(itemView);
            this.posterTitle = itemView.findViewById(R.id.poster_text);
        }
    }
}
