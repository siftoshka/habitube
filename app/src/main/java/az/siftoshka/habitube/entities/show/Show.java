package az.siftoshka.habitube.entities.show;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Show {

    @SerializedName("backdrop_path") @Expose private String backdropPath;
    @SerializedName("episode_run_time") @Expose private List<Integer> episodeRunTime = null;
    @SerializedName("first_air_date") @Expose private String firstAirDate;
    @SerializedName("genres") @Expose private List<ShowGenre> showGenres = null;
    @SerializedName("id") @Expose private int id;
    @SerializedName("in_production") @Expose private boolean inProduction;
    @SerializedName("last_air_date") @Expose private String lastAirDate;
    @SerializedName("name") @Expose private String name;
    @SerializedName("number_of_episodes") @Expose private int numberOfEpisodes;
    @SerializedName("number_of_seasons") @Expose private int numberOfSeasons;
    @SerializedName("overview") @Expose private String overview;
    @SerializedName("popularity") @Expose private double popularity;
    @SerializedName("poster_path") @Expose private String posterPath;
    @SerializedName("seasons") @Expose private List<Season> seasons = null;
    @SerializedName("status") @Expose private String status;
    @SerializedName("vote_average") @Expose private float voteAverage;
    @SerializedName("vote_count") @Expose private int voteCount;

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public List<Integer> getEpisodeRunTime() {
        return episodeRunTime;
    }

    public void setEpisodeRunTime(List<Integer> episodeRunTime) {
        this.episodeRunTime = episodeRunTime;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public List<ShowGenre> getShowGenres() {
        return showGenres;
    }

    public void setShowGenres(List<ShowGenre> showGenres) {
        this.showGenres = showGenres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isInProduction() {
        return inProduction;
    }

    public void setInProduction(boolean inProduction) {
        this.inProduction = inProduction;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEpisodes() {
        return numberOfEpisodes;
    }

    public void setNumberOfEpisodes(int numberOfEpisodes) {
        this.numberOfEpisodes = numberOfEpisodes;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
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

    public List<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}