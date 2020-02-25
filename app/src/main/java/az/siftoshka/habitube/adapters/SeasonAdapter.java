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
import az.siftoshka.habitube.entities.show.Season;
import az.siftoshka.habitube.utils.ImageLoader;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonHolder> {

    public interface ShowItemClickListener {
        void onPostClicked(int position);
    }

    private List<Season> seasons;
    private ShowItemClickListener clickListener;

    public SeasonAdapter(@NonNull ShowItemClickListener clickListener) {
        this.seasons = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public SeasonHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_alt, viewGroup, false);
        return new SeasonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonHolder holder, final int position) {
        final Season season = this.seasons.get(position);
        ImageLoader.load(holder.itemView, season.getPosterPath(), holder.posterImage);
        holder.posterTitle.setText(season.getName());

        holder.posterLayout.setOnClickListener(v ->
                clickListener.onPostClicked(position));
    }

    @Override
    public void onViewRecycled(@NonNull SeasonHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterLayout.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return seasons.size();
    }

    public void addAllMovies(List<Season> seasons) {
        this.seasons.clear();
        this.seasons.addAll(seasons);
        notifyDataSetChanged();
    }

    public Season getSeason(int position) {
        return this.seasons.get(position);
    }

    static class SeasonHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        ImageView posterImage;
        TextView posterTitle;

        SeasonHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text);
        }
    }
}
