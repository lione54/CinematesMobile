package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Recensioni.Model.DBModelFotoProfilo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelFotoProfiloResponce {

    @SerializedName("dbdata")
    private List<DBModelFotoProfilo> results;

    public DBModelFotoProfiloResponce() {
    }

    public DBModelFotoProfiloResponce(List<DBModelFotoProfilo> results) {
        this.results = results;
    }

    public List<DBModelFotoProfilo> getResults() {
        return results;
    }

    public void setResults(List<DBModelFotoProfilo> results) {
        this.results = results;
    }
}
