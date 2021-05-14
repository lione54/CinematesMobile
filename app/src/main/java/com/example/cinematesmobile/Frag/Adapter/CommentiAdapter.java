package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.CommentiActivity;
import com.example.cinematesmobile.Frag.Model.DBModelCommenti;
import com.example.cinematesmobile.ModelDBInterno.DBModelFotoProfiloResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelFotoProfilo;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentiAdapter extends RecyclerView.Adapter<CommentiAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelCommenti> result;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    public CommentiAdapter(Activity activity, List<DBModelCommenti> result) {
        this.activity = activity;
        this.result = result;
    }

    @NonNull @NotNull @Override public DataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_commenti, parent, false);
        return new DataHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull @NotNull DataHolder holder, int position) {
        DBModelCommenti dbModelCommenti = result.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelFotoProfiloResponce> fotoProfiloResponceCall = retrofitServiceDBInterno.PrendiFotoProfilo(dbModelCommenti.getChi_ha_Commentato());
        fotoProfiloResponceCall.enqueue(new Callback<DBModelFotoProfiloResponce>() {
            @Override public void onResponse(Call<DBModelFotoProfiloResponce> call, Response<DBModelFotoProfiloResponce> response) {
                DBModelFotoProfiloResponce dbModelFotoProfiloResponce = response.body();
                if(dbModelFotoProfiloResponce != null){
                    List<DBModelFotoProfilo> fotoProfilos = dbModelFotoProfiloResponce.getResults();
                    if(!(fotoProfilos.isEmpty())){
                        if(fotoProfilos.get(0).getFotoProfilo() != null){
                            Glide.with(activity).load(fotoProfilos.get(0).getFotoProfilo()).into(holder.ImageProfilo);
                        }else{
                            holder.ImageProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
                        }
                    }
                }
            }
            @Override public void onFailure(Call<DBModelFotoProfiloResponce> call, Throwable t) {
                Toast.makeText(activity,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        holder.UsernameCommento.setText(dbModelCommenti.getChi_ha_Commentato());
        holder.DataCommento.setText(dbModelCommenti.getQuando_ha_commentato());
        holder.TestoCommento.setText(dbModelCommenti.getTesto_commento());
    }

    @Override public int getItemCount() {
        return result.size();
    }

    public class DataHolder extends RecyclerView.ViewHolder {

        public CircleImageView ImageProfilo;
        public AppCompatTextView UsernameCommento, DataCommento, TestoCommento;

        public DataHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ImageProfilo = itemView.findViewById(R.id.image_user_commento);
            UsernameCommento = itemView.findViewById(R.id.username_di_chi_commenta);
            DataCommento = itemView.findViewById(R.id.data_commento);
            TestoCommento = itemView.findViewById(R.id.corpo_commento);
        }
    }
}
