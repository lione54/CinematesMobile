package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelVerifica {

    @SerializedName("dbdata")
    private List<DBModelVerificaResults> results;

    public DBModelVerifica() {
    }

    public DBModelVerifica(List<DBModelVerificaResults> results) {
        this.results = results;
    }

    public List<DBModelVerificaResults> getResults() {
        return results;
    }

    public void setResults(List<DBModelVerificaResults> results) {
        this.results = results;
    }
}
