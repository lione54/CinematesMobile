package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.Model.DBModelVoti;
import com.example.cinematesmobile.ModelDBInterno.DBModelVotiResponse;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientFilm;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceFilm;
import com.example.cinematesmobile.Search.ModelMovieActor.GeneriResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.GeneriResponseResult;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieResponseResults;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolder;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Activity activity;
    private List<MovieResponseResults> results;
    private RetrofitServiceFilm retrofitServiceFilm;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private Double Valutazione_Media;

    public MovieSearchAdapter(Activity activity, List<MovieResponseResults> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull @Override public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_ricerca, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        MovieResponseResults responseResults = results.get(i);
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        String lingua = "it-IT";
        searchViewHolder.setPosterImageView(activity, responseResults.getPoster_path());
        String title = responseResults.getTitle();
        Float ratings = responseResults.getVote_average();
        List<Integer> id_generi = responseResults.getGenre_id();
        int id = responseResults.getId();
        String titoloMod = title.replaceAll("'", "/");
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
                        searchViewHolder.VotoCinemates.setText(String.valueOf(Valutazione_Media));
                    }else {
                        Toast.makeText(activity,"Nessuna media voto trovata.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(activity,"Impossibile trovare valutazione media.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVotiResponse> call, Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        Call<GeneriResponse> generiResponseCall = retrofitServiceFilm.getGeneri(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
        generiResponseCall.enqueue(new Callback<GeneriResponse>() {
            @Override public void onResponse(@NonNull Call<GeneriResponse> call,@NonNull Response<GeneriResponse> response) {
                GeneriResponse generiResponse = response.body();
                List<GeneriResponseResult> generiResponseResultList = generiResponse.getGenres();
                if(generiResponseResultList != null){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < generiResponseResultList.size(); i++) {
                        for (int j = 0; j < id_generi.size(); j++) {
                            if (generiResponseResultList.get(i).getId() == id_generi.get(j)) {
                                stringBuilder.append(generiResponseResultList.get(i).getName()).append(" ");
                            }
                        }
                    }
                    if(stringBuilder != null){
                        searchViewHolder.Genere.setText(stringBuilder);
                        searchViewHolder.Genere.setVisibility(View.VISIBLE);
                    } else {
                        searchViewHolder.Genere.setText("Genere Non Disponibile.");
                        searchViewHolder.Genere.setVisibility(View.VISIBLE);
                    }
                } else {
                    searchViewHolder.Genere.setText("Genere Non Disponibile.");
                    searchViewHolder.Genere.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onFailure(@NonNull Call<GeneriResponse> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        if(responseResults.getOverview() != null){
            Integer Maxlen = responseResults.getOverview().length();
            if(Maxlen > 70) {
                String Trama = responseResults.getOverview().substring(0, 70);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Trama).append("...\nContinuare a leggere.");
                searchViewHolder.Trama.setText(stringBuilder);
            }else if (Maxlen == 0){
                searchViewHolder.Trama.setText("Non disponibile.");
            }else{
                String Trama = responseResults.getOverview().substring(0, 50);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Trama).append("...\nContinuare a leggere.");
                searchViewHolder.Trama.setText(stringBuilder);
            }
        }else{
            searchViewHolder.Trama.setText("Non disponibile.");
        }
        if(title != null){
            searchViewHolder.posterTitle.setVisibility(View.VISIBLE);
            searchViewHolder.posterTitle.setText(title);
        }else{
            searchViewHolder.posterTitle.setVisibility(View.GONE);
        }
        if(ratings != null){
            if(ratings > 0){
                String Punteggio = Float.toString(ratings);
                searchViewHolder.Voto.setText(Punteggio);
                searchViewHolder.Voto.setVisibility(View.VISIBLE);
            }else{
                searchViewHolder.Voto.setText("SV");
                searchViewHolder.Voto.setVisibility(View.VISIBLE);

            }
        }else{
            searchViewHolder.Voto.setVisibility(View.GONE);
        }
        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return results.size();
    }
}
