package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelEmoj {

    @SerializedName("Num_like")
    private String NumeroLike;
    @SerializedName("Num_dislike")
    private String NumeroDislike;
    @SerializedName("Tipo_Reaction")
    private String Tipo_Emoj;
    @SerializedName("Dove_ha_inserito_emoj")
    private String Dove_e_inserita;
    @SerializedName("quale_post")
    private String In_quale_post;
    @SerializedName("User_Emoj")
    private String User_che_ha_inserito_emoj;
    @SerializedName("User_Proprietario")
    private String User_Proprietario_post;

    public DBModelEmoj() {
    }

    public DBModelEmoj(String tipo_Emoj, String dove_e_inserita, String in_quale_post, String user_che_ha_inserito_emoj, String user_Proprietario_post, String numeroLike, String numeroDislike) {
        Tipo_Emoj = tipo_Emoj;
        Dove_e_inserita = dove_e_inserita;
        In_quale_post = in_quale_post;
        User_che_ha_inserito_emoj = user_che_ha_inserito_emoj;
        User_Proprietario_post = user_Proprietario_post;
        NumeroLike = numeroLike;
        NumeroDislike = numeroDislike;
    }

    public String getTipo_Emoj() {
        if(Tipo_Emoj == null){
            return null;
        }else {
            return Tipo_Emoj;
        }
    }

    public void setTipo_Emoj(String tipo_Emoj) {
        Tipo_Emoj = tipo_Emoj;
    }

    public String getDove_e_inserita() {
        return Dove_e_inserita;
    }

    public void setDove_e_inserita(String dove_e_inserita) {
        Dove_e_inserita = dove_e_inserita;
    }

    public String getIn_quale_post() {
        return In_quale_post;
    }

    public void setIn_quale_post(String in_quale_post) {
        In_quale_post = in_quale_post;
    }

    public String getUser_che_ha_inserito_emoj() {
        return User_che_ha_inserito_emoj;
    }

    public void setUser_che_ha_inserito_emoj(String user_che_ha_inserito_emoj) {
        User_che_ha_inserito_emoj = user_che_ha_inserito_emoj;
    }

    public String getUser_Proprietario_post() {
        return User_Proprietario_post;
    }

    public void setUser_Proprietario_post(String user_Proprietario_post) {
        User_Proprietario_post = user_Proprietario_post;
    }

    public String getNumeroLike() {
        return NumeroLike;
    }

    public void setNumeroLike(String numeroLike) {
        NumeroLike = numeroLike;
    }

    public String getNumeroDislike() {
        return NumeroDislike;
    }

    public void setNumeroDislike(String numeroDislike) {
        NumeroDislike = numeroDislike;
    }
}
