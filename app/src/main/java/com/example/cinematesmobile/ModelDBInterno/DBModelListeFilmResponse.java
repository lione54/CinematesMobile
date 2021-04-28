package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelListeFilmResponse {

    @SerializedName("dbdata")
    private List<DBModelDataListeFilm> results;

    public DBModelListeFilmResponse() {
    }

    public DBModelListeFilmResponse(List<DBModelDataListeFilm> results) {
        this.results = results;
    }

    public List<DBModelDataListeFilm> getResults() {
        return results;
    }

    public void setResults(List<DBModelDataListeFilm> results) {
        this.results = results;
    }
}
