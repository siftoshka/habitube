package az.siftoshka.habitube.entities.firebase;

import java.util.List;

public class ShowMedia {
    private int id;
    private float rate;
    private List<Integer> episodes;

    public ShowMedia() {
    }

    public ShowMedia(int id, float rate, List<Integer> episodes) {
        this.id = id;
        this.rate = rate;
        this.episodes = episodes;
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

    public List<Integer> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Integer> episodes) {
        this.episodes = episodes;
    }
}
