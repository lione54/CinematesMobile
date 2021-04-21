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
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.ScriviRecensioneActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SegnalazioniActivity extends AppCompatActivity {

    private String NomeUtenteSegnalatore, NomeUtenteSegnalato, FotoProfilo, IdRecensione;
    private CircleImageView Profilo;
    private AppCompatTextView UserSegnalatore, UserSegnalato;
    private AppCompatImageButton Indietro;
    private AppCompatButton Conferma, Annulla;
    private AppCompatCheckBox Inappropriato, Spoiler, Razzismo, Altro;
    private AppCompatEditText AltraMotivazione;
    private static final String INSURL = "http://192.168.1.9/cinematesdb/InviaSegnalazione.php";

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
                String Stato = "Inviata";
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
                    InviaSegnalazione(NomeUtenteSegnalatore,NomeUtenteSegnalato, stringBuilder, IdRecensione, Stato);
                }else {
                    Toast.makeText(SegnalazioniActivity.this, "Seleziona almeno un campo.", Toast.LENGTH_SHORT).show();
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


    private void InviaSegnalazione(String nomeUtenteSegnalatore, String nomeUtenteSegnalato, StringBuilder stringBuilder, String idRecensione, String stato) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(SegnalazioniActivity.this , "Segnalazione avvenuta con successo" , Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(SegnalazioniActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Segnalatore", nomeUtenteSegnalatore);
                params.put("User_Segnalato",nomeUtenteSegnalato);
                params.put("Id_Recensione", String.valueOf(idRecensione));
                params.put("Motivazione", String.valueOf(stringBuilder));
                params.put("Stato_Segnalazione",stato);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}