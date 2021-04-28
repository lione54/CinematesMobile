package com.example.cinematesmobile.Search.ModelMovieActor;

import java.util.List;

public class MovieDetail {
    private boolean adult;
    private String backdrop_path;
    private AppartieneAllaCollezione belongs_to_collection;
    private int budget;
    private List<Generi> genres;
    private String homepage;
    private Integer id;
    private String imdb_id;
    private String original_language;
    private String original_title;
    private String overview;
    private float popularity;
    private String poster_path;
    private List<Produttori> production_companies;
    private List<PaeseProduzoine> production_countries;
    private String release_date;
    private Integer runtime;
    private List<LingueParlate> spoken_languages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private float vote_average;
    private int vote_count;

    public MovieDetail() {
    }

    public MovieDetail(boolean adult, String backdrop_path, AppartieneAllaCollezione belongs_to_collection, int budget, List<Generi> genres, String homepage, Integer id, String imdb_id, String original_language, String original_title, String overview, float popularity, String poster_path, List<Produttori> production_companies, List<PaeseProduzoine> production_countries, String release_date,  Integer runtime, List<LingueParlate> spoken_languages, String status, String tagline, String title, boolean video, float vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.belongs_to_collection = belongs_to_collection;
        this.budget = budget;
        this.genres = genres;
        this.homepage = homepage;
        this.id = id;
        this.imdb_id = imdb_id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.production_companies = production_companies;
        this.production_countries = production_countries;
        this.release_date = release_date;
        this.runtime = runtime;
        this.spoken_languages = spoken_languages;
        this.status = status;
        this.tagline = tagline;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        String UrlBase = "https://image.tmdb.org/t/p/w500";
        return UrlBase + backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public AppartieneAllaCollezione getBelongs_to_collection() {
        return belongs_to_collection;
    }

    public void setBelongs_to_collection(AppartieneAllaCollezione belongs_to_collection) {
        this.belongs_to_collection = belongs_to_collection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Generi> getGenres() {
        return genres;
    }

    public void setGenres(List<Generi> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
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

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        String UrlBase = "https://image.tmdb.org/t/p/w500";
        return UrlBase + poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<Produttori> getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(List<Produttori> production_companies) {
        this.production_companies = production_companies;
    }

    public List<PaeseProduzoine> getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(List<PaeseProduzoine> production_countries) {
        this.production_countries = production_countries;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<LingueParlate> getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(List<LingueParlate> spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
