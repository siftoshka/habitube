package az.amorphist.poster.entities.show;

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

    public Season(Parcel parcel) {
        posterPath = parcel.readString();
        name = parcel.readString();
        airDate = parcel.readString();
        episodeCount = parcel.readInt();
        overview = parcel.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(name);
        dest.writeString(airDate);
        dest.writeInt(episodeCount);
        dest.writeString(overview);
    }

    public static final Parcelable.Creator<Season> CREATOR = new Parcelable.Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel source) {
            return new Season(source);
        }

        @Override
        public Season[] newArray(int size) {
            return new Season[size];
        }
    };
}