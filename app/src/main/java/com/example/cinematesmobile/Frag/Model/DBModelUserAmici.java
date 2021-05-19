package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelUserAmici {

    @SerializedName("User_Proprietario")
    private String Utente;
    @SerializedName("E_Amico_Di")
    private String E_Amico_Di;
    @SerializedName("Foto_Profilo")
    private String Foto_Profilo;

    public DBModelUserAmici() {
    }

    public DBModelUserAmici(String utente, String e_Amico_Di, String foto_Profilo) {
        Utente = utente;
        E_Amico_Di = e_Amico_Di;
        Foto_Profilo = foto_Profilo;
    }

    public String getUtente() {
        return Utente;
    }

    public void setUtente(String utente) {
        Utente = utente;
    }

    public String getE_Amico_Di() {
        return E_Amico_Di;
    }

    public void setE_Amico_Di(String e_Amico_Di) {
        E_Amico_Di = e_Amico_Di;
    }

    public String getFoto_Profilo() {
        if(Foto_Profilo == null){
            return null;
        }else {
            String UrlBase = "http://192.168.178.489/cinematesdb/";
            return UrlBase + Foto_Profilo;
        }
    }

    public void setFoto_Profilo(String foto_Profilo) {
        Foto_Profilo = foto_Profilo;
    }
}
