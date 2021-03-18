package com.example.cinematesmobile.Search.Model;


import java.util.List;

public class UpcomingResponse {

    private ReleaseDates dates;
    private int page;
    private List<MovieResponseResults> results;
    private int total_pages;
    private int total_results;

    public UpcomingResponse() {
    }

    public UpcomingResponse(ReleaseDates dates, int page, List<MovieResponseResults> results, int total_pages, int total_results) {
        this.dates = dates;
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public ReleaseDates getDates() {
        return dates;
    }

    public void setDates(ReleaseDates dates) {
        this.dates = dates;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieResponseResults> getResults() {
        return results;
    }

    public void setResults(List<MovieResponseResults> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
