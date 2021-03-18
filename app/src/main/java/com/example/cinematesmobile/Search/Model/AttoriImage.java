package com.example.cinematesmobile.Search.Model;

import java.util.List;

public class AttoriImage {
    private Integer id;
    private List<AttoriImmageResult> profiles;

    public AttoriImage() {
    }

    public AttoriImage(Integer id, List<AttoriImmageResult> profiles) {
        this.id = id;
        this.profiles = profiles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<AttoriImmageResult> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<AttoriImmageResult> profiles) {
        this.profiles = profiles;
    }
}
