package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelAttributiListaResults;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelAttributiLista {

    @SerializedName("dbdata")
    private List<DBModelAttributiListaResults> results;

    public DBModelAttributiLista() {
    }

    public DBModelAttributiLista(List<DBModelAttributiListaResults> results) {
        this.results = results;
    }

    public List<DBModelAttributiListaResults> getResults() {
        return results;
    }

    public void setResults(List<DBModelAttributiListaResults> results) {
        this.results = results;
    }
}
