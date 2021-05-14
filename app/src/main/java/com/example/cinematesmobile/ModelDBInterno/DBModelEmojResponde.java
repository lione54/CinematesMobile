package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelEmoj;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelEmojResponde {

    @SerializedName("dbdata")
    private List<DBModelEmoj> results;

    public DBModelEmojResponde() {
    }

    public DBModelEmojResponde(List<DBModelEmoj> results) {
        this.results = results;
    }

    public List<DBModelEmoj> getResults() {
        return results;
    }

    public void setResults(List<DBModelEmoj> results) {
        this.results = results;
    }
}
