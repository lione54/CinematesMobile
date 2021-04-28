package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelRecensioniResponce {

    @SerializedName("dbdata")
    private List<DBModelRecensioni> results;

    public DBModelRecensioniResponce() {
    }

    public DBModelRecensioniResponce(List<DBModelRecensioni> results) {
        this.results = results;
    }

    public List<DBModelRecensioni> getResults() {
        return results;
    }

    public void setResults(List<DBModelRecensioni> results) {
        this.results = results;
    }
}
