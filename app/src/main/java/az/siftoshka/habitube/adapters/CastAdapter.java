package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.credits.Cast;
import az.siftoshka.habitube.utils.ImageLoader;
import de.hdodenhof.circleimageview.CircleImageView;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastHolder> {

    public interface CastItemClickListener {
        void onPostClicked(int id);
    }

    private List<Cast> castList;
    private CastItemClickListener clickListener;

    public CastAdapter(@NonNull CastItemClickListener clickListener) {
        this.castList = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CastHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credits, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastHolder holder, final int position) {
        final Cast post = this.castList.get(position);
        ImageLoader.loadPersons(holder.itemView, post.getProfilePath(), holder.posterImage);
        holder.posterTitle.setText(post.getName());
        holder.posterDate.setText(post.getCharacter());

        holder.posterLayout.setOnClickListener(v -> {
            clickListener.onPostClicked(post.getId());
        });

    }

    @Override
    public void onViewRecycled(@NonNull CastHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterDate.setText(null);
        holder.posterLayout.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return castList == null ? 0 : castList.size();
    }

    public void addAllPersons(List<Cast> casts) {
        this.castList.clear();
        this.castList.addAll(casts);
        notifyDataSetChanged();
    }

    public void clean() {
        this.castList.clear();
        notifyDataSetChanged();
    }

    static class CastHolder extends RecyclerView.ViewHolder {

        LinearLayout posterLayout;
        CircleImageView posterImage;
        TextView posterTitle, posterDate;

        CastHolder(@NonNull View itemView) {
            super(itemView);
            this.posterLayout = itemView.findViewById(R.id.item_layout_search);
            this.posterImage = itemView.findViewById(R.id.poster_image_search);
            this.posterTitle = itemView.findViewById(R.id.poster_main_text_search);
            this.posterDate = itemView.findViewById(R.id.poster_main_date_search);
        }
    }
}