package az.siftoshka.habitube.entities.movielite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("page") @Expose private int page;
    @SerializedName("results") @Expose private List<MovieLite> results = null;
    @SerializedName("total_pages") @Expose private int totalPages;
    @SerializedName("total_results") @Expose private int totalResults;

    public MovieResponse(int page, List<MovieLite> results, int totalPages, int totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieLite> getResults() {
        return results;
    }

    public void setResults(List<MovieLite> results) {
        this.results = results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
