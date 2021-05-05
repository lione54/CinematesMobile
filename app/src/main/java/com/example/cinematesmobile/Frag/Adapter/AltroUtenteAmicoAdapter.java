package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

public class AltroUtenteAmicoAdapter extends RecyclerView.Adapter<AltroUtenteAmicoAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelDataListeFilm> dataListeFilms;
    private String UsernameAltroUtente;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private MovieListAltroUtenteAdapter movieListAltroUtenteAdapter;

    public AltroUtenteAmicoAdapter(Activity activity, List<DBModelDataListeFilm> dataListeFilms, String usernameAltroUtente) {
        this.activity = activity;
        this.dataListeFilms = dataListeFilms;
        UsernameAltroUtente = usernameAltroUtente;
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
        }else {
            if (dbModelDataListeFilm.getDescrizioneLista() != null) {
                if (!(dbModelDataListeFilm.getDescrizioneLista().equals("null"))) {
                    holder.Descrizione.setText(dbModelDataListeFilm.getDescrizioneLista());
                } else {
                    holder.Descrizione.setText("Descrizione non inserita dall'utente");
                }
            }
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
                        movieListAltroUtenteAdapter = new MovieListAltroUtenteAdapter(activity, preferiti);
                        holder.film.setAdapter(movieListAltroUtenteAdapter);
                        movieListAltroUtenteAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(activity,"Nessun film da mostrare.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"Impossibile recuperare i film.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public int getItemCount() {
        return dataListeFilms.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView Nomelista, Descrizione;
        private RecyclerView film;
        private LinearLayout DescrizioneListaAltroUserLayout;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            Nomelista = itemView.findViewById(R.id.nome_lista);
            Descrizione = itemView.findViewById(R.id.descrizione_lista_altro_user);
            film = itemView.findViewById(R.id.film_altro_utente);
            DescrizioneListaAltroUserLayout = itemView.findViewById(R.id.descrizione_lista_altro_user_layout);
        }
    }
}
