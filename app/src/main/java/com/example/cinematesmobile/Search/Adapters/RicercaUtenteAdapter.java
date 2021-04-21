package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.ActivityProfiloAltroUtente;
import com.example.cinematesmobile.Search.Model.DBModelDataUser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RicercaUtenteAdapter extends RecyclerView.Adapter<RicercaUtenteAdapter.DataViewHolder> {

    private Activity activity;
    private List<DBModelDataUser> dataList;
    public static final String JSON_ARRAY = "dbdata";
    private static final String INSURL = "http://192.168.1.9/cinematesdb/InviaRichiestaDiAmicizia.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSeRichiestaPresente.php";

    public RicercaUtenteAdapter(Activity activity, List<DBModelDataUser> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_utenti_search, parent, false);
        return new DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelDataUser data = dataList.get(position);
        if(data.getImmagineProfilo().equals("null")){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }else{
            Glide.with(activity).load(data.getImmagineProfilo()).into(holder.FotoProfilo);
        }
        holder.Username.setText(data.getUsername_Cercato());
        if(data.getEsisteAmicizia() == 1){
            holder.DettagliAmicizia.setVisibility(View.GONE);
            holder.InviaAmicizia.setEnabled(false);
            holder.InviaAmicizia.setText("Amici");
        }else{
            if(data.getAmiciInComune() == 0) {
                holder.DettagliAmicizia.setText(new StringBuilder().append(data.getAmiciInComune()).append(" Amici In Comune").toString());
            }else if(data.getAmiciInComune() == 1){
                holder.DettagliAmicizia.setText(new StringBuilder().append(data.getAmiciInComune()).append(" Amico In Comune").toString());
            }else{
                holder.DettagliAmicizia.setText(new StringBuilder().append(data.getAmiciInComune()).append(" Amici In Comune").toString());
            }
            VerificaSeRichiestaGiaInviata(data.getUserCheCerca(), data.getUsername_Cercato(), holder);
        }
        holder.InviaAmicizia.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                InviaRichiestaDiAmicizia(data.getUserCheCerca(), data.getUsername_Cercato(), holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityProfiloAltroUtente.class);
                intent.putExtra("UsernameAltroUtente", data.getUsername_Cercato());
                intent.putExtra("UsernameProprietario", data.getUserCheCerca());
                activity.startActivity(intent);
            }
        });
    }

    private void VerificaSeRichiestaGiaInviata(String userCheCerca, String username_cercato, DataViewHolder holder) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Amicizia");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 0) {
                        holder.InviaAmicizia.setVisibility(View.VISIBLE);
                        holder.InviaAmicizia.setText("Invia richiesta");
                    }else{
                        holder.InviaAmicizia.setEnabled(false);
                        holder.InviaAmicizia.setText("Richiesta già inviata");
                    }
                }catch (Exception e){
                    Toast.makeText(activity, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Utente", userCheCerca);
                params.put("E_Amico_Di",username_cercato);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void InviaRichiestaDiAmicizia(String userCheCerca, String username_cercato, DataViewHolder holder) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                        Toast.makeText(activity, "Rchiesta di amicizia inviata a " + username_cercato, Toast.LENGTH_LONG).show();
                        holder.InviaAmicizia.setEnabled(false);
                         holder.InviaAmicizia.setText("Richiesta inviata");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Utente", userCheCerca);
                params.put("E_Amico_Di",username_cercato);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DettagliAmicizia;
        public AppCompatButton InviaAmicizia;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user);
            Username = itemView.findViewById(R.id.username_ricerca);
            InviaAmicizia = itemView.findViewById(R.id.Invia_amicizia);
            DettagliAmicizia = itemView.findViewById(R.id.chi_lo_conosce);
        }
    }
}
