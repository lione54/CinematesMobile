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
            return FotoProfilo;
        }else {
            String UrlBase = "http://192.168.178.48/cinematesdb/";
            return UrlBase + FotoProfilo;
        }
    }

    public void setFotoProfilo(String fotoProfilo) {
        FotoProfilo = fotoProfilo;
    }
}
