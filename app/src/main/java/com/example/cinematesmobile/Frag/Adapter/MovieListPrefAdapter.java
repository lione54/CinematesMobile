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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.MovieDetail;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MovieListPrefAdapter extends RecyclerView.Adapter<MovieListPrefAdapter.DataViewHolder> {

    private Context mCtx;
    private List<DBModelDataFilms> dataList;
    private String User;
    private String Tipo_lista;
    private static final String RIMURL = "http://192.168.1.9/cinematesdb/RimuoviDaListeFilm.php";
    private RetrofitService retrofitService;
    public static final String JSON_ARRAY = "dbdata";
    private static final String RECURL = "http://192.168.1.9/cinematesdb/PrendiMediaVoti.php";
    private Double Valutazione_Media;

    public MovieListPrefAdapter(Context mCtx, List<DBModelDataFilms> dataList, String user, String tipo_lista) {
        this.mCtx = mCtx;
        this.dataList = dataList;
        User = user;
        Tipo_lista = tipo_lista;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_preferiti, null);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        DBModelDataFilms data = dataList.get(position);
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        String lingua = "it-IT";
        Glide.with(mCtx).load(data.getImage()).into(holder.posterImageView);
        holder.posterTitle.setText(data.getTitolofilm());
        int id = data.getId_film();
        Call<MovieDetail> movieDetailCall = retrofitService.getMovieDetail(id, BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
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
        PrendiMedia(data.getTitolofilm(), holder);
        holder.RimuoviDaLista.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                RimuoviFilmDaLista(User, Tipo_lista, data.getId_film());
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, dataList.size());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mCtx, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                mCtx.startActivity(intent);
            }
        });
    }

    private void PrendiMedia(String titolofilm, DataViewHolder holder) {
        String titoloMod = titolofilm.replaceAll("'", "/");
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
                    holder.VotoCinemates.setText(String.valueOf(Valutazione_Media));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Titolo_Film_Recensito", titoloMod);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
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

    private void RimuoviFilmDaLista(String user, String tipo_lista, Integer id_film) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(mCtx, "Film rimosso dalla lista " + tipo_lista, Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id_film));
                params.put("User_Proprietario", user);
                params.put("Tipo_Lista",tipo_lista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView posterImageView;
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