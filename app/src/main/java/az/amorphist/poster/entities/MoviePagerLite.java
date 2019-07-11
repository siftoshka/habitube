package az.amorphist.poster.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviePagerLite {
    @SerializedName("page") @Expose private Integer page;
    @SerializedName("results") @Expose private List<MovieLite> results = null;
    @SerializedName("total_pages") @Expose private Integer totalPages;
    @SerializedName("total_results") @Expose private Integer totalResults;

    public MoviePagerLite(Integer page, List<MovieLite> results, Integer totalPages, Integer totalResults) {
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieLite> getResults() {
        return results;
    }

    public void setResults(List<MovieLite> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
