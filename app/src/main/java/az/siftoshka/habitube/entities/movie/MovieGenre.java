package az.siftoshka.habitube.entities.movie;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieGenre {

    private int movieId;
    @SerializedName("id") @Expose private int id;
    @SerializedName("name") @Expose private String name;

    public MovieGenre(int movieId, int id, String name) {
        this.movieId = movieId;
        this.id = id;
        this.name = name;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
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
}