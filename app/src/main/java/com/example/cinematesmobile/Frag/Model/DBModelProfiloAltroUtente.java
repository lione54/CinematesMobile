package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelProfiloAltroUtente {

    @SerializedName("UserName")
    private String UserName;
    @SerializedName("Nome")
    private String Nome;
    @SerializedName("Cognome")
    private String Cognome;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Foto_Profilo")
    private String Foto_Profilo;
    @SerializedName("Descrizione_Profilo")
    private String Descrizione_Profilo;
    @SerializedName("Data_Nascita")
    private String Data_Nascita;
    @SerializedName("Sesso")
    private String Sesso;
    @SerializedName("Totale_Amici")
    private String Totale_Amici;
    @SerializedName("Foto_Copertina")
    private String Foto_Copertina;
    @SerializedName("Recensioni_Scritte")
    private String Recensioni_Scritte;
    @SerializedName("Film_In_Comune")
    private String Film_In_Comune;


    public DBModelProfiloAltroUtente() {
    }

    public DBModelProfiloAltroUtente(String userName, String nome, String cognome, String email, String foto_Profilo, String descrizione_Profilo, String data_Nascita, String sesso, String totale_Amici, String foto_Copertina, String recensioni_Scritte, String film_In_Comune) {
        UserName = userName;
        Nome = nome;
        Cognome = cognome;
        Email = email;
        Foto_Profilo = foto_Profilo;
        Descrizione_Profilo = descrizione_Profilo;
        Data_Nascita = data_Nascita;
        Sesso = sesso;
        Totale_Amici = totale_Amici;
        Foto_Copertina = foto_Copertina;
        Recensioni_Scritte = recensioni_Scritte;
        Film_In_Comune = film_In_Comune;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFoto_Profilo() {
        if(Foto_Profilo == null){
            return null;
        }else {
            String UrlBase = "http://18.116.88.169/cinematesdb/";
            return UrlBase + Foto_Profilo;
        }
    }

    public void setFoto_Profilo(String foto_Profilo) {
        Foto_Profilo = foto_Profilo;
    }

    public String getDescrizione_Profilo() {
        return Descrizione_Profilo;
    }

    public void setDescrizione_Profilo(String descrizione_Profilo) {
        Descrizione_Profilo = descrizione_Profilo;
    }

    public String getData_Nascita() {
        return Data_Nascita;
    }

    public void setData_Nascita(String data_Nascita) {
        Data_Nascita = data_Nascita;
    }

    public String getSesso() {
        return Sesso;
    }

    public void setSesso(String sesso) {
        Sesso = sesso;
    }

    public String getTotale_Amici() {
        return Totale_Amici;
    }

    public void setTotale_Amici(String totale_Amici) {
        Totale_Amici = totale_Amici;
    }

    public String getFoto_Copertina() {
        if(Foto_Copertina == null){
            return null;
        }else {
            String UrlBase = "http://18.116.88.169/cinematesdb/";
            return UrlBase + Foto_Copertina;
        }
    }

    public void setFoto_Copertina(String foto_Copertina) {
        Foto_Copertina = foto_Copertina;
    }

    public String getRecensioni_Scritte() {
        return Recensioni_Scritte;
    }

    public void setRecensioni_Scritte(String recensioni_Scritte) {
        Recensioni_Scritte = recensioni_Scritte;
    }

    public String getFilm_In_Comune() {
        return Film_In_Comune;
    }

    public void setFilm_In_Comune(String film_In_Comune) {
        Film_In_Comune = film_In_Comune;
    }

}
