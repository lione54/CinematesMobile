package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelRichiesteAmicizia;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelSegnalazioni;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DBNotificheModelRichiesteAmicizia> richiesteAmiciziaList;
    private List<DBNotificheModelSegnalazioni> segnalazioniList;
    private int size = 0;
    private int secondposition = 0;
    private boolean firstitemofsecondlist = true;
    private static int SegnalazioneAccettate = 1;
    private static int SegnalazioneDeclinate = 2;
    private static int AmiciziaInviata = 3;
    private static int AmiciziaAccettata = 4;
    private static final String ACCURL = "http://192.168.1.9/cinematesdb/AccettaAmicizia.php";
    private static final String AMURL = "http://192.168.1.9/cinematesdb/DiventaAmico.php";
    private static final String NAMURL = "http://192.168.1.9/cinematesdb/RifiutaAmicizia.php";

    public NotificationAdapter(Activity activity, List<DBNotificheModelRichiesteAmicizia> richiesteAmiciziaList, List<DBNotificheModelSegnalazioni> segnalazioniList) {
        this.activity = activity;
        this.richiesteAmiciziaList = richiesteAmiciziaList;
        this.segnalazioniList = segnalazioniList;
        this.size = richiesteAmiciziaList.size() + segnalazioniList.size();
    }

    @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == AmiciziaInviata){
                View view = LayoutInflater.from(activity).inflate(R.layout.richiesta_amicizia_in_entrata, parent, false);
                return new DataHolderInvia(view);
        }else if(viewType == AmiciziaAccettata){
                View view = LayoutInflater.from(activity).inflate(R.layout.richiesta_amicizia_accettata, parent, false);
                return new DataHolderAccettata(view);
        }else if(viewType == SegnalazioneAccettate){
                View view = LayoutInflater.from(activity).inflate(R.layout.notifica_segnalazione_accettata, parent, false);
                return new DataHolderSegnalzioneAccettata(view);
        }else{
                View view = LayoutInflater.from(activity).inflate(R.layout.notifica_segnalazione_declinata, parent, false);
                return new DataHolderegnalazioneDeclinata(view);
        }
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == SegnalazioneAccettate){
            ((DataHolderSegnalzioneAccettata) holder).TitoloFilmSegnalato.setText(segnalazioniList.get(secondposition).getTitoloFilmRecensito());
            ((DataHolderSegnalzioneAccettata) holder).MotivazioneSegnalazione.setText(segnalazioniList.get(secondposition).getMotivazione());
            ((DataHolderSegnalzioneAccettata) holder).RimuoviSegnAccettata.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    segnalazioniList.remove(secondposition);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(secondposition, segnalazioniList.size());
                    size -= 1;
                }
            });
        }else if(getItemViewType(position) == SegnalazioneDeclinate){
            ((DataHolderegnalazioneDeclinata) holder).TitoloFilmSegnalatoDeclinato.setText(segnalazioniList.get(secondposition).getTitoloFilmRecensito());
            ((DataHolderegnalazioneDeclinata) holder).MotivazioneSegnalazioneDeclinata.setText(segnalazioniList.get(secondposition).getMotivazione());
            ((DataHolderegnalazioneDeclinata) holder).RimuoviSegnDeclinata.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    segnalazioniList.remove(secondposition);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(secondposition, segnalazioniList.size());
                    size -= 1;
                }
            });
        } else if(getItemViewType(position) == AmiciziaInviata){
           if(richiesteAmiciziaList.get(position).getFoto().equals("null")){
               ((DataHolderInvia) holder).ImmagineInvioRichiesta.setImageResource(R.drawable.ic_baseline_person_24);
           }else{
               Glide.with(activity).load(richiesteAmiciziaList.get(position).getFoto()).into(((DataHolderInvia) holder).ImmagineInvioRichiesta);
           }
            ((DataHolderInvia) holder).UserInvioRichiesta.setText(richiesteAmiciziaList.get(position).getAmicoDi());
            ((DataHolderInvia) holder).AccettaRichiesta.setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                   AccettaRichiesta(richiesteAmiciziaList.get(position).getUtente(),richiesteAmiciziaList.get(position).getAmicoDi());
                   richiesteAmiciziaList.remove(position);
                   notifyItemRemoved(position);
                   notifyItemRangeRemoved(position, richiesteAmiciziaList.size());
                   size -= 1;
               }
           });
            ((DataHolderInvia) holder).RifiutaRichiesta.setOnClickListener(new View.OnClickListener() {
               @Override public void onClick(View v) {
                   RifiutaRichiesta(richiesteAmiciziaList.get(position).getUtente(),richiesteAmiciziaList.get(position).getAmicoDi());
                   richiesteAmiciziaList.remove(position);
                   notifyItemRemoved(position);
                   notifyItemRangeRemoved(position, richiesteAmiciziaList.size());
                   size -= 1;
               }
           });
        }else if (getItemViewType(position) == AmiciziaAccettata){
            if(richiesteAmiciziaList.get(position).getFoto().equals("null")){
                ((DataHolderAccettata) holder).ImmagineRichiestaAccettata.setImageResource(R.drawable.ic_baseline_person_24);
            }else{
                Glide.with(activity).load(richiesteAmiciziaList.get(position).getFoto()).into(((DataHolderAccettata) holder).ImmagineRichiestaAccettata);
            }
            ((DataHolderAccettata) holder).UserRichiestaAccettata.setText(richiesteAmiciziaList.get(position).getAmicoDi());
            ((DataHolderAccettata) holder).RimuoviNotifica.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    DiventaAmico(richiesteAmiciziaList.get(position).getUtente(),richiesteAmiciziaList.get(position).getAmicoDi());
                    richiesteAmiciziaList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, richiesteAmiciziaList.size());
                    size -= 1;
                }
            });
        }
    }

    private void DiventaAmico(String utente, String amicoDi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(activity, "Notifica Rimossa", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario", utente);
                params.put("UserAmico", amicoDi);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void RifiutaRichiesta(String utente, String amicoDi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NAMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(activity, "Richiesta Rifiutata", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario", utente);
                params.put("UserAmico", amicoDi);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    private void AccettaRichiesta(String utente, String amicoDi) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ACCURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(activity, "Richiesta Accettata", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario", utente);
                params.put("UserAmico", amicoDi);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return size;
    }

    @Override public int getItemViewType(int position) {
        if (position > (richiesteAmiciziaList.size() - 1)) {
            if (firstitemofsecondlist){
                    firstitemofsecondlist = false;
                    secondposition = 0;
                    secondposition = secondposition + (position - richiesteAmiciziaList.size());
            }else{
                if (position != richiesteAmiciziaList.size()) {
                    firstitemofsecondlist = true;
                    secondposition = 0;
                    secondposition = secondposition + (position - richiesteAmiciziaList.size());
                }
            }
            if(segnalazioniList.get(secondposition).getStato().equals("Accettata")){
                return SegnalazioneAccettate;
            }else{
                return SegnalazioneDeclinate;
            }
        }else{
            if (richiesteAmiciziaList.get(position).getStatoRichiesta().equals("Accettata")){
                return AmiciziaAccettata;
            }else{
                return AmiciziaInviata;
            }
        }
    }

    public class DataHolderInvia extends RecyclerView.ViewHolder {

        public CircleImageView ImmagineInvioRichiesta;
        public AppCompatButton AccettaRichiesta, RifiutaRichiesta;
        public AppCompatTextView UserInvioRichiesta;

        public DataHolderInvia(@NonNull View itemView) {
            super(itemView);
            ImmagineInvioRichiesta = itemView.findViewById(R.id.image_user_notifica_richiesta);
            UserInvioRichiesta = itemView.findViewById(R.id.username_richiesta_ricevuta);
            AccettaRichiesta = itemView.findViewById(R.id.accetta_richiesta);
            RifiutaRichiesta = itemView.findViewById(R.id.rifiuta_richiesta);
        }
    }

    public class DataHolderAccettata extends RecyclerView.ViewHolder {

        public CircleImageView ImmagineRichiestaAccettata;
        public AppCompatImageView RimuoviNotifica;
        public AppCompatTextView UserRichiestaAccettata;

        public DataHolderAccettata(@NonNull View itemView) {
            super(itemView);
            ImmagineRichiestaAccettata = itemView.findViewById(R.id.image_user_notifica_accettata);
            UserRichiestaAccettata = itemView.findViewById(R.id.username_richiesta_accettata);
            RimuoviNotifica = itemView.findViewById(R.id.rimuovi_notifica);
        }
    }

    public class DataHolderSegnalzioneAccettata extends RecyclerView.ViewHolder{

        public AppCompatTextView TitoloFilmSegnalato, MotivazioneSegnalazione;
        public AppCompatImageView RimuoviSegnAccettata;

        public DataHolderSegnalzioneAccettata(@NonNull View itemView) {
            super(itemView);
            TitoloFilmSegnalato = itemView.findViewById(R.id.titolofilm_segnalato);
            MotivazioneSegnalazione = itemView.findViewById(R.id.motivazione_segnalazione);
            RimuoviSegnAccettata = itemView.findViewById(R.id.rimuovi_notifica_segnalazione_accettata);
        }
    }

    public class DataHolderegnalazioneDeclinata extends RecyclerView.ViewHolder{

        public AppCompatTextView TitoloFilmSegnalatoDeclinato, MotivazioneSegnalazioneDeclinata;
        public AppCompatImageView RimuoviSegnDeclinata;

        public DataHolderegnalazioneDeclinata(@NonNull View itemView) {
            super(itemView);
            TitoloFilmSegnalatoDeclinato = itemView.findViewById(R.id.titolofilm_segnalato_declinato);
            MotivazioneSegnalazioneDeclinata = itemView.findViewById(R.id.motivazione_segnalazione_declinata);
            RimuoviSegnDeclinata = itemView.findViewById(R.id.rimuovi_notifica_segnalazione_declinata);
        }
    }
}
