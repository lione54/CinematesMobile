package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Search.ActivityProfiloAltroUtente;
import com.example.cinematesmobile.Frag.Model.DBModelDataUserResults;
import com.example.cinematesmobile.Search.VisualizzaAmiciInComuneActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RicercaUtenteAdapter extends RecyclerView.Adapter<RicercaUtenteAdapter.DataViewHolder> {

    private Activity activity;
    private List<DBModelDataUserResults> dataList;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    public RicercaUtenteAdapter(Activity activity, List<DBModelDataUserResults> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_utenti_search, parent, false);
        return new DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelDataUserResults data = dataList.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(data.getImmagineProfilo() == null){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }else{
            Glide.with(activity).load(data.getImmagineProfilo()).into(holder.FotoProfilo);
        }
        holder.Username.setText(data.getUsername_Cercato());
        if(data.getEsisteAmicizia() == 1){
            holder.DettagliAmicizia.setVisibility(View.GONE);
            holder.InviaAmicizia.setEnabled(false);
            holder.InviaAmicizia.setText("Amici");
        }else{
            if(data.getAmiciInComune() != null){
                if(data.getAmiciInComune().equals("1")){
                    holder.DettagliAmicizia.setText(new StringBuilder().append(data.getAmiciInComune()).append(" Amico in comune").toString());
                }else{
                    holder.DettagliAmicizia.setText(new StringBuilder().append(data.getAmiciInComune()).append(" Amici in comune").toString());
                }
            }else{
                    holder.DettagliAmicizia.setText(new StringBuilder().append(0).append(" Amici in comune").toString());
            }
            holder.DettagliAmicizia.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(activity, VisualizzaAmiciInComuneActivity.class);
                    intent.putExtra("Nome_Utente_Cercato", data.getUsername_Cercato());
                    intent.putExtra("Nome_Proprietario", data.getUserCheCerca());
                    activity.startActivity(intent);
                }
            });
            Call<DBModelVerifica> dbModelVerificaCall = retrofitServiceDBInterno.getVerificaRichiestaDiAmicizia(data.getUserCheCerca(), data.getUsername_Cercato());
            dbModelVerificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> modelVerificaResults = dbModelVerifica.getResults();
                        if(modelVerificaResults.get(0).getCodVerifica() == 0){
                            holder.InviaAmicizia.setVisibility(View.VISIBLE);
                            holder.InviaAmicizia.setText("Invia richiesta");
                        }else{
                            holder.InviaAmicizia.setEnabled(false);
                            holder.InviaAmicizia.setText("Richiesta già inviata");
                        }
                    }else{
                        Toast.makeText(activity, "Impossibile verificare stato richiesta", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                    Toast.makeText(activity, "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                }
            });
        }
        holder.InviaAmicizia.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelResponseToInsert> dbModelResponseToInsertCall = retrofitServiceDBInterno.InviaRichiestaAmicizia(dataList.get(position).getUserCheCerca(), dataList.get(position).getUsername_Cercato());
                dbModelResponseToInsertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                        if(dbModelResponseToInsert != null){
                            if (dbModelResponseToInsert.getStato().equals("Successfull")){
                                Call<DBModelResponseToInsert> notificaAmiciziaCall = retrofitServiceDBInterno.NotificaRichiestaAmicizia(dataList.get(position).getUsername_Cercato(), dataList.get(position).getUserCheCerca());
                                notificaAmiciziaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                    @Override public void onResponse(Call<DBModelResponseToInsert> call, Response<DBModelResponseToInsert> response) {
                                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                        if(dbModelResponseToInsert != null) {
                                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                Toast.makeText(activity, "Rchiesta di amicizia inviata a " + data.getUsername_Cercato(), Toast.LENGTH_LONG).show();
                                                holder.InviaAmicizia.setEnabled(false);
                                                holder.InviaAmicizia.setText("Richiesta inviata");
                                            }
                                        }
                                    }
                                    @Override public void onFailure(Call<DBModelResponseToInsert> call, Throwable t) {
                                        Toast.makeText(activity, "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Invio richiesta di amicizia a " + data.getUsername_Cercato() + " Fallito.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(activity, "Impossibile inviare richiesta", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                        Toast.makeText(activity, "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityProfiloAltroUtente.class);
                intent.putExtra("UsernameAltroUtente", data.getUsername_Cercato());
                intent.putExtra("UsernameProprietario", data.getUserCheCerca());
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DettagliAmicizia;
        public AppCompatButton InviaAmicizia;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user);
            Username = itemView.findViewById(R.id.username_ricerca);
            InviaAmicizia = itemView.findViewById(R.id.Invia_amicizia);
            DettagliAmicizia = itemView.findViewById(R.id.chi_lo_conosce);
        }
    }
}
