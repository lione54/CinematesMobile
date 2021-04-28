package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelVerificaResults {

    @SerializedName("Verifica")
    private Integer CodVerifica;

    public DBModelVerificaResults() {
    }

    public DBModelVerificaResults(Integer codVerifica) {
        CodVerifica = codVerifica;
    }

    public Integer getCodVerifica() {
        return CodVerifica;
    }

    public void setCodVerifica(Integer codVerifica) {
        CodVerifica = codVerifica;
    }
}
