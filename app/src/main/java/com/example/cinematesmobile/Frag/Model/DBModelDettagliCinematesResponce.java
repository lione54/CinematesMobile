package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelDettagliCinematesResponce {

    @SerializedName("Numero_Recensioni")
    private String NumeroRecensioni;
    @SerializedName("Valutazione")
    private String Valutazione;

    public DBModelDettagliCinematesResponce() {
    }

    public DBModelDettagliCinematesResponce(String numeroRecensioni, String valutazione) {
        NumeroRecensioni = numeroRecensioni;
        Valutazione = valutazione;
    }

    public String getNumeroRecensioni() {
        return NumeroRecensioni;
    }

    public void setNumeroRecensioni(String numeroRecensioni) {
        NumeroRecensioni = numeroRecensioni;
    }

    public String getValutazione() {
        return Valutazione;
    }

    public void setValutazione(String valutazione) {
        Valutazione = valutazione;
    }
}
