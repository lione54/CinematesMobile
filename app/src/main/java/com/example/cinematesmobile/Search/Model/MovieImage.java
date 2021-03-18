package com.example.cinematesmobile.Search.Model;

import java.util.List;

public class MovieImage {
    private int id;
    private List<AttoriImmageResult> posters;

    public MovieImage() {
    }

    public MovieImage(int id, List<AttoriImmageResult> posters) {
        this.id = id;
        this.posters = posters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AttoriImmageResult> getPosters() {
        return posters;
    }

    public void setPosters(List<AttoriImmageResult> posters) {
        this.posters = posters;
    }
}
