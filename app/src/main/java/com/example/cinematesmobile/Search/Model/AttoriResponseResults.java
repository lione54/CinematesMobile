package com.example.cinematesmobile.Search.Model;

import java.util.List;

public class AttoriResponseResults {
    private boolean forAdult;
    private int gender;
    private int id;
    private List<AttoriResponseResultsKnownFor> known_for;
    private String known_for_department;
    private String name;
    private String profile_path;

    public AttoriResponseResults() {
    }

    public AttoriResponseResults(boolean forAdult, int gender, int id, List<AttoriResponseResultsKnownFor> known_for, String known_for_department, String name, String profile_path) {
        this.forAdult = forAdult;
        this.gender = gender;
        this.id = id;
        this.known_for = known_for;
        this.known_for_department = known_for_department;
        this.name = name;
        this.profile_path = profile_path;
    }

    public boolean isForAdult() {
        return forAdult;
    }

    public void setForAdult(boolean forAdult) {
        this.forAdult = forAdult;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AttoriResponseResultsKnownFor> getKnown_for() {
        return known_for;
    }

    public void setKnown_for(List<AttoriResponseResultsKnownFor> known_for) {
        this.known_for = known_for;
    }

    public String getKnown_for_department() {
        return known_for_department;
    }

    public void setKnown_for_department(String known_for_department) {
        this.known_for_department = known_for_department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        String UrlBase = "https://image.tmdb.org/t/p/w500";
        return UrlBase + profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }
}
