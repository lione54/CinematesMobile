package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelDettagliCinematesResponce;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelDettagliCinemates {

    @SerializedName("dbdata")
    private List<DBModelDettagliCinematesResponce> results;

    public DBModelDettagliCinemates() {
    }

    public DBModelDettagliCinemates(List<DBModelDettagliCinematesResponce> results) {
        this.results = results;
    }

    public List<DBModelDettagliCinematesResponce> getResults() {
        return results;
    }

    public void setResults(List<DBModelDettagliCinematesResponce> results) {
        this.results = results;
    }
}
