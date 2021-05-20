package com.example.cinematesmobile.Recensioni.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelFotoProfilo {

    @SerializedName("Foto_Profilo")
    private String FotoProfilo;

    public DBModelFotoProfilo() {
    }

    public DBModelFotoProfilo(String fotoProfilo) {
        FotoProfilo = fotoProfilo;
    }

    public String getFotoProfilo() {
        if(FotoProfilo == null){
            return null;
        }else {
            String UrlBase = "http://18.116.88.169/cinematesdb/";
            return UrlBase + FotoProfilo;
        }
    }

    public void setFotoProfilo(String fotoProfilo) {
        FotoProfilo = fotoProfilo;
    }
}
