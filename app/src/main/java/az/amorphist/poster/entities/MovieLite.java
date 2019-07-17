package az.amorphist.poster.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieLite {
    @SerializedName("id") @Expose private int movieId;
    @SerializedName("original_title") @Expose private String movieTitle;
    @SerializedName("name") @Expose private String showTitle;
    @SerializedName("poster_path") @Expose private String movieImage;
    @SerializedName("profile_path") @Expose private String starImage;
    @SerializedName("media_type") @Expose private String mediaType;

    public MovieLite(int movieId, String movieTitle, String showTitle, String movieImage, String starImage, String mediaType) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showTitle = showTitle;
        this.movieImage = movieImage;
        this.starImage = starImage;
        this.mediaType = mediaType;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getStarImage() {
        return starImage;
    }

    public void setStarImage(String starImage) {
        this.starImage = starImage;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieLite movieLite = (MovieLite) o;

        return movieId == movieLite.movieId;
    }

    @Override
    public int hashCode() {
        return movieId;
    }
}
