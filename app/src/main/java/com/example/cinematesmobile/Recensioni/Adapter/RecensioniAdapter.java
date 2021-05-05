package com.example.cinematesmobile.Recensioni.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Segnalazioni.SegnalazioniActivity;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecensioniAdapter extends RecyclerView.Adapter<RecensioniAdapter.DataViewHolder>{

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private String User_Segnalatore;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    public RecensioniAdapter(Activity activity, List<DBModelRecensioni> recensioniList, String user_Segnalatore) {
        this.activity = activity;
        this.recensioniList = recensioniList;
        User_Segnalatore = user_Segnalatore;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_recensioni_scritte, parent, false);
        return new RecensioniAdapter.DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(dbModelRecensioni.getFoto().equals("null")){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getUser_Recensore());
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
        holder.Segnala.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelVerifica> verificaSegnalazioniCall = retrofitServiceDBInterno.VerificaPresenzaSegnalazione(User_Segnalatore, dbModelRecensioni.getUser_Recensore(), String.valueOf(dbModelRecensioni.getId_Recensione()));
                verificaSegnalazioniCall.enqueue(new Callback<DBModelVerifica>() {
                    @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if(dbModelVerifica != null) {
                            List<DBModelVerificaResults> modelVerificaResults = dbModelVerifica.getResults();
                            if (modelVerificaResults.get(0).getCodVerifica() == 0) {
                                Intent intent2 = new Intent(activity, SegnalazioniActivity.class);
                                intent2.putExtra("Nome_Utente_Segnalatore", User_Segnalatore);
                                intent2.putExtra("Nome_Utente_Segnalato", dbModelRecensioni.getUser_Recensore());
                                intent2.putExtra("Foto_Profilo", dbModelRecensioni.getFoto());
                                intent2.putExtra("Id_Recensione", String.valueOf(dbModelRecensioni.getId_Recensione()));
                                activity.startActivity(intent2);
                            }else{
                                Toast.makeText(activity, "Hai già segnalato questa recensione.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Impossibile verificare se hai già degnalato questa recensione.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                        Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione;
        public AppCompatButton Segnala;
        public RatingBar Voto;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.username_ricerca);
            DataRecensione = itemView.findViewById(R.id.data_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_recensione);
            Segnala = itemView.findViewById(R.id.segnalazioni_button);
            Voto = itemView.findViewById(R.id.valutazione_db);
        }
    }
}
