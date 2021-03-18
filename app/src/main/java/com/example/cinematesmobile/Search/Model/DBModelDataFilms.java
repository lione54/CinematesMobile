package com.example.cinematesmobile.Search.Model;

import java.net.URL;

public class DBModelDataFilms {
    private Integer id_film;
    private String Titolofilm;
    private String image;

    public DBModelDataFilms(Integer id_film, String titolofilm, String image) {
        this.id_film = id_film;
        Titolofilm = titolofilm;
        this.image = image;
    }

    public Integer getId_film() {
        return id_film;
    }

    public void setId_film(Integer id_film) {
        this.id_film = id_film;
    }

    public String getTitolofilm() {
        return Titolofilm;
    }

    public void setTitolofilm(String titolofilm) {
        Titolofilm = titolofilm;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
