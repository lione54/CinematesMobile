package com.example.cinematesmobile.Search.Model;

public class DBModelDataUser {

    private String UserCheCerca;
    private String Username_Cercato;
    private String immagineProfilo;
    private Integer id_utente;
    private Integer EsisteAmicizia;
    private Integer AmiciInComune;

    public DBModelDataUser() {
    }

    public DBModelDataUser(String userCheCerca, String username_Cercato, String immagineProfilo, Integer id_utente, Integer esisteAmicizia, Integer amiciInComune) {
        UserCheCerca = userCheCerca;
        Username_Cercato = username_Cercato;
        this.immagineProfilo = immagineProfilo;
        this.id_utente = id_utente;
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
        return immagineProfilo;
    }

    public void setImmagineProfilo(String immagineProfilo) {
        this.immagineProfilo = immagineProfilo;
    }

    public Integer getId_utente() {
        return id_utente;
    }

    public void setId_utente(Integer id_utente) {
        this.id_utente = id_utente;
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
