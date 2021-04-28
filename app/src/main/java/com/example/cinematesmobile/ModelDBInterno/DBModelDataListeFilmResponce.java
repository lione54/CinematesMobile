package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelDataListeFilmResponce {

    @SerializedName("dbdata")
    private List<DBModelDataListeFilm> listeFilms;

    public DBModelDataListeFilmResponce() {
    }

    public DBModelDataListeFilmResponce(List<DBModelDataListeFilm> listeFilms) {
        this.listeFilms = listeFilms;
    }

    public List<DBModelDataListeFilm> getListeFilms() {
        return listeFilms;
    }

    public void setListeFilms(List<DBModelDataListeFilm> listeFilms) {
        this.listeFilms = listeFilms;
    }
}
