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
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Model.DBModelUserAmici;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Search.ActivityProfiloAltroUtente;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MieiAmiciAdapter extends RecyclerView.Adapter<MieiAmiciAdapter.DataHolder>{

    private Activity activity;
    private List<DBModelUserAmici> amiciList;
    private String Username,Proprietario;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

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
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(data.getFoto_Profilo() == null){
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
                    Call<DBModelResponseToInsert> rimuoviamiciCall = retrofitServiceDBInterno.RimuoviAmico(Proprietario, data.getE_Amico_Di());
                    rimuoviamiciCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if (dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")){
                                    Toast.makeText(activity, data.getE_Amico_Di() + " Rimosso dagli amici.", Toast.LENGTH_SHORT).show();
                                    amiciList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, amiciList.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione di " + data.getE_Amico_Di() + " dagli amici fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(activity, "Impossibile eliminare " + data.getE_Amico_Di() + " dagli amici.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
