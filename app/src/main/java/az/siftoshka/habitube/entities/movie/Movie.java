package az.siftoshka.habitube.entities.movie;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import az.siftoshka.habitube.Constants;

@Entity(tableName = Constants.DB.MOVIE_TABLE)
public class Movie implements Parcelable {

    @ColumnInfo(name = "adult") @SerializedName("adult") @Expose private boolean adult;
    @Ignore @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") @Expose private String backdropPath;
    @Ignore @SerializedName("genres") @Expose private List<MovieGenre> movieGenres;
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose private int id;
    @ColumnInfo(name = "imdb_id") @SerializedName("imdb_id") @Expose private String imdbId;
    @ColumnInfo(name = "original_title") @SerializedName("original_title") @Expose private String originalTitle;
    @ColumnInfo(name = "overview") @SerializedName("overview") @Expose private String overview;
    @ColumnInfo(name = "popularity") @SerializedName("popularity") @Expose private double popularity;
    @Ignore @ColumnInfo(name = "poster_path") @SerializedName("poster_path") @Expose private String posterPath;
    @ColumnInfo(name = "release_date") @SerializedName("release_date") @Expose private String releaseDate;
    @ColumnInfo(name = "runtime") @SerializedName("runtime") @Expose private int runtime;
    @ColumnInfo(name = "status") @SerializedName("status") @Expose private String status;
    @ColumnInfo(name = "title") @SerializedName("title") @Expose private String title;
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") @Expose private double voteAverage;
    @ColumnInfo(name = "vote_count") @SerializedName("vote_count") @Expose private int voteCount;
    @ColumnInfo(name = "budget") @SerializedName("budget") @Expose private long budget;
    @ColumnInfo(name = "revenue") @SerializedName("revenue") @Expose private long revenue;
    @ColumnInfo(name = "added_date") private Date addedDate;
    @ColumnInfo(name = "poster_image", typeAffinity = ColumnInfo.BLOB) private byte[] posterImage;

    public Movie(boolean adult, int id, String imdbId, String originalTitle, String overview,
                 double popularity, String releaseDate, int runtime, String status, String title,
                 double voteAverage, int voteCount, long budget, long revenue, Date addedDate, byte[] posterImage) {
        this.adult = adult;
        this.id = id;
        this.imdbId = imdbId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.status = status;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.budget = budget;
        this.revenue = revenue;
        this.addedDate = addedDate;
        this.posterImage = posterImage;
    }

    protected Movie(Parcel in) {
        adult = in.readByte() != 0;
        backdropPath = in.readString();
        id = in.readInt();
        imdbId = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        runtime = in.readInt();
        status = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        budget = in.readLong();
        revenue = in.readLong();
        posterImage = in.createByteArray();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<MovieGenre> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(List<MovieGenre> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

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

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
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
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(backdropPath);
        parcel.writeInt(id);
        parcel.writeString(imdbId);
        parcel.writeString(originalTitle);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeInt(runtime);
        parcel.writeString(status);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(voteCount);
        parcel.writeLong(budget);
        parcel.writeLong(revenue);
        parcel.writeByteArray(posterImage);
    }
}
