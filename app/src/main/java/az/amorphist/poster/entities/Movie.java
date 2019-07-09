package az.amorphist.poster.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("id") @Expose private int movieId;
    @SerializedName("original_title") @Expose private String movieTitle;
    @SerializedName("original_name") @Expose private String showTitle;
    @SerializedName("overview") @Expose private String movieBody;
    @SerializedName("poster_path") @Expose private String movieImage;
    @SerializedName("backdrop_path") @Expose private String movieBackgroundImage;
    @SerializedName("release_date") @Expose private String movieDate;
    @SerializedName("first_air_date") @Expose private String showDate;
    @SerializedName("vote_average") @Expose private float movieRate;
    @SerializedName("popularity") @Expose private float movieViews;

    public Movie(int movieId, String movieTitle, String showTitle, String movieBody, String movieImage, String movieBackgroundImage, String movieDate, String showDate, float movieRate, float movieViews) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showTitle = showTitle;
        this.movieBody = movieBody;
        this.movieImage = movieImage;
        this.movieBackgroundImage = movieBackgroundImage;
        this.movieDate = movieDate;
        this.showDate = showDate;
        this.movieRate = movieRate;
        this.movieViews = movieViews;
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

    public String getMovieBody() {
        return movieBody;
    }

    public void setMovieBody(String movieBody) {
        this.movieBody = movieBody;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public String getMovieBackgroundImage() {
        return movieBackgroundImage;
    }

    public void setMovieBackgroundImage(String movieBackgroundImage) {
        this.movieBackgroundImage = movieBackgroundImage;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public float getMovieRate() {
        return movieRate;
    }

    public void setMovieRate(float movieRate) {
        this.movieRate = movieRate;
    }

    public float getMovieViews() {
        return movieViews;
    }

    public void setMovieViews(float movieViews) {
        this.movieViews = movieViews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return movieId == movie.movieId;
    }

    @Override
    public int hashCode() {
        return movieId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", movieTitle='" + movieTitle + '\'' +
                ", showTitle='" + showTitle + '\'' +
                ", movieBody='" + movieBody + '\'' +
                ", movieImage='" + movieImage + '\'' +
                ", movieBackgroundImage='" + movieBackgroundImage + '\'' +
                ", movieDate='" + movieDate + '\'' +
                ", showDate='" + showDate + '\'' +
                ", movieRate=" + movieRate +
                ", movieViews=" + movieViews +
                '}';
    }
}
