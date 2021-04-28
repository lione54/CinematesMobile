package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelDataListeFilm {

    @SerializedName("Tipo_Lista")
    private String TitoloLista;
    @SerializedName("Descrizione")
    private String DescrizioneLista;

    public DBModelDataListeFilm() {
    }

    public DBModelDataListeFilm(String titoloLista, String descrizioneLista) {
        TitoloLista = titoloLista;
        DescrizioneLista = descrizioneLista;
    }

    public String getTitoloLista() {
        return TitoloLista;
    }

    public void setTitoloLista(String titoloLista) {
        TitoloLista = titoloLista;
    }

    public String getDescrizioneLista() {
        return DescrizioneLista;
    }

    public void setDescrizioneLista(String descrizioneLista) {
        DescrizioneLista = descrizioneLista;
    }
}
