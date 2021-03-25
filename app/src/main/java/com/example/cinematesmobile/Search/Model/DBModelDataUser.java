package com.example.cinematesmobile.Search.Model;

public class DBModelDataUser {
    private String Username;
    private String immagineProfilo;
    private Integer id_utente;

    public DBModelDataUser() {
    }

    public DBModelDataUser(String username, String immagineProfilo, Integer id_utente) {
        Username = username;
        this.immagineProfilo = immagineProfilo;
        this.id_utente = id_utente;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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
}
