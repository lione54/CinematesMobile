package com.example.cinematesmobile.Search.ModelMovieActor;

public class AttoriImmageResult {
    private float aspect_ratio;
    private String file_path;
    private int height;
    private String iso_639_1;
    private float vote_avarange;
    private int vote_count;
    private int width;

    public AttoriImmageResult() {
    }

    public AttoriImmageResult(float aspect_ratio, String file_path, int height, String iso_639_1, float vote_avarange, int vote_count, int width) {
        this.aspect_ratio = aspect_ratio;
        this.file_path = file_path;
        this.height = height;
        this.iso_639_1 = iso_639_1;
        this.vote_avarange = vote_avarange;
        this.vote_count = vote_count;
        this.width = width;
    }

    public float getAspect_ratio() {
        return aspect_ratio;
    }

    public void setAspect_ratio(float aspect_ratio) {
        this.aspect_ratio = aspect_ratio;
    }

    public String getFile_path() {
            String UrlBase = "https://image.tmdb.org/t/p/w500";
            return UrlBase + file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public float getVote_avarange() {
        return vote_avarange;
    }

    public void setVote_avarange(float vote_avarange) {
        this.vote_avarange = vote_avarange;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
