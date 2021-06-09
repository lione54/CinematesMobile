package com.example.cinematesmobile.AsyncTask;

import android.app.Activity;
import android.app.Notification;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinematesmobile.Frag.Adapter.NotificationAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelNotifiche;
import com.example.cinematesmobile.ModelDBInterno.DBModelNotificheResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificheAsyncTask extends AsyncTask<Void, Void, Void> {

    private String NomeUtente;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private Activity activity;

    public NotificheAsyncTask(String nomeUtente, Activity activity) {
        NomeUtente = nomeUtente;
        this.activity = activity;
    }

    @Override protected Void doInBackground(Void... voids) {
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelVerifica> verificanotificheCall = retrofitServiceDBInterno.VerificaSeCiSonoNotifiche(NomeUtente);
        verificanotificheCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NotNull Call<DBModelVerifica> call, @NotNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null){
                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                    if(verificaResults.get(0).getCodVerifica() == 1){
                        Call<DBModelNotificheResponce> notificheResponceCall = retrofitServiceDBInterno.PrendiNotificheDaDB(NomeUtente);
                        notificheResponceCall.enqueue(new Callback<DBModelNotificheResponce>() {
                            @Override public void onResponse(Call<DBModelNotificheResponce> call, Response<DBModelNotificheResponce> response) {
                                DBModelNotificheResponce dbModelNotificheResponce = response.body();
                                if(dbModelNotificheResponce != null){
                                    List<DBModelNotifiche> notificheList = dbModelNotificheResponce.getResults();
                                    if(!(notificheList.isEmpty())){
                                        for (int i = 0; i < notificheList.size(); i++) {
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, notificheList.get(i).getTipo_Notifica());
                                        }
                                    }
                                }
                            }
                            @Override public void onFailure(Call<DBModelNotificheResponce> call, Throwable t) {

                            }
                        });
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelVerifica> call, @NotNull Throwable t) {
            }
        });
        return null;
    }
}
