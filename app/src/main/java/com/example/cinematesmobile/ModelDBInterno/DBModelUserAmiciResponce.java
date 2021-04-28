package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelUserAmici;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelUserAmiciResponce {

    @SerializedName("dbdata")
    private List<DBModelUserAmici> results;

    public DBModelUserAmiciResponce() {
    }

    public DBModelUserAmiciResponce(List<DBModelUserAmici> results) {
        this.results = results;
    }

    public List<DBModelUserAmici> getResults() {
        return results;
    }

    public void setResults(List<DBModelUserAmici> results) {
        this.results = results;
    }
}
