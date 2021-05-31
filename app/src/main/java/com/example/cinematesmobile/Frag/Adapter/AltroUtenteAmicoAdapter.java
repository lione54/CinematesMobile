package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.Frag.CommentiActivity;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.Frag.Model.DBModelEmoj;
import com.example.cinematesmobile.ModelDBInterno.DBModelEmojResponde;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AltroUtenteAmicoAdapter extends RecyclerView.Adapter<AltroUtenteAmicoAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelDataListeFilm> dataListeFilms;
    private String UsernameAltroUtente;
    private String UsernameProprietario;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private MovieListAltroUtenteAdapter movieListAltroUtenteAdapter;
    private boolean dislike, like;
    private int Nlike, Ndislike;

    public AltroUtenteAmicoAdapter(Activity activity, List<DBModelDataListeFilm> dataListeFilms, String usernameAltroUtente, String usernameProprietario) {
        this.activity = activity;
        this.dataListeFilms = dataListeFilms;
        UsernameAltroUtente = usernameAltroUtente;
        UsernameProprietario = usernameProprietario;
    }

    @NonNull @Override public AltroUtenteAmicoAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.card_liste_film_altro_utente, parent, false);
        return new AltroUtenteAmicoAdapter.DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull AltroUtenteAmicoAdapter.DataHolder holder, int position) {
        DBModelDataListeFilm dbModelDataListeFilm = dataListeFilms.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(dbModelDataListeFilm.getTitoloLista() != null){
            holder.Nomelista.setText(dbModelDataListeFilm.getTitoloLista());
        }
        if(dbModelDataListeFilm.getTitoloLista().equals("Da Vedere") || dbModelDataListeFilm.getTitoloLista().equals("Preferiti")){
            holder.DescrizioneListaAltroUserLayout.setVisibility(View.GONE);
            holder.Emoj.setVisibility(View.GONE);
        }else {
            if (dbModelDataListeFilm.getDescrizioneLista() != null) {
                if (dbModelDataListeFilm.getDescrizioneLista() != null) {
                    holder.Descrizione.setText(dbModelDataListeFilm.getDescrizioneLista());
                } else {
                    holder.Descrizione.setText("Descrizione non inserita dall'utente");
                }
            }
            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaInserimentoEmoj(UsernameProprietario, "Lista", dbModelDataListeFilm.getTitoloLista());
            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if(dbModelVerifica != null){
                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                        if(!(verificaResults.isEmpty())){
                            if(verificaResults.get(0).getCodVerifica() == 0){
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(UsernameAltroUtente, "Lista", dbModelDataListeFilm.getTitoloLista(), UsernameProprietario);
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
                                                Toast.makeText(activity,"Prelievo delle informazioni sulle emoji fallito.",Toast.LENGTH_SHORT).show();
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
                                Call<DBModelEmojResponde> EmojCall = retrofitServiceDBInterno.PrendiEmojDaDB(UsernameAltroUtente, "Lista", dbModelDataListeFilm.getTitoloLista(), UsernameProprietario);
                                EmojCall.enqueue(new Callback<DBModelEmojResponde>() {
                                    @Override public void onResponse(@NotNull Call<DBModelEmojResponde> call, @NotNull Response<DBModelEmojResponde> response) {
                                        DBModelEmojResponde dbModelEmojResponde = response.body();
                                        if(dbModelEmojResponde != null){
                                            List<DBModelEmoj> emojList = dbModelEmojResponde.getResults();
                                            if (!(emojList.isEmpty())){
                                                holder.NumeroLike.setText(emojList.get(0).getNumeroLike());
                                                holder.NuemroDislike.setText(emojList.get(0).getNumeroDislike());
                                                if(emojList.get(0).getTipo_Emoj().equals("Like")) {
                                                    if (emojList.get(0).getUser_che_ha_inserito_emoj().equals(UsernameProprietario)){
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
                                                    if (emojList.get(0).getUser_che_ha_inserito_emoj().equals(UsernameProprietario)) {
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
                                            }else {
                                                Toast.makeText(activity,"Prelievo delle informazioni sulle emoji fallito.",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(activity,"Impossibile prelevare informazioni sulle emoji.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override public void onFailure(@NotNull Call<DBModelEmojResponde> call, @NotNull Throwable t) {
                                        Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            Toast.makeText(activity,"Verifica Fallita",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity,"Impossibile verificare inserimento emoji",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                    Toast.makeText(activity,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                }
            });
        }
        Call<DBModelFilmsResponce> filmsResponceCall = retrofitServiceDBInterno.PrendiFilmDaDB(UsernameAltroUtente, dbModelDataListeFilm.getTitoloLista());
        filmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelFilmsResponce> call,@NonNull retrofit2.Response<DBModelFilmsResponce> response) {
                DBModelFilmsResponce dbModelFilmsResponce  = response.body();
                if(dbModelFilmsResponce != null){
                    List<DBModelDataFilms> preferiti = dbModelFilmsResponce.getResults();
                    if(!(preferiti.isEmpty())){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.film.getContext(), RecyclerView.HORIZONTAL, false);
                        holder.film.setLayoutManager(linearLayoutManager);
                        holder.film.setHasFixedSize(false);
                        movieListAltroUtenteAdapter = new MovieListAltroUtenteAdapter(activity, preferiti, UsernameAltroUtente, UsernameProprietario);
                        holder.film.setAdapter(movieListAltroUtenteAdapter);
                        movieListAltroUtenteAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(activity,"Nessun film da mostrare",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"Impossibile recuperare i film",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        holder.Like.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(like){
                    Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), UsernameProprietario);
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
                    Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), UsernameProprietario, "Like");
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
                                    Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), "Like", UsernameProprietario);
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
                    Call<DBModelResponseToInsert> deleteCall = retrofitServiceDBInterno.EliminaEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), UsernameProprietario);
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
                    Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), UsernameProprietario, "Dislike");
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
                                    Call<DBModelResponseToInsert> notificaCall = retrofitServiceDBInterno.NotificaInserimentoEmoj(UsernameAltroUtente, "Lista", dataListeFilms.get(position).getTitoloLista(), "Dislike", UsernameProprietario);
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
                intent.putExtra("UsernameProprietario", UsernameProprietario);
                intent.putExtra("UsernameAltroUtente", UsernameAltroUtente);
                intent.putExtra("TitoloLista", dataListeFilms.get(position).getTitoloLista());
                if(dataListeFilms.get(position).getDescrizioneLista() == null) {
                    intent.putExtra("Descrizione", "Descrizione non inserita dall' utente");
                }else{
                    intent.putExtra("Descrizione", dataListeFilms.get(position).getTitoloLista());
                }
                intent.putExtra("TipoCorrente", "Lista");
                intent.putExtra("DiChiè", "null");
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return dataListeFilms.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView Nomelista, Descrizione, NumeroLike, NuemroDislike;
        private RecyclerView film;
        private LinearLayout DescrizioneListaAltroUserLayout, Emoj;
        private MaterialFavoriteButton Like, Dislike;
        private AppCompatButton Commenta;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            Nomelista = itemView.findViewById(R.id.nome_lista);
            Descrizione = itemView.findViewById(R.id.descrizione_lista_altro_user);
            film = itemView.findViewById(R.id.film_altro_utente);
            DescrizioneListaAltroUserLayout = itemView.findViewById(R.id.descrizione_lista_altro_user_layout);
            Emoj = itemView.findViewById(R.id.Emoji);
            NumeroLike = itemView.findViewById(R.id.number_like);
            NuemroDislike = itemView.findViewById(R.id.number_dislike);
            Like = itemView.findViewById(R.id.like_button);
            Dislike = itemView.findViewById(R.id.dislike_button);
            Commenta = itemView.findViewById(R.id.commenti);
        }
    }
}
