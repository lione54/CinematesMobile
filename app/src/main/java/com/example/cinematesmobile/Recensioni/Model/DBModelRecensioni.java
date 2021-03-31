package com.example.cinematesmobile.Recensioni.Model;

import java.util.Date;

public class DBModelRecensioni {

    private Integer Id_Recensione;
    private Integer Id_Film_Recensioni;
    private Integer Valutazione;
    private String User_Recensore;
    private Date Data_Pubblicazione;
    private String Testo_Recensione;

    public DBModelRecensioni() {
    }

    public DBModelRecensioni(Integer id_Recensione, Integer id_Film_Recensioni, Integer valutazione, String user_Recensore, Date data_Pubblicazione, String testo_Recensione) {
        Id_Recensione = id_Recensione;
        Id_Film_Recensioni = id_Film_Recensioni;
        Valutazione = valutazione;
        User_Recensore = user_Recensore;
        Data_Pubblicazione = data_Pubblicazione;
        Testo_Recensione = testo_Recensione;
    }

    public Integer getId_Recensione() {
        return Id_Recensione;
    }

    public void setId_Recensione(Integer id_Recensione) {
        Id_Recensione = id_Recensione;
    }

    public Integer getId_Film_Recensioni() {
        return Id_Film_Recensioni;
    }

    public void setId_Film_Recensioni(Integer id_Film_Recensioni) {
        Id_Film_Recensioni = id_Film_Recensioni;
    }

    public Integer getValutazione() {
        return Valutazione;
    }

    public void setValutazione(Integer valutazione) {
        Valutazione = valutazione;
    }

    public String getUser_Recensore() {
        return User_Recensore;
    }

    public void setUser_Recensore(String user_Recensore) {
        User_Recensore = user_Recensore;
    }

    public Date getData_Pubblicazione() {
        return Data_Pubblicazione;
    }

    public void setData_Pubblicazione(Date data_Pubblicazione) {
        Data_Pubblicazione = data_Pubblicazione;
    }

    public String getTesto_Recensione() {
        return Testo_Recensione;
    }

    public void setTesto_Recensione(String testo_Recensione) {
        Testo_Recensione = testo_Recensione;
    }
}
