package com.example.cinematesmobile.Frag.Model;

public class DBModelDataListeFilm {

    private String TitoloLista;
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
