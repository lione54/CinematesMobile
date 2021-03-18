package com.example.cinematesmobile.Search.Model;

import java.lang.reflect.Constructor;
import java.util.List;

public class MovieResponseResults {
    private boolean for_adult;
    private String backdrop_path;
    private List<Integer> genre_id;
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private String poster_path;
    private String release_date;
    private String title;
    private boolean video;
    private float vote_average;

    public MovieResponseResults() {
    }

    public MovieResponseResults(boolean for_adult, String backdrop_path, List<Integer> genre_id, int id, String original_language, String original_title, String overview, String poster_path, String release_date, String title, boolean video, float vote_average) {
        this.for_adult = for_adult;
        this.backdrop_path = backdrop_path;
        this.genre_id = genre_id;
        this.id = id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
    }

    public boolean isFor_adult() {
        return for_adult;
    }

    public void setFor_adult(boolean for_adult) {
        this.for_adult = for_adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public List<Integer> getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(List<Integer> genre_id) {
        this.genre_id = genre_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        String UrlBase = "https://image.tmdb.org/t/p/w500";
        return UrlBase + poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }
}
