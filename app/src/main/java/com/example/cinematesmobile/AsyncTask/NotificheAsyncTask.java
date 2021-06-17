package com.example.cinematesmobile.AsyncTask;

import android.app.Activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;

import com.example.cinematesmobile.Frag.CommentiActivity;
import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Frag.Model.DBModelNotifiche;
import com.example.cinematesmobile.ModelDBInterno.DBModelNotificheResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Comment;

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
                            @Override public void onResponse(@NotNull Call<DBModelNotificheResponce> call,@NotNull Response<DBModelNotificheResponce> response) {
                                DBModelNotificheResponce dbModelNotificheResponce = response.body();
                                if(dbModelNotificheResponce != null){
                                    List<DBModelNotifiche> notificheList = dbModelNotificheResponce.getResults();
                                    if(!(notificheList.isEmpty())){
                                        for (int i = 0; i < notificheList.size(); i++) {
                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(activity,"Notifiche");
                                            builder.setContentTitle(notificheList.get(i).getTipo_Notifica());
                                            if (notificheList.get(i).getTipo_Notifica().equals("Like")) {
                                                if(notificheList.get(i).getQuale_post().equals("Recensioni")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo like alla tua recensione di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getQuale_post().equals("Commento")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo like al tuo commento di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getQuale_post().equals("Lista")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo like alla tua lista " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }
                                            } else if (notificheList.get(i).getTipo_Notifica().equals("Dislike")) {
                                                if (notificheList.get(i).getQuale_post().equals("Recensioni")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo dislike alla tua recensione di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                } else if (notificheList.get(i).getQuale_post().equals("Commento")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo dislike al tuo commento di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getQuale_post().equals("Lista")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha messo dislike alla tua lista " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }
                                            }else if (notificheList.get(i).getTipo_Notifica().equals("Commento")){
                                                if (notificheList.get(i).getQuale_post().equals("Recensioni")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha commentato la tua recensione di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, CommentiActivity.class);
                                                    toFragment.putExtra("UsernameProprietario", NomeUtente);
                                                    toFragment.putExtra("UsernameAltroUtente",NomeUtente);
                                                    toFragment.putExtra("TipoCorrente",notificheList.get(i).getQuale_post());
                                                    toFragment.putExtra("TitoloFilm",notificheList.get(i).getArgomento_notifica());
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getQuale_post().equals("Lista")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha commentato la tua lista " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, CommentiActivity.class);
                                                    toFragment.putExtra("UsernameProprietario", NomeUtente);
                                                    toFragment.putExtra("UsernameAltroUtente",notificheList.get(i).getUser_che_fa_azione());
                                                    toFragment.putExtra("TipoCorrente",notificheList.get(i).getQuale_post());
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    toFragment.putExtra("TitoloLista",notificheList.get(i).getArgomento_notifica());
                                                    toFragment.putExtra("DiChiè","Mia");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }
                                            }else if (notificheList.get(i).getTipo_Notifica().equals("Richiesta")){
                                                if (notificheList.get(i).getArgomento_notifica().equals("Inviata")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ti ha inviato una richiesta di amicizia");
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getArgomento_notifica().equals("Accettata")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha accettato la tua richiesta di amicizia ");
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }
                                            }else if (notificheList.get(i).getTipo_Notifica().equals("Segnalazione")){
                                                if (notificheList.get(i).getArgomento_notifica().equals("Declinata")) {
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha declinato la segnalazione della recensione di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }else if(notificheList.get(i).getArgomento_notifica().equals("Accettata")){
                                                    builder.setContentText(notificheList.get(i).getUser_che_fa_azione() + " ha accettato la segnalazione della recensione di " + notificheList.get(i).getArgomento_notifica());
                                                    Intent toFragment = new Intent(activity, FragmentActivity.class);
                                                    toFragment.putExtra("UserProprietario", NomeUtente);
                                                    toFragment.putExtra("ClickNotifica","Yes");
                                                    PendingIntent pendingIntent = PendingIntent.getActivity(activity, 201, toFragment, PendingIntent.FLAG_UPDATE_CURRENT);
                                                    builder.setContentIntent(pendingIntent);
                                                    builder.setFullScreenIntent(pendingIntent, true);
                                                }
                                            }
                                            builder.setSmallIcon(R.drawable.ic_oscar);
                                            builder.setAutoCancel(true);
                                            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
                                            notificationManager.notify(i, builder.build());
                                        }
                                    }
                                }
                            }
                            @Override public void onFailure(@NotNull Call<DBModelNotificheResponce> call,@NotNull Throwable t) {

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
