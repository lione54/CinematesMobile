package com.example.cinematesmobile.Search.Model;

public class AppartieneAllaCollezione {

    private int id;
    private String name;
    private String poster_path;
    private String backdrop_path;

    public AppartieneAllaCollezione() {
    }

    public AppartieneAllaCollezione(int id, String name, String poster_path, String backdrop_path) {
        this.id = id;
        this.name = name;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster_path() {
        String UrlBase = "https://image.tmdb.org/t/p/w500";
        return UrlBase + poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}

