package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelRecuperaUsername {

    @SerializedName("dbdata")
    private String Username;

    public DBModelRecuperaUsername() {
    }

    public DBModelRecuperaUsername(String username) {
        Username = username;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
