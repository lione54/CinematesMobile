package com.example.cinematesmobile.Recensioni.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.CommentiActivity;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecensioniAdapterPerCommenti extends RecyclerView.Adapter<RecensioniAdapterPerCommenti.DataHolder> {

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private String User_Segnalatore;


    public RecensioniAdapterPerCommenti(Activity activity, List<DBModelRecensioni> recensioniList, String user_Segnalatore) {
        this.activity = activity;
        this.recensioniList = recensioniList;
        User_Segnalatore = user_Segnalatore;
    }

    @NonNull @NotNull @Override public DataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_recensioni_per_commenti, parent, false);
        return new RecensioniAdapterPerCommenti.DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull @NotNull DataHolder holder, int position) {
        DBModelRecensioni dbModelRecensioni = recensioniList.get(position);
        if(dbModelRecensioni.getFoto() == null){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }else{
            Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getUser_Recensore());
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
    }

    @Override public int getItemCount() {
        return recensioniList.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        private CircleImageView FotoProfilo;
        public AppCompatTextView Username, DataRecensione, CorpoRecensione;
        public RatingBar Voto;

        public DataHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.image_user_viusalizza_rece);
            Username = itemView.findViewById(R.id.username_ricerca);
            DataRecensione = itemView.findViewById(R.id.data_recensione);
            CorpoRecensione = itemView.findViewById(R.id.corpo_recensione);
            Voto = itemView.findViewById(R.id.valutazione_db);
        }
    }
}
