package com.example.cinematesmobile.Recensioni.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;
import com.example.cinematesmobile.Recensioni.ScriviRecensioneActivity;
import com.example.cinematesmobile.Segnalazioni.SegnalazioniActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecensioniAdapter extends RecyclerView.Adapter<RecensioniAdapter.DataViewHolder>{

    private Context mCtx;
    private List<DBModelRecensioni> recensioniList;
    private String User_Segnalatore;
    public static final String JSON_ARRAY = "dbdata";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSeSegnalazionePresente.php";

    public RecensioniAdapter(Context mCtx, List<DBModelRecensioni> recensioniList, String user_Segnalatore) {
        this.mCtx = mCtx;
        this.recensioniList = recensioniList;
        User_Segnalatore = user_Segnalatore;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_recensioni_scritte, null);
        return new RecensioniAdapter.DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        if(dbModelRecensioni.getFoto().equals("null")){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(mCtx).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getUser_Recensore());
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
        holder.Segnala.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                VerificaSeSegnalazioneEsistente(User_Segnalatore, dbModelRecensioni.getUser_Recensore(), dbModelRecensioni.getFoto(), String.valueOf(dbModelRecensioni.getId_Recensione()));
            }
        });
    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione;
        public AppCompatButton Segnala;
        public RatingBar Voto;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.username_ricerca);
            DataRecensione = itemView.findViewById(R.id.data_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_recensione);
            Segnala = itemView.findViewById(R.id.segnalazioni_button);
            Voto = itemView.findViewById(R.id.valutazione_db);
        }
    }

    private void VerificaSeSegnalazioneEsistente(String nomeUtenteSegnalatore, String nomeUtenteSegnalato, String Foto,String idRecensione) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Id_Film_Recensito");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 0) {
                        Intent intent2 = new Intent(mCtx, SegnalazioniActivity.class);
                        intent2.putExtra("Nome_Utente_Segnalatore", nomeUtenteSegnalatore);
                        intent2.putExtra("Nome_Utente_Segnalato", nomeUtenteSegnalato);
                        intent2.putExtra("Foto_Profilo", Foto);
                        intent2.putExtra("Id_Recensione", idRecensione);
                        mCtx.startActivity(intent2);
                    }else{
                        Toast.makeText(mCtx, "Hai già segnalato la recensione di questo user", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(mCtx, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Segnalatore", nomeUtenteSegnalatore);
                params.put("User_Segnalato",nomeUtenteSegnalato);
                params.put("Id_Recensione", String.valueOf(idRecensione));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
        requestQueue.add(stringRequest);
    }
}
