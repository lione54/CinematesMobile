package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MieRecensioniAdapter extends RecyclerView.Adapter<MieRecensioniAdapter.DataHolder> {

    private Activity activity;
    private List<DBModelRecensioni> recensioniList;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

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
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(dbModelRecensioni.getFoto().equals("null")){
            holder.FotoProfilo.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(activity).load(dbModelRecensioni.getFoto()).into(holder.FotoProfilo);
        }
        holder.Username.setText(dbModelRecensioni.getTitolo_Film().replaceAll("/", "'"));
        holder.DataRecensione.setText(dbModelRecensioni.getData_Pubblicazione());
        holder.CorpoRecensione.setText(dbModelRecensioni.getTesto_Recensione());
        holder.Voto.setRating(dbModelRecensioni.getValutazione());
        holder.Rimuovi.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelResponseToInsert> rimuoviCall = retrofitServiceDBInterno.RimuoviRecensione(String.valueOf(dbModelRecensioni.getId_Recensione()), dbModelRecensioni.getUser_Recensore());
                rimuoviCall.enqueue(new Callback<DBModelResponseToInsert>() {
                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                        if (dbModelResponseToInsert != null) {
                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                Toast.makeText(activity, "Recensione rimossa con successo", Toast.LENGTH_SHORT).show();
                                recensioniList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, recensioniList.size());
                            }else{
                                Toast.makeText(activity, "Rimozione recensione fallita", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(activity, "Impossibile eliminare recensione.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                        Toast.makeText(activity, "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
