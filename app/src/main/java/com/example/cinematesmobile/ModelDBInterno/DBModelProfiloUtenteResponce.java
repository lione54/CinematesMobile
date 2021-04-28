package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelProfiloUtente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelProfiloUtenteResponce {

    @SerializedName("dbdata")
    private List<DBModelProfiloUtente> results;

    public DBModelProfiloUtenteResponce() {
    }

    public DBModelProfiloUtenteResponce(List<DBModelProfiloUtente> results) {
        this.results = results;
    }

    public List<DBModelProfiloUtente> getResults() {
        return results;
    }

    public void setResults(List<DBModelProfiloUtente> results) {
        this.results = results;
    }
}
