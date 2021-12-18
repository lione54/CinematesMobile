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

public class SalvaRecensioneScrittaTestBlackBox {

    private final static String RECE_CE1 = "";
    private final static String RECE_CE2 = "Questa è una recensione.";
    private final static String RECE_CE3 = "Questa è una recensione di test per verificare come si comporta  il metodo utilizzato per salvare le recensioni quando viene inserita una recensione che supera i seicento caratteri, che a questo punto non sono stati raggiunti quindi sto continuando a scrivere sperando che il momento in cui si arrivi a questi seicento caratteri, che pensavo fossero anche pochi per scrivere una recensione, siano raggiunti. Non so più cosa scrivere e siamo a soli quattrocentoquarant'otto caratteri che ovviamente adesso non saranno più quattrocentoquarant'otto ma cinquecentocinquanta, sono quasi arrivato al traguardo finalmente.";
    private final static float PUNT_CE1 = 0;
    private final static float PUNT_CE2 = 2.5F;
    private final static float PUNT_CE3 = 6.0F;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Test (expected = NullPointerException.class)
    public void TestMetodoRECE_CE1_PUNT_CE1(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE1, PUNT_CE1, "filmTest1", "UtenteTest1");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest1");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE1, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void TestMetodoRECE_CE1_PUNT_CE2(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE1, PUNT_CE2, "filmTest2", "UtenteTest2");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest2");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE1, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void TestMetodoRECE_CE1_PUNT_CE3(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE1, PUNT_CE2, "filmTest3", "UtenteTest3");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest3");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE1, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoRECE_CE2_PUNT_CE1(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE2, PUNT_CE1, "filmTest4", "UtenteTest4");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest4");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE2, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test
    public void TestMetodoRECE_CE2_PUNT_CE2(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE2, PUNT_CE2, "filmTest5", "UtenteTest5");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest5");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE2, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test(expected = RuntimeException.class)
    public void TestMetodoRECE_CE2_PUNT_CE3(){
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE2, PUNT_CE3, "filmTest6", "UtenteTest6");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest6");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni>  recensioniList = dbModelRecensioniResponce.getResults();
                if(!(recensioniList.isEmpty())){
                    Assert.assertEquals(RECE_CE2, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoRECE_CE3_PUNT_CE1() {
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE3, PUNT_CE1, "filmTest7", "UtenteTest7");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest7");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni> recensioniList = dbModelRecensioniResponce.getResults();
                if (!(recensioniList.isEmpty())) {
                    Assert.assertEquals(RECE_CE3, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoRECE_CE3_PUNT_CE2() {
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE3, PUNT_CE2, "filmTest8", "UtenteTest8");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest8");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni> recensioniList = dbModelRecensioniResponce.getResults();
                if (!(recensioniList.isEmpty())) {
                    Assert.assertEquals(RECE_CE3, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoRECE_CE3_PUNT_CE3() {
        ScriviRecensioneActivity scriviRecensioneActivity = new ScriviRecensioneActivity();
        scriviRecensioneActivity.SalvaRecensioneScritta(RECE_CE3, PUNT_CE3, "filmTest9", "UtenteTest9");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiMieRecensioni("UtenteTest9");
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                List<DBModelRecensioni> recensioniList = dbModelRecensioniResponce.getResults();
                if (!(recensioniList.isEmpty())) {
                    Assert.assertEquals(RECE_CE3, recensioniList.get(0).getTesto_Recensione());
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call, @NonNull Throwable t) {

            }
        });
    }
}
