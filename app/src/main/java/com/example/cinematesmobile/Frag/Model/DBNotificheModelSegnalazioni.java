package com.example.cinematesmobile.Frag.Model;

public class DBNotificheModelSegnalazioni {

    private String UserSegnalato;
    private String UserSegnalatore;
    private String Motivazione;
    private String TitoloFilmRecensito;
    private String Stato;

    public DBNotificheModelSegnalazioni() {
    }

    public DBNotificheModelSegnalazioni(String userSegnalato, String userSegnalatore, String motivazione, String titoloFilmRecensito, String stato) {
        UserSegnalato = userSegnalato;
        UserSegnalatore = userSegnalatore;
        Motivazione = motivazione;
        TitoloFilmRecensito = titoloFilmRecensito;
        Stato = stato;
    }

    public String getUserSegnalato() {
        return UserSegnalato;
    }

    public void setUserSegnalato(String userSegnalato) {
        UserSegnalato = userSegnalato;
    }

    public String getUserSegnalatore() {
        return UserSegnalatore;
    }

    public void setUserSegnalatore(String userSegnalatore) {
        UserSegnalatore = userSegnalatore;
    }

    public String getMotivazione() {
        return Motivazione;
    }

    public void setMotivazione(String motivazione) {
        Motivazione = motivazione;
    }

    public String getTitoloFilmRecensito() {
        return TitoloFilmRecensito;
    }

    public void setTitoloFilmRecensito(String titoloFilmRecensito) {
        TitoloFilmRecensito = titoloFilmRecensito;
    }

    public String getStato() {
        return Stato;
    }

    public void setStato(String stato) {
        Stato = stato;
    }
}
