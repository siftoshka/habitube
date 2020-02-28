package az.siftoshka.habitube.entities.show;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import az.siftoshka.habitube.Constants;

@Entity(tableName = Constants.DB.SHOW_TABLE)
public class Show implements Parcelable {

    @Ignore @SerializedName("backdrop_path") @Expose private String backdropPath;
    @Ignore @SerializedName("episode_run_time") @Expose private List<Integer> episodeRunTime = null;
    @SerializedName("first_air_date") @Expose private String firstAirDate;
    @Ignore @SerializedName("genres") @Expose private List<ShowGenre> showGenres = null;
    @PrimaryKey @SerializedName("id") @Expose private int id;
    @SerializedName("in_production") @Expose private boolean inProduction;
    @SerializedName("last_air_date") @Expose private String lastAirDate;
    @SerializedName("name") @Expose private String name;
    @SerializedName("number_of_episodes") @Expose private int numberOfEpisodes;
    @SerializedName("number_of_seasons") @Expose private int numberOfSeasons;
    @SerializedName("overview") @Expose private String overview;
    @SerializedName("popularity") @Expose private double popularity;
    @SerializedName("poster_path") @Expose private String posterPath;
    @Ignore @SerializedName("seasons") @Expose private List<Season> seasons = null;
    @SerializedName("status") @Expose private String status;
    @SerializedName("vote_average") @Expose private float voteAverage;
    @SerializedName("vote_count") @Expose private int voteCount;
    @ColumnInfo(name = "added_date") private Date addedDate;
    @ColumnInfo(name = "poster_image", typeAffinity = ColumnInfo.BLOB) private byte[] posterImage;


    public Show(String firstAirDate, int id, boolean inProduction, String lastAirDate, String name,
                int numberOfEpisodes, int numberOfSeasons, String overview, double popularity,
                String posterPath, String status, float voteAverage, int voteCount, Date addedDate, byte[] posterImage) {
        this.firstAirDate = firstAirDate;
        this.id = id;
        this.inProduction = inProduction;
        this.lastAirDate = lastAirDate;
        this.name = name;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.status = status;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.addedDate = addedDate;
        this.posterImage = posterImage;
    }

    protected Show(Parcel in) {
        backdropPath = in.readString();
        firstAirDate = in.readString();
        id = in.readInt();
        inProduction = in.readByte() != 0;
        lastAirDate = in.readString();
        name = in.readString();
        numberOfEpisodes = in.readInt();
        numberOfSeasons = in.readInt();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        seasons = in.createTypedArrayList(Season.CREATOR);
        status = in.readString();
        voteAverage = in.readFloat();
        voteCount = in.readInt();
        posterImage = in.createByteArray();
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public byte[] getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(byte[] posterImage) {
        this.posterImage = posterImage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<ShowGenre> getShowGenres() {
        return showGenres;
    }

    public void setShowGenres(List<ShowGenre> showGenres) {
        this.showGenres = showGenres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInProduction() {
        return inProduction;
    }

    public void setInProduction(boolean inProduction) {
        this.inProduction = inProduction;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return id == show.id &&
                inProduction == show.inProduction &&
                numberOfEpisodes == show.numberOfEpisodes &&
                numberOfSeasons == show.numberOfSeasons &&
                Double.compare(show.popularity, popularity) == 0 &&
                Float.compare(show.voteAverage, voteAverage) == 0 &&
                voteCount == show.voteCount &&
                Objects.equals(backdropPath, show.backdropPath) &&
                Objects.equals(episodeRunTime, show.episodeRunTime) &&
                Objects.equals(firstAirDate, show.firstAirDate) &&
                Objects.equals(showGenres, show.showGenres) &&
                Objects.equals(lastAirDate, show.lastAirDate) &&
                Objects.equals(name, show.name) &&
                Objects.equals(overview, show.overview) &&
                Objects.equals(posterPath, show.posterPath) &&
                Objects.equals(seasons, show.seasons) &&
                Objects.equals(status, show.status) &&
                Arrays.equals(posterImage, show.posterImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(backdropPath);
        parcel.writeString(firstAirDate);
        parcel.writeInt(id);
        parcel.writeByte((byte) (inProduction ? 1 : 0));
        parcel.writeString(lastAirDate);
        parcel.writeString(name);
        parcel.writeInt(numberOfEpisodes);
        parcel.writeInt(numberOfSeasons);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeTypedList(seasons);
        parcel.writeString(status);
        parcel.writeFloat(voteAverage);
        parcel.writeInt(voteCount);
        parcel.writeByteArray(posterImage);
    }
}
