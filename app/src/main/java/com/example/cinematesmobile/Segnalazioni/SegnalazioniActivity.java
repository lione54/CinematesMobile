package com.example.cinematesmobile.Segnalazioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.ScriviRecensioneActivity;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SegnalazioniActivity extends AppCompatActivity {

    private String NomeUtenteSegnalatore, NomeUtenteSegnalato, FotoProfilo, IdRecensione;
    private CircleImageView Profilo;
    private AppCompatTextView UserSegnalatore, UserSegnalato;
    private AppCompatImageButton Indietro;
    private AppCompatButton Conferma, Annulla;
    private AppCompatCheckBox Inappropriato, Spoiler, Razzismo, Altro;
    private AppCompatEditText AltraMotivazione;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segnalazioni);
        NomeUtenteSegnalatore = getIntent().getExtras().getString("Nome_Utente_Segnalatore");
        NomeUtenteSegnalato = getIntent().getExtras().getString("Nome_Utente_Segnalato");
        FotoProfilo= getIntent().getExtras().getString("Foto_Profilo");
        IdRecensione= getIntent().getExtras().getString("Id_Recensione");
        Profilo = findViewById(R.id.immagine_user_x_segnalazione);
        UserSegnalatore = findViewById(R.id.nome_utente_segnalatore);
        UserSegnalato = findViewById(R.id.nomeutente_segnalato);
        Indietro = findViewById(R.id.previously_segn);
        Conferma = findViewById(R.id.conferma_segnalazione);
        Annulla = findViewById(R.id.Annulla_segnalazione);
        Inappropriato = findViewById(R.id.contenuto_ina);
        Spoiler = findViewById(R.id.spoiler);
        Razzismo = findViewById(R.id.razzismo);
        Altro = findViewById(R.id.altro);
        AltraMotivazione = findViewById(R.id.Altra_motivazione);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(FotoProfilo.equals("null")){
            Profilo.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(SegnalazioniActivity.this).load(FotoProfilo).into(Profilo);
        }
        UserSegnalatore.setText(NomeUtenteSegnalatore);
        UserSegnalato.setText(NomeUtenteSegnalato);
        Indietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(SegnalazioniActivity.this, "Segnalazione annullata", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Altro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AltraMotivazione.setVisibility(View.VISIBLE);
                }else{
                    AltraMotivazione.setVisibility(View.GONE);
                }
            }
        });
        Conferma.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                if(Inappropriato.isChecked() || Spoiler.isChecked() || Razzismo.isChecked() || Altro.isChecked()){
                    if(Inappropriato.isChecked()) {
                        stringBuilder.append(Inappropriato.getText().toString());
                    }
                    if(Spoiler.isChecked()) {
                        stringBuilder.append("-").append(Spoiler.getText().toString());
                    }
                    if(Razzismo.isChecked()) {
                        stringBuilder.append("-").append(Razzismo.getText().toString());
                    }
                    if(Altro.isChecked()) {
                        if(AltraMotivazione.isShown() && AltraMotivazione.length() > 0){
                            for(int i=0; i<AltraMotivazione.length(); i++){
                                stringBuilder.append("-").append(AltraMotivazione.getText().toString());
                            }
                        }else {
                            Toast.makeText(SegnalazioniActivity.this, "Scrivi qualcosa", Toast.LENGTH_SHORT).show();
                        }
                    }
                    Call<DBModelResponseToInsert> inviaSegnalazioneCall = retrofitServiceDBInterno.InviaSegnalazione(NomeUtenteSegnalatore, NomeUtenteSegnalato, String.valueOf(IdRecensione), stringBuilder.toString());
                    inviaSegnalazioneCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(Call<DBModelResponseToInsert> call, Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null) {
                                if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                    Toast.makeText(SegnalazioniActivity.this , "Segnalazione avvenuta con successo" , Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(SegnalazioniActivity.this, "Invio segnalazione nei confronti di " + NomeUtenteSegnalato + " Fallito.", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                }
                            }else {
                                Toast.makeText(SegnalazioniActivity.this, "Impossibile Inviare Segnalazione", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override public void onFailure(Call<DBModelResponseToInsert> call, Throwable t) {
                            Toast.makeText(SegnalazioniActivity.this, "Ops Qualcosa è Andato Storto.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(SegnalazioniActivity.this, "Seleziona almeno un campo", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Annulla.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(SegnalazioniActivity.this, "Segnalazione annullata", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }
}