package com.example.cinematesmobile.Frag.Model;

import com.example.cinematesmobile.BuildConfig;
import com.google.gson.annotations.SerializedName;

public class DBModelNotifiche {

    @SerializedName("Tipo_Notifica")
    private String Tipo_Notifica;
    @SerializedName("quale_post")
    private String quale_post;
    @SerializedName("Argomento_notifica")
    private String Argomento_notifica;
    @SerializedName("User_che_fa_azione")
    private String User_che_fa_azione;
    @SerializedName("UserSegnalato")
    private String UserSegnalato;
    @SerializedName("Motivazione")
    private String Motivazione;
    @SerializedName("Titolo_Film")
    private String Titolo_Film;
    @SerializedName("Foto_Profilo")
    private String Foto_Profilo;

    public DBModelNotifiche() {
    }

    public DBModelNotifiche(String tipo_Notifica, String quale_post, String argomento_notifica, String user_che_fa_azione, String userSegnalato, String motivazione, String titolo_Film, String foto_Profilo) {
        Tipo_Notifica = tipo_Notifica;
        this.quale_post = quale_post;
        Argomento_notifica = argomento_notifica;
        User_che_fa_azione = user_che_fa_azione;
        UserSegnalato = userSegnalato;
        Motivazione = motivazione;
        Titolo_Film = titolo_Film;
        Foto_Profilo = foto_Profilo;
    }

    public String getTipo_Notifica() {
        return Tipo_Notifica;
    }

    public void setTipo_Notifica(String tipo_Notifica) {
        Tipo_Notifica = tipo_Notifica;
    }

    public String getQuale_post() {
        return quale_post;
    }

    public void setQuale_post(String quale_post) {
        this.quale_post = quale_post;
    }

    public String getArgomento_notifica() {
        return Argomento_notifica;
    }

    public void setArgomento_notifica(String argomento_notifica) {
        Argomento_notifica = argomento_notifica;
    }

    public String getUser_che_fa_azione() {
        return User_che_fa_azione;
    }

    public void setUser_che_fa_azione(String user_che_fa_azione) {
        User_che_fa_azione = user_che_fa_azione;
    }

    public String getUserSegnalato() {
        return UserSegnalato;
    }

    public void setUserSegnalato(String userSegnalato) {
        UserSegnalato = userSegnalato;
    }

    public String getMotivazione() {
        return Motivazione;
    }

    public void setMotivazione(String motivazione) {
        Motivazione = motivazione;
    }

    public String getTitolo_Film() {
        return Titolo_Film;
    }

    public void setTitolo_Film(String titolo_Film) {
        Titolo_Film = titolo_Film;
    }

    public String getFoto_Profilo() {
        if(Foto_Profilo == null){
            return null;
        }else{
            String UrlBase = "http://" + BuildConfig.IP_AWS + "/cinematesdb/";
            return UrlBase + Foto_Profilo;
        }
    }

    public void setFoto_Profilo(String foto_Profilo) {
        Foto_Profilo = foto_Profilo;
    }
}
