package com.example.cinematesmobile.Frag.Model;

import com.google.gson.annotations.SerializedName;

public class DBModelVoti {

    @SerializedName("Valutazione")
    private Double Valutazione_Media;

    public DBModelVoti() {
    }

    public DBModelVoti(Double valutazione_Media) {
        Valutazione_Media = valutazione_Media;
    }

    public Double getValutazione_Media() {
        return Valutazione_Media;
    }

    public void setValutazione_Media(Double valutazione_Media) {
        Valutazione_Media = valutazione_Media;
    }
}
