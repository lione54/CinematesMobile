package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MieRecensioniAdapter extends RecyclerView.Adapter<MieRecensioniAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private static final String RIMURL = "http://192.168.1.9/cinematesdb/RimuoviRecensione.php";

    public MieRecensioniAdapter(Activity activity, List<DBModelRecensioni> recensioniList) {
        this.activity = activity;
        this.recensioniList = recensioniList;
    }

    @NonNull @Override public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_mie_recensioni_scritte, parent, false);
        return new MieRecensioniAdapter.DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        if(dbModelRecensioni.getFoto().equals("null")){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getTitolo_Film());
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
        holder.Rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                RimuoviRecensione(dbModelRecensioni.getUser_Recensore(), dbModelRecensioni.getId_Recensione());
                recensioniList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, recensioniList.size());
            }
        });
    }

    private void RimuoviRecensione(String user_recensore, Integer id_recensione) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(activity, "Recensione rimossa con successo", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Recensione", String.valueOf(id_recensione));
                params.put("User_Proprietario", user_recensore);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione;
        public AppCompatButton Rimuovi;
        public RatingBar Voto;
        
        public DataHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.mia_image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.mio_username);
            DataRecensione = itemView.findViewById(R.id.data_mia_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_mie_recensione);
            Rimuovi = itemView.findViewById(R.id.rimozione_button);
            Voto = itemView.findViewById(R.id.valutazione_db_mie_rece);
        }
    }
}
