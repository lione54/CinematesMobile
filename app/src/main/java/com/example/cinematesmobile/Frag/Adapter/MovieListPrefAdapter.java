package com.example.cinematesmobile.Frag.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.Model.DBModelVoti;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVotiResponse;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientFilm;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceFilm;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieDetail;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListPrefAdapter extends RecyclerView.Adapter<MovieListPrefAdapter.DataViewHolder> {

    private Context mCtx;
    private List<DBModelDataFilms> dataList;
    private String User;
    private String Tipo_lista;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private RetrofitServiceFilm retrofitServiceFilm;
    private Double Valutazione_Media;

    public MovieListPrefAdapter(Context mCtx, List<DBModelDataFilms> dataList, String user, String tipo_lista) {
        this.mCtx = mCtx;
        this.dataList = dataList;
        User = user;
        Tipo_lista = tipo_lista;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_preferiti, null);
        return new DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelDataFilms data = dataList.get(position);
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        String lingua = "it-IT";
        Glide.with(mCtx).load(data.getImage()).into(holder.posterImageView);
        holder.posterTitle.setText(data.getTitolofilm());
        int id = data.getId_film();
        Call<MovieDetail> movieDetailCall = retrofitServiceFilm.PredndiDettagliFilmTMDB(id, BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                MovieDetail movieDetailResponse = response.body();
                if(movieDetailResponse != null){
                    prepareMovieDetails(movieDetailResponse, holder);
                }else{
                    Toast.makeText(mCtx,"Nessun dettaglio trovato",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                Toast.makeText(mCtx,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        String titoloMod = data.getTitolofilm().replaceAll("'", "/");
        Call<DBModelVotiResponse> votiResponseCall = retrofitServiceDBInterno.PrendiMediaVoti(titoloMod);
        votiResponseCall.enqueue(new Callback<DBModelVotiResponse>() {
            @Override public void onResponse(@NonNull Call<DBModelVotiResponse> call,@NonNull Response<DBModelVotiResponse> response) {
                DBModelVotiResponse dbModelVotiResponse = response.body();
                if(dbModelVotiResponse != null){
                    List<DBModelVoti> votiList = dbModelVotiResponse.getResults();
                    if(!(votiList.isEmpty())){
                        for(int i = 0; i < votiList.size(); i++){
                            if(votiList.get(i).getValutazione_Media() == null){
                                Valutazione_Media = 0.0;
                            }else{
                                Valutazione_Media = votiList.get(i).getValutazione_Media();
                            }
                        }
                        holder.VotoCinemates.setText(String.valueOf(Valutazione_Media));
                    }else {
                        Toast.makeText(mCtx,"Nessuna media voto trovata",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mCtx,"Impossibile trovare valutazione media",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVotiResponse> call,@NonNull Throwable t) {
                Toast.makeText(mCtx,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        holder.RimuoviDaLista.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelResponseToInsert> rimuoviCall = retrofitServiceDBInterno.RimuoviFilm(String.valueOf(data.getId_film()), User);
                rimuoviCall.enqueue(new Callback<DBModelResponseToInsert>() {
                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                        if (dbModelResponseToInsert != null) {
                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                Toast.makeText(mCtx, "Film rimosso dalla lista " + Tipo_lista + ".", Toast.LENGTH_SHORT).show();
                                dataList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, dataList.size());
                            }else{
                                Toast.makeText(mCtx, "Rimozione dalla lista " + Tipo_lista + " fallito.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mCtx, "Impossibile rimuovere da " + Tipo_lista + ".", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                        Toast.makeText(mCtx,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mCtx, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("UsernameProprietario", User);
                mCtx.startActivity(intent);
            }
        });
    }

    private void prepareMovieDetails(MovieDetail movieDetailResponse, DataViewHolder holder) {
        if(movieDetailResponse.getGenres() != null){
            if (movieDetailResponse.getGenres().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(movieDetailResponse.getGenres().stream().map(generi -> generi.getName()).collect(Collectors.toList()));
                String geners = stringBuilder.toString().replaceAll("\\[", "");
                String gen = geners.replaceAll("\\]","");
                holder.Generi.setText(gen);
            } else {
                holder.Generi.setText("Non Disponibile");
            }
        }else {
            holder.Generi.setText("Non Disponibile");
        }
        if(movieDetailResponse.getOverview() != null){
            if(movieDetailResponse.getOverview().length() > 0) {
                String Trama = movieDetailResponse.getOverview().substring(0, 70);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Trama).append("...\nContinuare a leggere.");
                holder.Trama.setText(stringBuilder);
            }else{
                holder.Trama.setText("Non disponibile");
            }
        }else{
            holder.Trama.setText("Non disponibile");
        }
        if(movieDetailResponse.getVote_average() > 0){
            String Punteggio = Float.toString(movieDetailResponse.getVote_average());
            holder.VotoTMDB.setText(Punteggio);
        }else{
            holder.VotoTMDB.setText("SV");
        }
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        private final KenBurnsView posterImageView;
        public AppCompatTextView posterTitle, Generi, Trama, VotoTMDB, VotoCinemates;
        public AppCompatButton RimuoviDaLista;

        public DataViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster_image_view_preferiti);
            posterTitle = itemView.findViewById(R.id.titolo_poster_preferiti);
            Generi = itemView.findViewById(R.id.film_detail_genere);
            Trama = itemView.findViewById(R.id.film_pref_trama);
            VotoTMDB = itemView.findViewById(R.id.film_votazione_generale);
            VotoCinemates = itemView.findViewById(R.id.film_votazione_cinemates);
            RimuoviDaLista = itemView.findViewById(R.id.rimuovi_da_lista);
            RandomTransitionGenerator generator = new RandomTransitionGenerator(1000, new DecelerateInterpolator());
            posterImageView.setTransitionGenerator(generator);
        }
    }
}