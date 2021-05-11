package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelDataUserResults {

    @SerializedName("Id_Utente")
    private Integer Id_Utente;
    @SerializedName("UserName")
    private String Username_Cercato;
    @SerializedName("Foto_Profilo")
    private String immagineProfilo;
    @SerializedName("Amicizia")
    private Integer EsisteAmicizia;
    @SerializedName("Amici_In_Comune")
    private Integer AmiciInComune;
    @SerializedName("UserCheCerca")
    private String UserCheCerca;

    public DBModelDataUserResults() {
    }

    public DBModelDataUserResults(String userCheCerca, String username_Cercato, String immagineProfilo, Integer Id_Utente, Integer esisteAmicizia, Integer amiciInComune) {
        UserCheCerca = userCheCerca;
        Username_Cercato = username_Cercato;
        this.immagineProfilo = immagineProfilo;
        this.Id_Utente = Id_Utente;
        EsisteAmicizia = esisteAmicizia;
        AmiciInComune = amiciInComune;
    }

    public String getUserCheCerca() {
        return UserCheCerca;
    }

    public void setUserCheCerca(String userCheCerca) {
        UserCheCerca = userCheCerca;
    }

    public String getUsername_Cercato() {
        return Username_Cercato;
    }

    public void setUsername_Cercato(String username_Cercato) {
        Username_Cercato = username_Cercato;
    }

    public String getImmagineProfilo() {
        if(immagineProfilo == null){
            return null;
        }else{
            String UrlBase = "http://192.168.1.9/cinematesdb/";
            return UrlBase + immagineProfilo;
        }
    }

    public void setImmagineProfilo(String immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }

    public Integer getId_Utente() {
        return Id_Utente;
    }

    public void setId_Utente(Integer id_Utente) {
        this.Id_Utente = id_Utente;
    }

    public Integer getEsisteAmicizia() {
        return EsisteAmicizia;
    }

    public void setEsisteAmicizia(Integer esisteAmicizia) {
        EsisteAmicizia = esisteAmicizia;
    }

    public Integer getAmiciInComune() {
        return AmiciInComune;
    }

    public void setAmiciInComune(Integer amiciInComune) {
        AmiciInComune = amiciInComune;
    }
}
