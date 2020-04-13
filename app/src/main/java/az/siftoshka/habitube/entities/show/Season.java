package az.siftoshka.habitube.entities.show;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Season implements Parcelable {

    @SerializedName("air_date") @Expose private String airDate;
    @SerializedName("episode_count") @Expose private int episodeCount;
    @SerializedName("id") @Expose private int id;
    @SerializedName("name") @Expose private String name;
    @SerializedName("overview") @Expose private String overview;
    @SerializedName("poster_path") @Expose private String posterPath;
    @SerializedName("season_number") @Expose private int seasonNumber;
    private boolean added;

    protected Season(Parcel in) {
        airDate = in.readString();
        episodeCount = in.readInt();
        id = in.readInt();
        name = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        seasonNumber = in.readInt();
        added = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(airDate);
        dest.writeInt(episodeCount);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeInt(seasonNumber);
        dest.writeByte((byte) (added ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Season> CREATOR = new Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel in) {
            return new Season(in);
        }

        @Override
        public Season[] newArray(int size) {
            return new Season[size];
        }
    };

    public String getAirDate() {
        return airDate;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public int getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(int episodeCount) {
        this.episodeCount = episodeCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }
}
