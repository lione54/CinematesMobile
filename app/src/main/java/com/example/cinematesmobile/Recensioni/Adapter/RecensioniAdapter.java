package com.example.cinematesmobile.Recensioni.Adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecensioniAdapter extends RecyclerView.Adapter<RecensioniAdapter.DataViewHolder>{

    private Context mCtx;
    private List<DBModelRecensioni> recensioniList;


    public RecensioniAdapter(Context mCtx, List<DBModelRecensioni> recensioniList) {
        this.mCtx = mCtx;
        this.recensioniList = recensioniList;
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
                Toast.makeText(mCtx, "Prossimamente",Toast.LENGTH_SHORT).show();
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
}
