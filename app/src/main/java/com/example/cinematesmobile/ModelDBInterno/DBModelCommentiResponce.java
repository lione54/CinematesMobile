package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelCommenti;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelCommentiResponce {

    @SerializedName("dbdata")
    private List<DBModelCommenti> results;

    public DBModelCommentiResponce() {
    }

    public DBModelCommentiResponce(List<DBModelCommenti> results) {
        this.results = results;
    }

    public List<DBModelCommenti> getResults() {
        return results;
    }

    public void setResults(List<DBModelCommenti> results) {
        this.results = results;
    }
}
