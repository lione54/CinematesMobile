package com.example.cinematesmobile.ModelDBInterno;

import com.google.gson.annotations.SerializedName;

public class DBModelVerificaResults {

    @SerializedName("Verifica")
    private int CodVerifica;

    public DBModelVerificaResults() {
    }

    public DBModelVerificaResults(int codVerifica) {
        CodVerifica = codVerifica;
    }

    public int getCodVerifica() {
        return CodVerifica;
    }

    public void setCodVerifica(int codVerifica) {
        CodVerifica = codVerifica;
    }
}
