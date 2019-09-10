package az.amorphist.habitube.entities.movie;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static androidx.room.ForeignKey.CASCADE;
import static az.amorphist.habitube.Constants.DB.MOVIE_GENRE_TABLE;

@Entity(tableName = MOVIE_GENRE_TABLE, foreignKeys = @ForeignKey(entity = Movie.class, parentColumns = "id", childColumns = "movie_id", onDelete = CASCADE))
public class MovieGenre {

    @ColumnInfo(name = "movie_id", index = true) private int movieId;
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "uid") private long uid;
    @ColumnInfo(name = "genre_id") @SerializedName("id") @Expose private int id;
    @ColumnInfo(name = "name") @SerializedName("name") @Expose private String name;

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

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
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