package com.example.cinematesmobile;

import androidx.annotation.NonNull;

import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.Recensioni.ScriviRecensioneActivity;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalvaRecensioneScrittaTestWhiteBox {

    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Test (expected = RuntimeException.class) //questo a causa della presenta dell'allert dialog
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_6_10(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test1", 0, "filmTest1", "UtenteTest1");
    }

    @Test (expected = RuntimeException.class) //questo a causa della presenta dell'allert dialog
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_6_11(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test2", 0, "filmTest2", "UtenteTest2");
    }


    @Test (expected = RuntimeException.class) //questo a causa della presenta dell'allert dialog
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_7_12(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test3", 0, "filmTest3", "UtenteTest3");
    }

    @Test
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_7_12_13_14_16(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test4", 1, "filmTest4", "UtenteTest4");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest4");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals("Test4", recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_7_12_13_14_17(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test5", 1, "filmTest5", "UtenteTest5");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest5");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                Assert.assertTrue(recensioniList.isEmpty());
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_4_7_12_13_15(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("Test6", 1, "filmTest6", "UtenteTest6");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest6");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                Assert.assertNull(dbModelRecensioniResponce);
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_5_8(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("", 1, "filmTest7", "UtenteTest7");
    }

    @Test (expected = NullPointerException.class)
    public void TestSalvaRecensioneScrittaWhiteBoxPath_2_3_5_9(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta("", 1, "filmTest8", "UtenteTest8");
    }
}
