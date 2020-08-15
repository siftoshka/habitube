package az.siftoshka.habitube.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.explore.Media;

public class MediaAdapter  extends RecyclerView.Adapter<MediaAdapter.MediaHolder> {

    public interface ItemClickListener {
        void onPostClicked(int id);
    }

    private List<Media> media;
    private Context context;
    private ItemClickListener clickListener;


    public MediaAdapter(Context context, @NonNull ItemClickListener clickListener) {
        this.media = new ArrayList<>();
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MediaAdapter.MediaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_media_explore, viewGroup, false);
        int deviceWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        view.getLayoutParams().width = deviceWidth - (deviceWidth / 100 * 20);
        return new MediaAdapter.MediaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MediaAdapter.MediaHolder holder, final int position) {
        final Media media = this.media.get(position);
        holder.mediaLayout.setBackgroundTintList(context.getResources().getColorStateList(media.getBackground()));
        holder.mediaImage.setImageResource(media.getImage());
        holder.mediaText.setText(media.getText());
        holder.mediaText.setTextColor(context.getResources().getColorStateList(media.getTextColor()));
        holder.mediaLayout.setOnClickListener(view -> clickListener.onPostClicked(media.getId()));
    }

    @Override
    public void onViewRecycled(@NonNull MediaAdapter.MediaHolder holder) {
        holder.mediaText.setText(null);
    }

    @Override
    public int getItemCount() {
        return media.size();
    }

    public void addAllMedia() {
        this.media.clear();
        List<Media> media = new ArrayList<>();
        media.add(new Media(1, R.color.background, R.drawable.ic_undraw_netflix, R.string.enjoy_netflix, R.color.dark_600));
        media.add(new Media(2, R.color.prime_video, R.drawable.ic_amazon_prime_video, R.string.enjoy_amazon, R.color.white_is_white));
        media.add(new Media(3, R.color.disney_plus, R.drawable.ic_disney_plus, R.string.enjoy_disney, R.color.white_is_white));
        media.add(new Media(4, R.color.hint_text, R.drawable.ic_appletv, R.string.enjoy_apple, R.color.white_is_white));
        this.media.addAll(media);
        notifyDataSetChanged();
    }

    static class MediaHolder extends RecyclerView.ViewHolder {
        LinearLayout mediaLayout;
        ImageView mediaImage;
        TextView mediaText;

        MediaHolder(@NonNull View itemView) {
            super(itemView);
            this.mediaLayout = itemView.findViewById(R.id.explore_media);
            this.mediaImage = itemView.findViewById(R.id.media_image);
            this.mediaText = itemView.findViewById(R.id.media_text);
        }
    }
}
