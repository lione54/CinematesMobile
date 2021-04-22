package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.GeneriResponse;
import com.example.cinematesmobile.Search.Model.GeneriResponseResult;
import com.example.cinematesmobile.Search.Model.MovieDetail;
import com.example.cinematesmobile.Search.Model.MovieResponseResults;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolder;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Activity activity;
    private List<MovieResponseResults> results;
    private RetrofitService retrofitService;
    public static final String JSON_ARRAY = "dbdata";
    private static final String RECURL = "http://192.168.178.48/cinematesdb/PrendiMediaVoti.php";
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
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        String lingua = "it-IT";
        searchViewHolder.setPosterImageView(activity, responseResults.getPoster_path());
        String title = responseResults.getTitle();
        Float ratings = responseResults.getVote_average();
        List<Integer> id_generi = responseResults.getGenre_id();
        int id = responseResults.getId();
        PrendiVotoMedioDaDB(title, searchViewHolder);
        Call<GeneriResponse> generiResponseCall = retrofitService.getGeneri(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
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
                        searchViewHolder.Genere.setText("Genere Non Disponibile");
                        searchViewHolder.Genere.setVisibility(View.VISIBLE);
                    }
                } else {
                    searchViewHolder.Genere.setText("Genere Non Disponibile");
                    searchViewHolder.Genere.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onFailure(@NonNull Call<GeneriResponse> call,@NonNull Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        if(responseResults.getOverview() != null){
            if(responseResults.getOverview().length() > 0) {
                String Trama = responseResults.getOverview().substring(0, 70);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Trama).append("...\nContinuare a leggere.");
                searchViewHolder.Trama.setText(stringBuilder);
            }else{
                searchViewHolder.Trama.setText("Non disponibile");
            }
        }else{
            searchViewHolder.Trama.setText("Non disponibile");
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

    private void PrendiVotoMedioDaDB(String titolo, SearchViewHolder searchViewHolder) {
        String titoloMod = titolo.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String str_valu = object.getString("Valutazione");
                        if(str_valu.equals("null")){
                            Valutazione_Media = 0.0;
                        }else {
                            Valutazione_Media = Double.valueOf(str_valu);
                        }
                    }
                    searchViewHolder.VotoCinemates.setText(String.valueOf(Valutazione_Media));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Titolo_Film_Recensito", titoloMod);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return results.size();
    }
}
