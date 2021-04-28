package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelVoti;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelVotiResponse {

    @SerializedName("dbdata")
    private List<DBModelVoti> results;

    public DBModelVotiResponse() {
    }

    public DBModelVotiResponse(List<DBModelVoti> results) {
        this.results = results;
    }

    public List<DBModelVoti> getResults() {
        return results;
    }

    public void setResults(List<DBModelVoti> results) {
        this.results = results;
    }
}
