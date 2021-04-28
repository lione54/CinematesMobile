package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class DBModelDataFilms {

    @SerializedName("id_film_inserito")
    private Integer id_film;
    @SerializedName("Titolo_Film")
    private String Titolofilm;
    @SerializedName("Url_Immagine")
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
