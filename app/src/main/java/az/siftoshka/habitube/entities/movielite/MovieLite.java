package az.siftoshka.habitube.entities.movielite;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieLite implements Parcelable {

    @SerializedName("id") @Expose private int movieId;
    @SerializedName("title") @Expose private String movieTitle;
    @SerializedName("name") @Expose private String showTitle;
    @SerializedName("poster_path") @Expose private String movieImage;
    @SerializedName("backdrop_path") @Expose private String backdropPath;
    @SerializedName("profile_path") @Expose private String starImage;
    @SerializedName("release_date") @Expose private String releaseDate;
    @SerializedName("vote_average") @Expose private double voteAverage;
    @SerializedName("first_air_date") @Expose private String firstAirDate;
    @SerializedName("media_type") @Expose private String mediaType;

    public MovieLite(int movieId, String movieTitle, String showTitle, String movieImage, String backdropPath,
                     String starImage, String releaseDate, double voteAverage, String firstAirDate, String mediaType) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.showTitle = showTitle;
        this.movieImage = movieImage;
        this.backdropPath = backdropPath;
        this.starImage = starImage;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.firstAirDate = firstAirDate;
        this.mediaType = mediaType;
    }

    public MovieLite() {
    }

    protected MovieLite(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        showTitle = in.readString();
        movieImage = in.readString();
        starImage = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        firstAirDate = in.readString();
        mediaType = in.readString();
    }

    public static final Creator<MovieLite> CREATOR = new Creator<MovieLite>() {
        @Override
        public MovieLite createFromParcel(Parcel in) {
            return new MovieLite(in);
        }

        @Override
        public MovieLite[] newArray(int size) {
            return new MovieLite[size];
        }
    };

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(movieTitle);
        parcel.writeString(showTitle);
        parcel.writeString(movieImage);
        parcel.writeString(starImage);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
        parcel.writeString(firstAirDate);
        parcel.writeString(mediaType);
    }
}
