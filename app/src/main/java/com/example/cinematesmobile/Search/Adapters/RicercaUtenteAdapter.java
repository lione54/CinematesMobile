package com.example.cinematesmobile.Search.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Profilo;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.ActivityProfiloUtenteCercato;
import com.example.cinematesmobile.Search.Model.DBModelDataFilms;
import com.example.cinematesmobile.Search.Model.DBModelDataUser;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.util.List;

public class RicercaUtenteAdapter extends RecyclerView.Adapter<RicercaUtenteAdapter.DataViewHolder> {
    private Context mCtx;
    private List<DBModelDataUser> dataList;

    public RicercaUtenteAdapter(Context mCtx, List<DBModelDataUser> dataList) {
        this.mCtx = mCtx;
        this.dataList = dataList;
    }

    @NonNull @Override public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_ricerca_utenti, null);
        return new DataViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DBModelDataUser data = dataList.get(position);
        Glide.with(mCtx).load(data.getImmagineProfilo()).into(holder.FotoProfilo);
        holder.Username.setText(data.getUsername());
        int id = data.getId_utente();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mCtx, ActivityProfiloUtenteCercato.class);
                intent.putExtra("id", String.valueOf(id));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView FotoProfilo;
        public AppCompatTextView Username;

        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            FotoProfilo = itemView.findViewById(R.id.imagine_profilo_utente);
            Username = itemView.findViewById(R.id.username_utente);
            RandomTransitionGenerator generator = new RandomTransitionGenerator(1000, new DecelerateInterpolator());
            FotoProfilo.setTransitionGenerator(generator);
        }
    }
}
