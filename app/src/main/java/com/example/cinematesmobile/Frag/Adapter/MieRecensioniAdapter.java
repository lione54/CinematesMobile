package com.example.cinematesmobile.Frag.Adapter;

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
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MieRecensioniAdapter extends RecyclerView.Adapter<MieRecensioniAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private String AltroUser;
    private String UserProprietario;
    private boolean dislike, like;
    private int Nlike, Ndislike;

    public MieRecensioniAdapter(Activity activity, List<DBModelRecensioni> recensioniList, String user_Segnalatore, String userProprietario) {
        this.activity = activity;
        this.recensioniList = recensioniList;
        AltroUser = user_Segnalatore;
        UserProprietario = userProprietario;
    }

    @NonNull @Override public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_mie_recensioni_scritte, parent, false);
        return new MieRecensioniAdapter.DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(AltroUser.equals(UserProprietario)){
            if(dbModelRecensioni.getFoto() == null){
                holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }else{
                Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
            }
            holder.Username.setText(dbModelRecensioni.getTitolo_Film().replaceAll("/", "'"));
            holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
            holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
            holder.Voto.setRating(dbModelRecensioni.getValutazione());
            holder.Rimuovi.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuoviCall = retrofitServiceDBInterno.RimuoviRecensione(String.valueOf(dbModelRecensioni.getId_Recensione()), dbModelRecensioni.getUser_Recensore());
                    rimuoviCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if (dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Recensione rimossa con successo", Toast.LENGTH_SHORT).show();
                                    recensioniList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, recensioniList.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione recensione fallita", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(activity, "Impossibile eliminare recensione", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film());
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                        if(!(verificaResults.isEmpty())){
                            if(verificaResults.get(0).getCodVerifica() == 0){
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), AltroUser);
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
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), AltroUser);
                                EmojCall.enqueue(new Callback<DBModelEmojResponde>() {
                                    @Override public void onResponse(@NotNull Call<DBModelEmojResponde> call, @NotNull Response<DBModelEmojResponde> response) {
                                        DBModelEmojResponde dbModelEmojResponde = response.body();
                                        if(dbModelEmojResponde != null){
                                            List<DBModelEmoj> emojList = dbModelEmojResponde.getResults();
                                            if (!(emojList.isEmpty())){
                                                holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                                holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                                if(emojList.get(0).getTipo_Emoj().equals("Like")){
                                                    holder.Like.setImageResource(R.drawable.ic_like_active);
                                                    like = true;
                                                    dislike = false;
                                                    holder.Dislike.setEnabled(false);
                                                }else if(emojList.get(0).getTipo_Emoj().equals("Dislike")){
                                                    holder.Dislike.setImageResource(R.drawable.ic_dislike_active);
                                                    dislike = true;
                                                    like = false;
                                                    holder.Like.setEnabled(false);
                                                }
                                                Nlike = Integer.parseInt(emojList.get(0).getNumeroLike());
                                                Ndislike = Integer.parseInt(emojList.get(0).getNumeroDislike());
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
            holder.Like.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(like){
                        Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser);
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
                        Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser, "Like");
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
                                        Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni",  recensioniList.get(position).getTitolo_Film(), "Like", AltroUser);
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
                        Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser);
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
                        Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser, "Dislike");
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
                                        Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni", recensioniList.get(position).getTitolo_Film(), "Dislike", AltroUser);
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
                    intent.putExtra("UsernameProprietario", AltroUser);
                    intent.putExtra("UsernameAltroUtente", dbModelRecensioni.getUser_Recensore());
                    intent.putExtra("TitoloFilm", recensioniList.get(position).getTitolo_Film());
                    intent.putExtra("TipoCorrente", "Recensioni");
                    activity.startActivity(intent);
                }
            });
        }else{
            if(dbModelRecensioni.getFoto() == null){
                holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }else{
                Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
            }
            holder.Username.setText(dbModelRecensioni.getTitolo_Film().replaceAll("/", "'"));
            holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
            holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
            holder.Voto.setRating(dbModelRecensioni.getValutazione());
            holder.Rimuovi.setVisibility(View.GONE);
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film());
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                        if(!(verificaResults.isEmpty())){
                            if(verificaResults.get(0).getCodVerifica() == 0){
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), AltroUser);
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
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(dbModelRecensioni.getUser_Recensore(), "Recensioni", dbModelRecensioni.getTitolo_Film(), AltroUser);
                                EmojCall.enqueue(new Callback<DBModelEmojResponde>() {
                                    @Override public void onResponse(@NotNull Call<DBModelEmojResponde> call, @NotNull Response<DBModelEmojResponde> response) {
                                        DBModelEmojResponde dbModelEmojResponde = response.body();
                                        if(dbModelEmojResponde != null){
                                            List<DBModelEmoj> emojList = dbModelEmojResponde.getResults();
                                            if (!(emojList.isEmpty())){
                                                holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                                holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                                if(emojList.get(0).getTipo_Emoj().equals("Like")){
                                                    holder.Like.setImageResource(R.drawable.ic_like_active);
                                                    like = true;
                                                    dislike = false;
                                                    holder.Dislike.setEnabled(false);
                                                }else if(emojList.get(0).getTipo_Emoj().equals("Dislike")){
                                                    holder.Dislike.setImageResource(R.drawable.ic_dislike_active);
                                                    dislike = true;
                                                    like = false;
                                                    holder.Like.setEnabled(false);
                                                }
                                                Nlike = Integer.parseInt(emojList.get(0).getNumeroLike());
                                                Ndislike = Integer.parseInt(emojList.get(0).getNumeroDislike());
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
            holder.Like.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(like){
                        Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser);
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
                        Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser, "Like");
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
                                        Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni",  recensioniList.get(position).getTitolo_Film(), "Like", AltroUser);
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
                        Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser);
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
                        Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(dbModelRecensioni.getUser_Recensore(), "Recensioni", recensioniList.get(position).getTitolo_Film(), AltroUser, "Dislike");
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
                                        Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(dbModelRecensioni.getUser_Recensore(),"Recensioni", recensioniList.get(position).getTitolo_Film(), "Dislike", AltroUser);
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
                    intent.putExtra("UsernameProprietario", UserProprietario);
                    intent.putExtra("UsernameAltroUtente", dbModelRecensioni.getUser_Recensore());
                    intent.putExtra("TitoloFilm", recensioniList.get(position).getTitolo_Film());
                    intent.putExtra("TipoCorrente", "Recensioni");
                    activity.startActivity(intent);
                }
            });
        }

    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione, NumeroLike, NuemroDislike;
        public AppCompatButton Rimuovi;
        public RatingBar Voto;
        private MaterialFavoriteButton Like, Dislike;
        private AppCompatButton Commenta;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.mia_image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.mio_username);
            DataRecensione = itemView.findViewById(R.id.data_mia_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_mie_recensione);
            Rimuovi = itemView.findViewById(R.id.rimozione_button);
            Voto = itemView.findViewById(R.id.valutazione_db_mie_rece);
            NumeroLike = itemView.findViewById(R.id.number_like);
            NuemroDislike = itemView.findViewById(R.id.number_dislike);
            Like = itemView.findViewById(R.id.like_button);
            Dislike = itemView.findViewById(R.id.dislike_button);
            Commenta = itemView.findViewById(R.id.commenti);
        }
    }
}
