package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelFilmsResponce {

    @SerializedName("dbdata")
    private List<DBModelDataFilms> results;

    public DBModelFilmsResponce() {
    }

    public DBModelFilmsResponce(List<DBModelDataFilms> results) {
        this.results = results;
    }

    public List<DBModelDataFilms> getResults() {
        return results;
    }

    public void setResults(List<DBModelDataFilms> results) {
        this.results = results;
    }
}
