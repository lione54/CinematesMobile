package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelAttributiListaResults {

    @SerializedName("Descrizione")
    private String Descrizione;
    @SerializedName("Visibilita")
    private String Visibilita;

    public DBModelAttributiListaResults() {
    }

    public DBModelAttributiListaResults(String descrizione, String visibilita) {
        Descrizione = descrizione;
        Visibilita = visibilita;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getVisibilita() {
        return Visibilita;
    }

    public void setVisibilita(String visibilita) {
        Visibilita = visibilita;
    }
}
