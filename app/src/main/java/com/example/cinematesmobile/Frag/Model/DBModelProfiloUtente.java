package com.example.cinematesmobile.Frag.Model;

import com.example.cinematesmobile.BuildConfig;
import com.google.gson.annotations.SerializedName;

public class DBModelProfiloUtente {

    @SerializedName("UserName")
    private String UserName;
    @SerializedName("Nome")
    private String Nome;
    @SerializedName("Cognome")
    private String Cognome;
    @SerializedName("Email")
    private String Email;
    @SerializedName("Passwd")
    private String Passwd;
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
    @SerializedName("Numero_Liste_Personalizzate")
    private String Numero_Liste_Personalizzate;
    @SerializedName("Recensioni_Scritte")
    private String Recensioni_Scritte;

    public DBModelProfiloUtente() {
    }

    public DBModelProfiloUtente(String userName, String nome, String cognome, String email, String passwd, String foto_Profilo, String descrizione_Profilo, String data_Nascita, String sesso, String totale_Amici, String foto_Copertina, String numero_Liste_Personalizzate, String recensioni_Scritte) {
        UserName = userName;
        Nome = nome;
        Cognome = cognome;
        Email = email;
        Passwd = passwd;
        Foto_Profilo = foto_Profilo;
        Descrizione_Profilo = descrizione_Profilo;
        Data_Nascita = data_Nascita;
        Sesso = sesso;
        Totale_Amici = totale_Amici;
        Foto_Copertina = foto_Copertina;
        Numero_Liste_Personalizzate = numero_Liste_Personalizzate;
        Recensioni_Scritte = recensioni_Scritte;
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

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }

    public String getFoto_Profilo() {
        if(Foto_Profilo == null){
            return null;
        }else {
            String UrlBase = "http://"+ BuildConfig.IP_AWS + "/cinematesdb/";
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
            String UrlBase = "http://" + BuildConfig.IP_AWS + "/cinematesdb/";
            return UrlBase + Foto_Copertina;
        }
    }

    public void setFoto_Copertina(String foto_Copertina) {
        Foto_Copertina = foto_Copertina;
    }

    public String getNumero_Liste_Personalizzate() {
        return Numero_Liste_Personalizzate;
    }

    public void setNumero_Liste_Personalizzate(String numero_Liste_Personalizzate) {
        Numero_Liste_Personalizzate = numero_Liste_Personalizzate;
    }

    public String getRecensioni_Scritte() {
        return Recensioni_Scritte;
    }

    public void setRecensioni_Scritte(String recensioni_Scritte) {
        Recensioni_Scritte = recensioni_Scritte;
    }
}
