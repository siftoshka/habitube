package az.amorphist.poster.entities.movie;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "movies")
public class Movie {

    @SerializedName("adult") @Expose private boolean adult;
    @SerializedName("backdrop_path") @Expose private String backdropPath;
    @Ignore @SerializedName("genres") @Expose private List<MovieGenre> movieGenres = null;
    @PrimaryKey @SerializedName("id") @Expose private int id;
    @SerializedName("imdb_id") @Expose private String imdbId;
    @SerializedName("original_title") @Expose private String originalTitle;
    @SerializedName("overview") @Expose private String overview;
    @SerializedName("popularity") @Expose private double popularity;
    @SerializedName("poster_path") @Expose private String posterPath;
    @SerializedName("release_date") @Expose private String releaseDate;
    @SerializedName("runtime") @Expose private int runtime;
    @SerializedName("status") @Expose private String status;
    @SerializedName("tagline") @Expose private String tagline;
    @SerializedName("title") @Expose private String title;
    @SerializedName("vote_average") @Expose private double voteAverage;
    @SerializedName("vote_count") @Expose private int voteCount;

    public Movie(boolean adult, String backdropPath, int id, String imdbId, String originalTitle, String overview, double popularity, String posterPath, String releaseDate, int runtime, String status, String tagline, String title, double voteAverage, int voteCount) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
