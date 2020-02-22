package az.siftoshka.habitube.entities.credits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {

    @SerializedName("id") @Expose private long id;
    @SerializedName("cast") @Expose private List<Cast> cast = null;
    @SerializedName("crew") @Expose private List<Crew> crew = null;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }
}
