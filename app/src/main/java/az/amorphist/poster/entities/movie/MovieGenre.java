package az.amorphist.poster.entities.movie;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "movie_genre", foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "movieId", childColumns = "id", onDelete = CASCADE))
public class MovieGenre {

    private int movieId;
    @PrimaryKey @SerializedName("id") @Expose private int id;
    @SerializedName("name") @Expose private String name;

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