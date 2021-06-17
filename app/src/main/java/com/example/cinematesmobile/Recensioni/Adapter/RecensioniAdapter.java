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
import com.example.cinematesmobile.Frag.CommentiActivity;
import com.example.cinematesmobile.Frag.Model.DBModelEmoj;
import com.example.cinematesmobile.ModelDBInterno.DBModelEmojResponde;
import com.example.cinematesmobile.ModelDBInterno.DBModelFotoProfiloResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelFotoProfilo;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Segnalazioni.SegnalazioniActivity;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecensioniAdapter extends RecyclerView.Adapter<RecensioniAdapter.DataViewHolder>{

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private String User_Proprietario;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private boolean dislike, like;
    private int Nlike, Ndislike;

    public RecensioniAdapter(Activity activity, List<DBModelRecensioni> recensioniList, String user_Segnalatore) {
        this.activity = activity;
        this.recensioniList = recensioniList;
        User_Proprietario = user_Segnalatore;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_recensioni_scritte, parent, false);
        return new RecensioniAdapter.DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(dbModelRecensioni.getFoto() == null){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }else{
            Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getUser_Recensore());
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film());
        verificaCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null){
                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                    if(!(verificaResults.isEmpty())){
                        if(verificaResults.get(0).getCodVerifica() == 0){
                            Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiNumeroDiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), User_Proprietario);
                            EmojCall.enqueue(new Callback<DBModelEmojResponde>() {
                                @Override public void onResponse(@NotNull Call<DBModelEmojResponde> call, @NotNull Response<DBModelEmojResponde> response) {
                                    DBModelEmojResponde dbModelEmojResponde = response.body();
                                    if(dbModelEmojResponde != null){
                                        List<DBModelEmoj> emojList = dbModelEmojResponde.getResults();
                                        if (!(emojList.isEmpty())){
                                            holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                            holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                            holder.Like.setImageResource(R.drawable.ic_like_disable);
                                            holder.Dislike.setImageResource(R.drawable.ic_dislike_diable);
                                            Nlike = Integer.parseInt(emojList.get(0).getNumeroLike());
                                            Ndislike = Integer.parseInt(emojList.get(0).getNumeroDislike());
                                            like = false;
                                            dislike = false;
                                        }else {
                                            Toast.makeText(activity,"Prelievo delle informazioni sulle emoj fallito.",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(activity,"Impossibile prelevare informazioni sulle emoj.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NotNull Call<DBModelEmojResponde> call, @NotNull Throwable t) {
                                    Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), User_Proprietario);
                            EmojCall.enqueue(new Callback<DBModelEmojResponde>() {
                                @Override public void onResponse(@NotNull Call<DBModelEmojResponde> call, @NotNull Response<DBModelEmojResponde> response) {
                                    DBModelEmojResponde dbModelEmojResponde = response.body();
                                    if(dbModelEmojResponde != null){
                                        List<DBModelEmoj> emojList = dbModelEmojResponde.getResults();
                                        if (!(emojList.isEmpty())){
                                            holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                            holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                            if(emojList.get(0).getTipo_Emoj() != null && emojList.get(0).getUser_che_ha_inserito_emoj() != null){
                                                if(emojList.get(0).getTipo_Emoj().equals("Like") ){
                                                    if(emojList.get(0).getUser_che_ha_inserito_emoj().equals(User_Proprietario)) {
                                                        holder.Like.setImageResource(R.drawable.ic_like_active);
                                                        like = true;
                                                        dislike = false;
                                                        holder.Dislike.setEnabled(false);
                                                    }else{
                                                        holder.Like.setImageResource(R.drawable.ic_like_disable);
                                                        like = false;
                                                        dislike = false;
                                                    }
                                                }else if(emojList.get(0).getTipo_Emoj().equals("Dislike")){
                                                    if(emojList.get(0).getUser_che_ha_inserito_emoj().equals(User_Proprietario)) {
                                                        holder.Dislike.setImageResource(R.drawable.ic_dislike_active);
                                                        dislike = true;
                                                        like = false;
                                                        holder.Like.setEnabled(false);
                                                    }else{
                                                        holder.Dislike.setImageResource(R.drawable.ic_dislike_diable);
                                                        dislike = false;
                                                        like = false;
                                                    }
                                                }
                                                Nlike = Integer.parseInt(emojList.get(0).getNumeroLike());
                                                Ndislike = Integer.parseInt(emojList.get(0).getNumeroDislike());
                                            }else{
                                                holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                                holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                                holder.Like.setImageResource(R.drawable.ic_like_disable);
                                                holder.Dislike.setImageResource(R.drawable.ic_dislike_diable);
                                                Nlike = Integer.parseInt(emojList.get(0).getNumeroLike());
                                                Ndislike = Integer.parseInt(emojList.get(0).getNumeroDislike());
                                                like = false;
                                                dislike = false;
                                            }
                                        }else {
                                            Toast.makeText(activity,"Prelievo delle informazioni sulle emoj fallito.",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(activity,"Impossibile prelevare informazioni sulle emoj.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NotNull Call<DBModelEmojResponde> call, @NotNull Throwable t) {
                                    Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }else{
                        Toast.makeText(activity,"Verifica Fallita.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"Impossibile verificare inserimento emoj.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        holder.Segnala.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelVerifica> verificaSegnalazioniCall = retrofitServiceDBInterno.VerificaPresenzaSegnalazione(User_Proprietario, dbModelRecensioni.getUser_Recensore(), String.valueOf(dbModelRecensioni.getId_Recensione()));
                verificaSegnalazioniCall.enqueue(new Callback<DBModelVerifica>() {
                    @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if(dbModelVerifica != null) {
                            List<DBModelVerificaResults> modelVerificaResults = dbModelVerifica.getResults();
                            if (modelVerificaResults.get(0).getCodVerifica() == 0) {
                                Call<DBModelFotoProfiloResponce> fotoProfiloResponceCall = retrofitServiceDBInterno.PrendiFotoProfilo(User_Proprietario);
                                fotoProfiloResponceCall.enqueue(new Callback<DBModelFotoProfiloResponce>() {
                                    @Override public void onResponse(@NonNull Call<DBModelFotoProfiloResponce> call,@NonNull Response<DBModelFotoProfiloResponce> response) {
                                        DBModelFotoProfiloResponce dbModelFotoProfiloResponce = response.body();
                                        if(dbModelFotoProfiloResponce != null){
                                            List<DBModelFotoProfilo> fotoProfilos = dbModelFotoProfiloResponce.getResults();
                                            if (fotoProfilos.get(0).getFotoProfilo() != null){
                                                Intent intent2 = new Intent(activity, SegnalazioniActivity.class);
                                                intent2.putExtra("Nome_Utente_Segnalatore", User_Proprietario);
                                                intent2.putExtra("Nome_Utente_Segnalato", dbModelRecensioni.getUser_Recensore());
                                                intent2.putExtra("Foto_Profilo", fotoProfilos.get(0).getFotoProfilo());
                                                intent2.putExtra("Id_Recensione", String.valueOf(dbModelRecensioni.getId_Recensione()));
                                                activity.startActivity(intent2);
                                            }else{
                                                Intent intent2 = new Intent(activity, SegnalazioniActivity.class);
                                                intent2.putExtra("Nome_Utente_Segnalatore", User_Proprietario);
                                                intent2.putExtra("Nome_Utente_Segnalato", dbModelRecensioni.getUser_Recensore());
                                                intent2.putExtra("Id_Recensione", String.valueOf(dbModelRecensioni.getId_Recensione()));
                                                activity.startActivity(intent2);
                                            }
                                        }
                                    }
                                    @Override public void onFailure(@NonNull Call<DBModelFotoProfiloResponce> call,@NonNull Throwable t) {

                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Hai già segnalato questa recensione", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Impossibile verificare se hai già segnalato questa recensione", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                        Toast.makeText(activity, "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(like){
                    Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), User_Proprietario);
                    deleteCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    like = false;
                                    Nlike--;
                                    holder.NumeroLike.setText(String.valueOf(Nlike));
                                    holder.Dislike.setEnabled(true);
                                    holder.Like.setImageResource(R.drawable.ic_like_disable);
                                }
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), User_Proprietario, "Like");
                    insertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    like = true;
                                    Nlike++;
                                    holder.NumeroLike.setText(String.valueOf(Nlike));
                                    holder.Dislike.setEnabled(false);
                                    holder.Like.setImageResource(R.drawable.ic_like_active);
                                    Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni",  recensioniList.get(position).getTitolo_Film(), "Like", User_Proprietario);
                                    notificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                            if(dbModelResponseToInsert != null) {
                                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {

                                                }
                                            }
                                        }
                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        holder.Dislike.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(dislike){
                    Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), User_Proprietario);
                    deleteCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call, @NotNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    dislike = false;
                                    Ndislike--;
                                    holder.NuemroDislike.setText(String.valueOf(Ndislike));
                                    holder.Like.setEnabled(true);
                                    holder.Dislike.setImageResource(R.drawable.ic_dislike_diable);
                                }
                            }
                        }
                        @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call, @NotNull Throwable t) {
                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), User_Proprietario, "Dislike");
                    insertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call, @NotNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    dislike = true;
                                    Ndislike++;
                                    holder.NuemroDislike.setText(String.valueOf(Ndislike));
                                    holder.Like.setEnabled(false);
                                    holder.Dislike.setImageResource(R.drawable.ic_dislike_active);
                                    Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni", recensioniList.get(position).getTitolo_Film(), "Dislike", User_Proprietario);
                                    notificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                            if(dbModelResponseToInsert != null) {
                                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {

                                                }
                                            }
                                        }
                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                        @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call, @NotNull Throwable t) {
                            Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        holder.Commenta.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, CommentiActivity.class);
                intent.putExtra("UsernameProprietario", User_Proprietario);
                intent.putExtra("UsernameAltroUtente", recensioniList.get(position).getUser_Recensore());
                intent.putExtra("TitoloFilm", recensioniList.get(position).getTitolo_Film());
                intent.putExtra("TipoCorrente", "Recensioni");
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione, NumeroLike, NuemroDislike;
        public AppCompatButton Segnala;
        public RatingBar Voto;
        private MaterialFavoriteButton Like, Dislike;
        private AppCompatButton Commenta;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.username_ricerca);
            DataRecensione = itemView.findViewById(R.id.data_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_recensione);
            Segnala = itemView.findViewById(R.id.segnalazioni_button);
            Voto = itemView.findViewById(R.id.valutazione_db);
            NumeroLike = itemView.findViewById(R.id.number_like);
            NuemroDislike = itemView.findViewById(R.id.number_dislike);
            Like = itemView.findViewById(R.id.like_button);
            Dislike = itemView.findViewById(R.id.dislike_button);
            Commenta = itemView.findViewById(R.id.commenti);
        }
    }
}
