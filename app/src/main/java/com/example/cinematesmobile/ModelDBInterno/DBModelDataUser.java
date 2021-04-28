package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelDataUserResults;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelDataUser {

    @SerializedName("dbdata")
    private List<DBModelDataUserResults> results;

    public DBModelDataUser() {
    }

    public DBModelDataUser(List<DBModelDataUserResults> results) {
        this.results = results;
    }

    public List<DBModelDataUserResults> getResults() {
        return results;
    }

    public void setResults(List<DBModelDataUserResults> results) {
        this.results = results;
    }
}
