package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelResponseToInsert {

    @SerializedName("dbdata")
    private String stato;

    public DBModelResponseToInsert() {
    }

    public DBModelResponseToInsert(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
}
