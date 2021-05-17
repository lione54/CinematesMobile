package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelVoti;
import com.example.cinematesmobile.ModelDBInterno.DBModelVotiResponse;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientFilm;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceFilm;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieDetail;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListInComAdapter extends RecyclerView.Adapter<MovieListInComAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelDataFilms> results;
    private String UserProprietario;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private RetrofitServiceFilm retrofitServiceFilm;
    private Double Valutazione_Media;

    public MovieListInComAdapter(Activity activity, List<DBModelDataFilms> results, String userProprietario) {
        this.activity = activity;
        this.results = results;
        UserProprietario = userProprietario;
    }

    @NotNull @Override public DataHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_film_in_comune, parent, false);
        return new DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        DBModelDataFilms data = results.get(position);
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        String lingua = "it-IT";
        Glide.with(activity).load(data.getImage()).into(holder.posterImageView);
        holder.posterTitle.setText(data.getTitolofilm());
        int id = data.getId_film();
        Call<MovieDetail> movieDetailCall = retrofitServiceFilm.PredndiDettagliFilmTMDB(id, BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                MovieDetail movieDetailResponse = response.body();
                if(movieDetailResponse != null){
                    prepareMovieDetails(movieDetailResponse, holder);
                }else{
                    Toast.makeText(activity,"Nessun dettaglio trovato",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(activity,"Nessuna media voto trovata",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"Impossibile trovare valutazione media",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVotiResponse> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("UsernameProprietario", UserProprietario);
                activity.startActivity(intent);
            }
        });
    }

    private void prepareMovieDetails(MovieDetail movieDetailResponse, MovieListInComAdapter.DataHolder holder) {
        String Disponibilita = "Non Disponibile";
        String SV = "SV";
        if(movieDetailResponse.getGenres() != null){
            if (movieDetailResponse.getGenres().size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(movieDetailResponse.getGenres().stream().map(generi -> generi.getName()).collect(Collectors.toList()));
                String geners = stringBuilder.toString().replaceAll("\\[", "");
                String gen = geners.replaceAll("\\]","");
                holder.Generi.setText(gen);
            } else {
                holder.Generi.setText(Disponibilita);
            }
        }else {
            holder.Generi.setText(Disponibilita);
        }
        if(movieDetailResponse.getOverview() != null){
            if(movieDetailResponse.getOverview().length() > 0) {
                String Trama = movieDetailResponse.getOverview().substring(0, 70);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Trama).append("...\nContinuare a leggere.");
                holder.Trama.setText(stringBuilder);
            }else{
                holder.Trama.setText(Disponibilita);
            }
        }else{
            holder.Trama.setText(Disponibilita);
        }
        if(movieDetailResponse.getVote_average() > 0){
            String Punteggio = Float.toString(movieDetailResponse.getVote_average());
            holder.VotoTMDB.setText(Punteggio);
        }else{
            holder.VotoTMDB.setText(SV);
        }
    }

    @Override public int getItemCount() {
        return results.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private final KenBurnsView posterImageView;
        public AppCompatTextView posterTitle, Generi, Trama, VotoTMDB, VotoCinemates;


        public DataHolder(@NotNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster_image_view_in_comune);
            posterTitle = itemView.findViewById(R.id.titolo_poster_in_comune);
            Generi = itemView.findViewById(R.id.film_detail_genere_in_comune);
            Trama = itemView.findViewById(R.id.film_pref_trama_in_comune);
            VotoTMDB = itemView.findViewById(R.id.film_votazione_generale_in_comune);
            VotoCinemates = itemView.findViewById(R.id.film_votazione_cinemates_in_comune);
            RandomTransitionGenerator generator = new RandomTransitionGenerator(1000, new DecelerateInterpolator());
            posterImageView.setTransitionGenerator(generator);
        }
    }
}