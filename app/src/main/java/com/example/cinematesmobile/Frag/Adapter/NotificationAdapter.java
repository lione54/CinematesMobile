package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Model.DBModelNotifiche;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DBModelNotifiche> results;
    private String UserProprietario;
    private static int SegnalazioneAccettate = 1;
    private static int SegnalazioneDeclinate = 2;
    private static int AmiciziaInviata = 3;
    private static int AmiciziaAccettata = 4;
    private static int EmojLike = 5;
    private static int EmojDislike = 6;
    private static int Commento = 7;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    public NotificationAdapter(Activity activity, List<DBModelNotifiche> results, String userProprietario) {
        this.activity = activity;
        this.results = results;
        UserProprietario = userProprietario;
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
        }else if(viewType == SegnalazioneDeclinate){
            View view = LayoutInflater.from(activity).inflate(R.layout.notifica_segnalazione_declinata, parent, false);
            return new DataHolderSegnalazioneDeclinata(view);
        }else if(viewType == EmojLike){
            View view = LayoutInflater.from(activity).inflate(R.layout.notifica_emoj_like, parent, false);
            return new DataHolderEmojLike(view);
        }else if(viewType == EmojDislike){
            View view = LayoutInflater.from(activity).inflate(R.layout.notifica_emoj_dislike, parent, false);
            return new DataHolderEmojDislike(view);
        }else{
            View view = LayoutInflater.from(activity).inflate(R.layout.notifica_commento, parent, false);
            return new DataHolderCommenti(view);
        }
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DBModelNotifiche dbModelNotifiche = results.get(position);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if (getItemViewType(position) == SegnalazioneAccettate){
            ((DataHolderSegnalzioneAccettata) holder).TitoloFilmSegnalato.setText(dbModelNotifiche.getTitolo_Film());
            ((DataHolderSegnalzioneAccettata) holder).MotivazioneSegnalazione.setText(dbModelNotifiche.getMotivazione());
            ((DataHolderSegnalzioneAccettata) holder).RimuoviSegnAccettata.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Notifica rimossa.", Toast.LENGTH_SHORT).show();
                                    results.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, results.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if(getItemViewType(position) == SegnalazioneDeclinate){
            ((DataHolderSegnalazioneDeclinata) holder).TitoloFilmSegnalatoDeclinato.setText(dbModelNotifiche.getTitolo_Film());
            ((DataHolderSegnalazioneDeclinata) holder).MotivazioneSegnalazioneDeclinata.setText(dbModelNotifiche.getMotivazione());
            ((DataHolderSegnalazioneDeclinata) holder).RimuoviSegnDeclinata.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    results.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeRemoved(position, results.size());
                }
            });
        } else if(getItemViewType(position) == AmiciziaInviata){
            if(results.get(position).getFoto_Profilo() != null){
                Glide.with(activity).load(dbModelNotifiche.getFoto_Profilo()).into(((DataHolderInvia) holder).ImmagineInvioRichiesta);
            }else{
                ((DataHolderInvia) holder).ImmagineInvioRichiesta.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }
            ((DataHolderInvia) holder).UserInvioRichiesta.setText(dbModelNotifiche.getUser_che_fa_azione());
            ((DataHolderInvia) holder).AccettaRichiesta.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> accettaAmicoCall = retrofitServiceDBInterno.AccettaAmicizia(UserProprietario, dbModelNotifiche.getUser_che_fa_azione());
                    accettaAmicoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Call<DBModelResponseToInsert> diventaAmicoCall = retrofitServiceDBInterno.DiventaAmico(UserProprietario, dbModelNotifiche.getUser_che_fa_azione());
                                    diventaAmicoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                            if(dbModelResponseToInsert != null){
                                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                                                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                            if(dbModelResponseToInsert != null){
                                                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                                    Toast.makeText(activity, "Richiesta accettata.", Toast.LENGTH_SHORT).show();
                                                                    results.remove(position);
                                                                    notifyItemRemoved(position);
                                                                    notifyItemRangeRemoved(position, results.size());
                                                                }else{
                                                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else {
                                                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }else{
                                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(activity, "Accettazione richiesta fallito.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile accettare richiesta.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            ((DataHolderInvia) holder).RifiutaRichiesta.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rifiutaAmiciziaCall = retrofitServiceDBInterno.RifiutaAmicizia(UserProprietario, dbModelNotifiche.getUser_che_fa_azione());
                    rifiutaAmiciziaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                            if(dbModelResponseToInsert != null){
                                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                    Toast.makeText(activity, "Richiesta rifiutata.", Toast.LENGTH_SHORT).show();
                                                    results.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeRemoved(position, results.size());
                                                }else{
                                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                                }
                                            }else {
                                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(activity, "Rifiuto richiesta fallito.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rifiutare richiesta.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if (getItemViewType(position) == AmiciziaAccettata){
            if(dbModelNotifiche.getFoto_Profilo() != null){
                Glide.with(activity).load(dbModelNotifiche.getFoto_Profilo()).into(((DataHolderAccettata) holder).ImmagineRichiestaAccettata);
            }else{
                ((DataHolderAccettata) holder).ImmagineRichiestaAccettata.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }
            ((DataHolderAccettata) holder).UserRichiestaAccettata.setText(dbModelNotifiche.getUser_che_fa_azione());
            ((DataHolderAccettata) holder).RimuoviNotifica.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Notifica rimossa.", Toast.LENGTH_SHORT).show();
                                    results.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, results.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if (getItemViewType(position) == EmojLike){
            if(dbModelNotifiche.getFoto_Profilo() != null){
                Glide.with(activity).load(dbModelNotifiche.getFoto_Profilo()).into(((DataHolderEmojLike) holder).ImmagineEmojLike);
            }else{
                ((DataHolderEmojLike) holder).ImmagineEmojLike.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }
            ((DataHolderEmojLike) holder).UserEmojLike.setText(dbModelNotifiche.getUser_che_fa_azione());
            if(results.get(position).getQuale_post().equals("Lista")) {
                String riga1 = "ha messo like alla tua lista di nome ";
                String riga2 = dbModelNotifiche.getArgomento_notifica() + ".";
                ((DataHolderEmojLike) holder).ACosaHaMessoEmojLike.setText(riga1);
                ((DataHolderEmojLike) holder).TitoloEmojLike.setText(riga2);
            }else if(results.get(position).getQuale_post().equals("Recensioni")){
                String riga1 = "ha messo like alla tua recensione del film ";
                String riga2 =  dbModelNotifiche.getArgomento_notifica() + ".";
                ((DataHolderEmojLike) holder).ACosaHaMessoEmojLike.setText(riga1);
                ((DataHolderEmojLike) holder).TitoloEmojLike.setText(riga2);
            }
            ((DataHolderEmojLike) holder).RimuoviNotifica.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Notifica rimossa.", Toast.LENGTH_SHORT).show();
                                    results.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, results.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if (getItemViewType(position) == EmojDislike){
            if(dbModelNotifiche.getFoto_Profilo() != null){
                Glide.with(activity).load(dbModelNotifiche.getFoto_Profilo()).into(((DataHolderEmojDislike) holder).ImmagineEmojDislike);
            }else{
                ((DataHolderEmojDislike) holder).ImmagineEmojDislike.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }
            ((DataHolderEmojDislike) holder).UserEmojDislike.setText(results.get(position).getUser_che_fa_azione());
            if(dbModelNotifiche.getQuale_post().equals("Lista")) {
                String riga1 = "ha messo dislike alla tua lista di nome ";
                String riga2 = dbModelNotifiche.getArgomento_notifica() + ".";
                ((DataHolderEmojDislike) holder).ACosaHaMessoEmojDislike.setText(riga1);
                ((DataHolderEmojDislike) holder).TitoloEmojDislike.setText(riga2);
            }else if(dbModelNotifiche.getQuale_post().equals("Recensione")){
                String riga1 = "ha messo dislike alla tua recensione del film ";
                String riga2 =  results.get(position).getArgomento_notifica() + ".";
                ((DataHolderEmojDislike) holder).ACosaHaMessoEmojDislike.setText(riga1);
                ((DataHolderEmojDislike) holder).TitoloEmojDislike.setText(riga2);
            }
            ((DataHolderEmojDislike) holder).RimuoviNotifica.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Notifica rimossa.", Toast.LENGTH_SHORT).show();
                                    results.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, results.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }else if (getItemViewType(position) == Commento){
            if(dbModelNotifiche.getFoto_Profilo() != null){
                Glide.with(activity).load(dbModelNotifiche.getFoto_Profilo()).into(((DataHolderCommenti) holder).ImmagineCommento);
            }else{
                ((DataHolderCommenti) holder).ImmagineCommento.setImageResource(R.drawable.ic_baseline_person_24_orange);
            }
            ((DataHolderCommenti) holder).UserCommento.setText(results.get(position).getUser_che_fa_azione());
            if(dbModelNotifiche.getQuale_post().equals("Lista")) {
                String riga1 = "ha commentato la tua lista di nome";
                String riga2 = dbModelNotifiche.getArgomento_notifica() + ".";
                ((DataHolderCommenti) holder).CosaHaCommentato.setText(riga1);
                ((DataHolderCommenti) holder).TitolocosahaCommentato.setText(riga2);
            }else if(dbModelNotifiche.getQuale_post().equals("Recensioni")){
                String riga1 = "ha commentato la tua recensione del film ";
                String riga2 = dbModelNotifiche.getArgomento_notifica() + ".";
                ((DataHolderCommenti) holder).CosaHaCommentato.setText(riga1);
                ((DataHolderCommenti) holder).TitolocosahaCommentato.setText(riga2);
            }
            ((DataHolderCommenti) holder).RimuoviNotifica.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Call<DBModelResponseToInsert> rimuovinotificaCall = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, dbModelNotifiche.getTipo_Notifica(), dbModelNotifiche.getQuale_post(), dbModelNotifiche.getArgomento_notifica());
                    rimuovinotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(activity, "Notifica rimossa.", Toast.LENGTH_SHORT).show();
                                    results.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeRemoved(position, results.size());
                                }else{
                                    Toast.makeText(activity, "Rimozione notifica fallita.", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(activity, "Impossibile rimuovere notifica.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(activity, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override public int getItemCount() {
        return results.size();
    }

    @Override public int getItemViewType(int position) {
        if (results.get(position).getTipo_Notifica().equals("Like") || results.get(position).getTipo_Notifica().equals("Dislike")) {
            if(results.get(position).getTipo_Notifica().equals("Like")){
                return EmojLike;
            }else{
                return EmojDislike;
            }
        }else if (results.get(position).getTipo_Notifica().equals("Commento")){
                return Commento;
        }else if(results.get(position).getTipo_Notifica().equals("Richiesta")){
            if(results.get(position).getArgomento_notifica().equals("Accettata")){
                return AmiciziaAccettata;
            }else {
                return AmiciziaInviata;
            }
        }else{
            if(results.get(position).getTipo_Notifica().equals("Segnalazione") && results.get(position).getArgomento_notifica().equals("Accettata")){
                    return SegnalazioneAccettate;
            }else{
                return SegnalazioneDeclinate;
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

    public class DataHolderSegnalazioneDeclinata extends RecyclerView.ViewHolder{

        public AppCompatTextView TitoloFilmSegnalatoDeclinato, MotivazioneSegnalazioneDeclinata;
        public AppCompatImageView RimuoviSegnDeclinata;

        public DataHolderSegnalazioneDeclinata(@NonNull View itemView) {
            super(itemView);
            TitoloFilmSegnalatoDeclinato = itemView.findViewById(R.id.titolofilm_segnalato_declinato);
            MotivazioneSegnalazioneDeclinata = itemView.findViewById(R.id.motivazione_segnalazione_declinata);
            RimuoviSegnDeclinata = itemView.findViewById(R.id.rimuovi_notifica_segnalazione_declinata);
        }
    }

    public class DataHolderEmojLike extends RecyclerView.ViewHolder{

        public CircleImageView ImmagineEmojLike;
        public AppCompatTextView UserEmojLike, ACosaHaMessoEmojLike, TitoloEmojLike;
        public AppCompatImageView RimuoviNotifica;

        public DataHolderEmojLike(@NonNull View itemView) {
            super(itemView);
            ImmagineEmojLike = itemView.findViewById(R.id.image_user_notifica_emoj_like);
            UserEmojLike = itemView.findViewById(R.id.username_emoj_like);
            ACosaHaMessoEmojLike = itemView.findViewById(R.id.a_cosa_ha_messo_like);
            TitoloEmojLike = itemView.findViewById(R.id.titolo_per_like);
            RimuoviNotifica = itemView.findViewById(R.id.rimuovi_notifica);
        }
    }

    public class DataHolderEmojDislike extends RecyclerView.ViewHolder{

        public CircleImageView ImmagineEmojDislike;
        public AppCompatTextView UserEmojDislike, ACosaHaMessoEmojDislike, TitoloEmojDislike;
        public AppCompatImageView RimuoviNotifica;

        public DataHolderEmojDislike(@NonNull View itemView) {
            super(itemView);
            ImmagineEmojDislike = itemView.findViewById(R.id.image_user_notifica_emoj_dislike);
            UserEmojDislike = itemView.findViewById(R.id.username_emoj_dislike);
            ACosaHaMessoEmojDislike = itemView.findViewById(R.id.a_cosa_ha_messo_dislike);
            TitoloEmojDislike = itemView.findViewById(R.id.titolo_per_dislike);
            RimuoviNotifica = itemView.findViewById(R.id.rimuovi_notifica);
        }
    }

    public class DataHolderCommenti extends RecyclerView.ViewHolder{

        public CircleImageView ImmagineCommento;
        public AppCompatTextView UserCommento, CosaHaCommentato, TitolocosahaCommentato;
        public AppCompatImageView RimuoviNotifica;

        public DataHolderCommenti(@NonNull View itemView) {
            super(itemView);
            ImmagineCommento = itemView.findViewById(R.id.image_user_notifica_commento);
            UserCommento = itemView.findViewById(R.id.username_commento);
            CosaHaCommentato = itemView.findViewById(R.id.cosa_ha_commentato);
            TitolocosahaCommentato = itemView.findViewById(R.id.titolo_per_commento);
            RimuoviNotifica = itemView.findViewById(R.id.rimuovi_notifica);
        }
    }
}
