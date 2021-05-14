package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelCommenti {

    @SerializedName("Dove_ha_Inserito_commento")
    private String Dove_ha_Inserito_commento;
    @SerializedName("quale_post")
    private String quale_post;
    @SerializedName("Chi_ha_Commentato")
    private String Chi_ha_Commentato;
    @SerializedName("Proprietario_Post")
    private String Proprietario_Post;
    @SerializedName("testo_commento")
    private String testo_commento;
    @SerializedName("quando_ha_commentato")
    private String quando_ha_commentato;

    public DBModelCommenti() {
    }

    public DBModelCommenti(String dove_ha_Inserito_commento, String quale_post, String chi_ha_Commentato, String proprietario_Post, String testo_commento, String quando_ha_commentato) {
        Dove_ha_Inserito_commento = dove_ha_Inserito_commento;
        this.quale_post = quale_post;
        Chi_ha_Commentato = chi_ha_Commentato;
        Proprietario_Post = proprietario_Post;
        this.testo_commento = testo_commento;
        this.quando_ha_commentato = quando_ha_commentato;
    }

    public String getDove_ha_Inserito_commento() {
        return Dove_ha_Inserito_commento;
    }

    public void setDove_ha_Inserito_commento(String dove_ha_Inserito_commento) {
        Dove_ha_Inserito_commento = dove_ha_Inserito_commento;
    }

    public String getQuale_post() {
        return quale_post;
    }

    public void setQuale_post(String quale_post) {
        this.quale_post = quale_post;
    }

    public String getChi_ha_Commentato() {
        return Chi_ha_Commentato;
    }

    public void setChi_ha_Commentato(String chi_ha_Commentato) {
        Chi_ha_Commentato = chi_ha_Commentato;
    }

    public String getProprietario_Post() {
        return Proprietario_Post;
    }

    public void setProprietario_Post(String proprietario_Post) {
        Proprietario_Post = proprietario_Post;
    }

    public String getTesto_commento() {
        return testo_commento;
    }

    public void setTesto_commento(String testo_commento) {
        this.testo_commento = testo_commento;
    }

    public String getQuando_ha_commentato() {
        return quando_ha_commentato;
    }

    public void setQuando_ha_commentato(String quando_ha_commentato) {
        this.quando_ha_commentato = quando_ha_commentato;
    }
}
