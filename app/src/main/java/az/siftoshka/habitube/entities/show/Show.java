package az.siftoshka.habitube.entities.show;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import az.siftoshka.habitube.Constants;
import az.siftoshka.habitube.utils.IntegerConverter;

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
    @Ignore @SerializedName("seasons") @Expose private List<Season> seasons;
    @SerializedName("status") @Expose private String status;
    @SerializedName("vote_average") @Expose private float voteAverage;
    @SerializedName("vote_count") @Expose private int voteCount;
    @ColumnInfo(name = "added_date") private Date addedDate;
    @ColumnInfo(name = "my_rating") private float myRating;
    @TypeConverters(IntegerConverter.class) @ColumnInfo(name = "watched_seasons") private List<Integer> watchedSeasons = null;

    public Show(String firstAirDate, int id, boolean inProduction, String lastAirDate, String name,
                int numberOfEpisodes, int numberOfSeasons, String overview, double popularity,
                String posterPath, String status, float voteAverage, int voteCount,
                Date addedDate, float myRating, List<Integer> watchedSeasons) {
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
        this.myRating = myRating;
        this.watchedSeasons = watchedSeasons;
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
        myRating = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(backdropPath);
        dest.writeString(firstAirDate);
        dest.writeInt(id);
        dest.writeByte((byte) (inProduction ? 1 : 0));
        dest.writeString(lastAirDate);
        dest.writeString(name);
        dest.writeInt(numberOfEpisodes);
        dest.writeInt(numberOfSeasons);
        dest.writeString(overview);
        dest.writeDouble(popularity);
        dest.writeString(posterPath);
        dest.writeTypedList(seasons);
        dest.writeString(status);
        dest.writeFloat(voteAverage);
        dest.writeInt(voteCount);
        dest.writeFloat(myRating);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public void setSeasons(Season season) {
        this.seasons.add(season);
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

    public float getMyRating() {
        return myRating;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public List<Integer> getWatchedSeasons() {
        return watchedSeasons;
    }

    public void setWatchedSeasons(Integer watchedSeason) {
        this.watchedSeasons.add(watchedSeason);
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
                Float.compare(show.myRating, myRating) == 0 &&
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
                Objects.equals(addedDate, show.addedDate) &&
                Objects.equals(watchedSeasons, show.watchedSeasons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backdropPath, episodeRunTime, firstAirDate, showGenres, id, inProduction, lastAirDate, name, numberOfEpisodes, numberOfSeasons, overview, popularity, posterPath, seasons, status, voteAverage, voteCount, addedDate, myRating, watchedSeasons);
    }
}
