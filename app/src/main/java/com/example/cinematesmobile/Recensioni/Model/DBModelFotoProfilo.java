package com.example.cinematesmobile.Recensioni.Model;

import com.example.cinematesmobile.BuildConfig;
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
            String UrlBase = "http://" + BuildConfig.IP_AWS + "/cinematesdb/";
            return UrlBase + FotoProfilo;
        }
    }

    public void setFotoProfilo(String fotoProfilo) {
        FotoProfilo = fotoProfilo;
    }
}
