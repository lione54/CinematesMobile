package com.example.cinematesmobile.Recensioni.Model;

import java.util.Date;

public class DBModelRecensioni {

    private Integer Id_Recensione;
    private Float Valutazione;
    private String User_Recensore;
    private String Data_Pubblicazione;
    private String Testo_Recensione;
    private String Titolo_Film;
    private String Foto;

    public DBModelRecensioni() {
    }

    public DBModelRecensioni(Integer id_Recensione, Float valutazione, String user_Recensore, String data_Pubblicazione, String testo_Recensione, String titolo_Film, String foto) {
        Id_Recensione = id_Recensione;
        Valutazione = valutazione;
        User_Recensore = user_Recensore;
        Data_Pubblicazione = data_Pubblicazione;
        Testo_Recensione = testo_Recensione;
        Titolo_Film = titolo_Film;
        Foto = foto;
    }

    public Integer getId_Recensione() {
        return Id_Recensione;
    }

    public void setId_Recensione(Integer id_Recensione) {
        Id_Recensione = id_Recensione;
    }

    public Float getValutazione() {
        return Valutazione;
    }

    public void setValutazione(Float valutazione) {
        Valutazione = valutazione;
    }

    public String getUser_Recensore() {
        return User_Recensore;
    }

    public void setUser_Recensore(String user_Recensore) {
        User_Recensore = user_Recensore;
    }

    public String getData_Pubblicazione() {
        return Data_Pubblicazione;
    }

    public void setData_Pubblicazione(String data_Pubblicazione) {
        Data_Pubblicazione = data_Pubblicazione;
    }

    public String getTesto_Recensione() {
        return Testo_Recensione;
    }

    public void setTesto_Recensione(String testo_Recensione) {
        Testo_Recensione = testo_Recensione;
    }

    public String getTitolo_Film() {
        return Titolo_Film;
    }

    public void setTitolo_Film(String titolo_Film) {
        Titolo_Film = titolo_Film;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
