package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelRecuperaPasswdResponce {

    @SerializedName("dbdata")
    private String Passwd;

    public DBModelRecuperaPasswdResponce() {
    }

    public DBModelRecuperaPasswdResponce(String passwd) {
        Passwd = passwd;
    }

    public String getPasswd() {
        return Passwd;
    }

    public void setPasswd(String passwd) {
        Passwd = passwd;
    }
}
