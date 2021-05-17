package com.example.cinematesmobile.ModelDBInterno;

import com.example.cinematesmobile.Frag.Model.DBModelNotifiche;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DBModelNotificheResponce {

    @SerializedName("dbdata")
    private List<DBModelNotifiche> results;

    public DBModelNotificheResponce() {
    }

    public DBModelNotificheResponce(List<DBModelNotifiche> results) {
        this.results = results;
    }

    public List<DBModelNotifiche> getResults() {
        return results;
    }

    public void setResults(List<DBModelNotifiche> results) {
        this.results = results;
    }
}
