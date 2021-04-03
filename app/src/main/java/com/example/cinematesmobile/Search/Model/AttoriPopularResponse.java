package com.example.cinematesmobile.Search.Model;

import java.util.List;

public class AttoriPopularResponse {

    private Integer page;
    private List<AttoriResponseResults> results;
    private Integer total_pages;
    private Integer total_results;

    public AttoriPopularResponse() {
    }

    public AttoriPopularResponse(Integer page, List<AttoriResponseResults> results, Integer total_pages, Integer total_results) {
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<AttoriResponseResults> getResults() {
        return results;
    }

    public void setResults(List<AttoriResponseResults> results) {
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }
}
