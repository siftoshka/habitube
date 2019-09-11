package az.siftoshka.habitube.entities.movie;

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
public class Movie {

    @ColumnInfo(name = "adult") @SerializedName("adult") @Expose private boolean adult;
    @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") @Expose private String backdropPath;
    @Ignore @SerializedName("genres") @Expose private List<MovieGenre> movieGenres;
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") @Expose private int id;
    @ColumnInfo(name = "imdb_id") @SerializedName("imdb_id") @Expose private String imdbId;
    @ColumnInfo(name = "original_title") @SerializedName("original_title") @Expose private String originalTitle;
    @ColumnInfo(name = "overview") @SerializedName("overview") @Expose private String overview;
    @ColumnInfo(name = "popularity") @SerializedName("popularity") @Expose private double popularity;
    @ColumnInfo(name = "poster_path") @SerializedName("poster_path") @Expose private String posterPath;
    @ColumnInfo(name = "release_date") @SerializedName("release_date") @Expose private String releaseDate;
    @ColumnInfo(name = "runtime") @SerializedName("runtime") @Expose private int runtime;
    @ColumnInfo(name = "status") @SerializedName("status") @Expose private String status;
    @ColumnInfo(name = "tagline") @SerializedName("tagline") @Expose private String tagline;
    @ColumnInfo(name = "title") @SerializedName("title") @Expose private String title;
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") @Expose private double voteAverage;
    @ColumnInfo(name = "vote_count") @SerializedName("vote_count") @Expose private int voteCount;
    @ColumnInfo(name = "added_date") private Date addedDate;

    public Movie(boolean adult, String backdropPath, int id,
                 String imdbId, String originalTitle, String overview, double popularity,
                 String posterPath, String releaseDate, int runtime, String status, String tagline,
                 String title, double voteAverage, int voteCount) {
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.id = id;
        this.imdbId = imdbId;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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
}
