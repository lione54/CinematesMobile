package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelProfiloAltroUtente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelProfiloAltroUtenteResponce {

    @SerializedName("dbdata")
    private List<DBModelProfiloAltroUtente> results;

    public DBModelProfiloAltroUtenteResponce() {
    }

    public DBModelProfiloAltroUtenteResponce(List<DBModelProfiloAltroUtente> results) {
        this.results = results;
    }

    public List<DBModelProfiloAltroUtente> getResults() {
        return results;
    }

    public void setResults(List<DBModelProfiloAltroUtente> results) {
        this.results = results;
    }
}
