package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.Frag.FragmentPreferiti;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AltroUtenteAmicoAdapter extends RecyclerView.Adapter<AltroUtenteAmicoAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelDataListeFilm> dataListeFilms;
    private String UsernameAltroUtente;
    private static final String URL = "http://192.168.178.48/cinematesdb/PrendiDaLista.php";
    public static final String JSON_ARRAY = "dbdata";
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
        PrendiDaListeAltroUtente(UsernameAltroUtente, dbModelDataListeFilm.getTitoloLista(), holder);
    }

    private void PrendiDaListeAltroUtente(String usernameAltroUtente, String titoloLista, DataHolder holder) {
        List<DBModelDataFilms> preferiti = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id_film = object.getString("id_film_inserito");
                            String str_titolo = object.getString("Titolo_Film");
                            String str_url = object.getString("Url_Immagine");
                            Integer id_film = Integer.parseInt(str_id_film);
                            String strt_titoloMod = str_titolo.replaceAll("/", "'");
                            DBModelDataFilms dbModelDataFilms = new DBModelDataFilms(id_film, strt_titoloMod, str_url);
                            preferiti.add(dbModelDataFilms);
                        }
                    if(!(preferiti.isEmpty())){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.film.getContext(), RecyclerView.HORIZONTAL, false);
                        holder.film.setLayoutManager(linearLayoutManager);
                        holder.film.setHasFixedSize(false);
                        movieListAltroUtenteAdapter = new MovieListAltroUtenteAdapter(activity, preferiti);
                        holder.film.setAdapter(movieListAltroUtenteAdapter);
                        movieListAltroUtenteAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Toast.makeText(activity, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", usernameAltroUtente);
                params.put("Tipo_Lista",titoloLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
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
