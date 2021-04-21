package com.example.cinematesmobile.Frag.Adapter;

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
import com.example.cinematesmobile.Frag.Model.DBModelUserAmici;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.ActivityProfiloAltroUtente;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MieiAmiciAdapter extends RecyclerView.Adapter<MieiAmiciAdapter.DataHolder>{

    private Activity activity;
    private List<DBModelUserAmici> amiciList;
    private String Username,Proprietario;
    private static final String RIMURL = "http://192.168.1.9/cinematesdb/RimuoviAmico.php";

    public MieiAmiciAdapter(Activity activity, List<DBModelUserAmici> amiciList, String username, String proprietario) {
        this.activity = activity;
        this.amiciList = amiciList;
        Username = username;
        Proprietario = proprietario;
    }

    @NonNull @Override public MieiAmiciAdapter.DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_miei_amici, parent, false);
        return new MieiAmiciAdapter.DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull MieiAmiciAdapter.DataHolder holder, int position) {
        DBModelUserAmici data = amiciList.get(position);
        if(data.getFoto_Profilo().equals("null")){
            holder.ImageUserAmico.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }else{
            Glide.with(activity).load(data.getFoto_Profilo()).into(holder.ImageUserAmico);
        }
        holder.UsernameAmico.setText(data.getE_Amico_Di());
        if(!(Username.equals(Proprietario))){
            holder.RimuovidaAmici.setVisibility(View.GONE);
        }else {
            holder.RimuovidaAmici.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RimuoviAmico(data.getE_Amico_Di());
                    amiciList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, amiciList.size());
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityProfiloAltroUtente.class);
                intent.putExtra("UsernameAltroUtente", data.getE_Amico_Di());
                intent.putExtra("UsernameProprietario", Proprietario);
                activity.startActivity(intent);
            }
        });
    }

    private void RimuoviAmico(String e_amico_di) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(activity, e_amico_di + " Rimosso dagli amici", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario",Username);
                params.put("User_Da_Rimuovere", e_amico_di);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    @Override public int getItemCount() {
        return amiciList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        public CircleImageView ImageUserAmico;
        public AppCompatTextView UsernameAmico;
        public AppCompatButton RimuovidaAmici;

        public DataHolder(@NonNull View itemView) {
            super(itemView);
            ImageUserAmico = itemView.findViewById(R.id.image_user_amico);
            UsernameAmico = itemView.findViewById(R.id.username_amico);
            RimuovidaAmici = itemView.findViewById(R.id.rimuovida_amici);
        }
    }
}
