package az.siftoshka.habitube.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import az.siftoshka.habitube.R;
import az.siftoshka.habitube.entities.video.Video;
import az.siftoshka.habitube.utils.ImageLoader;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    public interface VideoItemClickListener {
        void onPostClicked(String videoKey);
    }

    private List<Video> videos;
    private VideoItemClickListener clickListener;

    public VideoAdapter(@NonNull VideoItemClickListener clickListener) {
        this.videos = new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video_list, viewGroup, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, final int position) {
        final Video video = this.videos.get(position);
        holder.posterTitle.setText(video.getName());
        ImageLoader.loadYoutube(holder.itemView, video.getKey(), holder.posterImage);
        holder.posterImage.setOnClickListener(v -> clickListener.onPostClicked(video.getKey()));
    }

    @Override
    public void onViewRecycled(@NonNull VideoHolder holder) {
        holder.posterTitle.setText(null);
        holder.posterImage.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void addAllVideos(List<Video> videos) {
        this.videos.clear();
        this.videos.addAll(videos);
        notifyDataSetChanged();
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView posterTitle;

        VideoHolder(@NonNull View itemView) {
            super(itemView);
            this.posterImage = itemView.findViewById(R.id.poster_image);
            this.posterTitle = itemView.findViewById(R.id.poster_text);
        }
    }
}
