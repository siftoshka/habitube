package az.siftoshka.habitube.entities.firebase;

import java.util.List;

public class ShowMedia {
    private int id;
    private float rate;
    private List<Integer> seasons;

    public ShowMedia() {
    }

    public ShowMedia(int id, float rate, List<Integer> seasons) {
        this.id = id;
        this.rate = rate;
        this.seasons = seasons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public List<Integer> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Integer> seasons) {
        this.seasons = seasons;
    }
}
