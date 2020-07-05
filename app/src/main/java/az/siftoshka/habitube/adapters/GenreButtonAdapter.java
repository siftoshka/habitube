package az.siftoshka.habitube.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.genres.Genres;
import az.siftoshka.habitube.entities.movielite.MovieLite;

public class GenreButtonAdapter extends RecyclerView.Adapter<GenreButtonAdapter.GenreButtonHolder> {

    public interface GenreItemClickListener {
        void onClickListener(String id);
    }

    private List<Genres> genres;
    private Context context;
    private GenreItemClickListener clickListener;

    public GenreButtonAdapter(@NonNull GenreItemClickListener clickListener, Context context) {
        this.genres = new ArrayList<>();
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public GenreButtonHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_genres_button, viewGroup, false);
        return new GenreButtonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreButtonHolder holder, final int position) {
        final Genres genres = this.genres.get(position);
        holder.text.setText(genres.getName());
        holder.image.setImageDrawable(context.getDrawable(genres.getImage()));
        holder.button.setOnClickListener(view -> clickListener.onClickListener(genres.getId()));
    }

    @Override
    public void onViewRecycled(@NonNull GenreButtonHolder holder) {
        holder.itemView.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public void addGenres(List<Genres> genres) {
        this.genres.clear();
        this.genres.addAll(genres);
        notifyDataSetChanged();
    }

    static class GenreButtonHolder extends RecyclerView.ViewHolder {

        RelativeLayout button;
        TextView text;
        ImageView image;

        GenreButtonHolder(@NonNull View itemView) {
            super(itemView);
            this.button = itemView.findViewById(R.id.genres);
            this.text = itemView.findViewById(R.id.genres_text);
            this.image = itemView.findViewById(R.id.genres_image);
        }
    }
}
