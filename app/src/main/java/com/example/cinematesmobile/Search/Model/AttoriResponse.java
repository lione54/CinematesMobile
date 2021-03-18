package com.example.cinematesmobile.Search.Model;

import java.util.List;

public class AttoriResponse {
    private int page;
    private List<AttoriResponseResults> results;
    private int total_page;
    private int total_results;

    public AttoriResponse() {
    }

    public AttoriResponse(int page, List<AttoriResponseResults> results, int total_page, int total_results) {
        this.page = page;
        this.results = results;
        this.total_page = total_page;
        this.total_results = total_results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<AttoriResponseResults> getResults() {
        return results;
    }

    public void setResults(List<AttoriResponseResults> results) {
        this.results = results;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }
}
