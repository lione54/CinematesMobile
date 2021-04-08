package com.example.cinematesmobile.Frag.Model;

public class DBNotificheModelRichiesteAmicizia {

    private String Utente;
    private String AmicoDi;
    private String Foto;
    private String StatoRichiesta;

    public DBNotificheModelRichiesteAmicizia() {
    }

    public DBNotificheModelRichiesteAmicizia(String utente, String amicoDi, String foto, String statoRichiesta) {
        Utente = utente;
        AmicoDi = amicoDi;
        Foto = foto;
        StatoRichiesta = statoRichiesta;
    }

    public String getUtente() {
        return Utente;
    }

    public void setUtente(String utente) {
        Utente = utente;
    }

    public String getAmicoDi() {
        return AmicoDi;
    }

    public void setAmicoDi(String amicoDi) {
        AmicoDi = amicoDi;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getStatoRichiesta() {
        return StatoRichiesta;
    }

    public void setStatoRichiesta(String statoRichiesta) {
        StatoRichiesta = statoRichiesta;
    }
}
