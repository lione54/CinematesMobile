package com.example.cinematesmobile.Search.ModelMovieActor;

import java.util.List;

public class GeneriResponse {

    private List<GeneriResponseResult> genres;

    public GeneriResponse() {
    }

    public GeneriResponse(List<GeneriResponseResult> genres) {
        this.genres = genres;
    }

    public List<GeneriResponseResult> getGenres() {
        return genres;
    }

    public void setGenres(List<GeneriResponseResult> genres) {
        this.genres = genres;
    }
}
