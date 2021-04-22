package com.example.cinematesmobile.Recensioni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScriviRecensioneActivity extends AppCompatActivity {

    private String NomeUtente, UrlImmagine, TitoloFilm;
    private AppCompatTextView Nome;
    private AppCompatEditText CorpoRecensione;
    private AppCompatButton Invia;
    private AppCompatButton Annulla;
    private RatingBar Voto;
    private AlertDialog.Builder dialogBilder;
    private AlertDialog MettiZero;
    private AppCompatButton Si, No;
    private AppCompatImageButton Previously;
    private CircleImageView ImmagineRece;
    private static final String INSURL = "http://192.168.178.48/cinematesdb/AggiungiRecensioneAlDatabase.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrivi_recensione);
        NomeUtente= getIntent().getExtras().getString("Nome_Utente");
        TitoloFilm = getIntent().getExtras().getString("Titolo_Film");
        UrlImmagine = getIntent().getExtras().getString("Foto_Profilo");;
        ImmagineRece = findViewById(R.id.immagineRece);
        Nome = findViewById(R.id.nome_utente);
        CorpoRecensione = findViewById(R.id.corp_rece);
        Previously = findViewById(R.id.previously);
        Invia = findViewById(R.id.invia_button);
        Annulla = findViewById(R.id.annu_button);
        Voto = findViewById(R.id.Votofilm);
        if(UrlImmagine.equals("null")){
            ImmagineRece.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(this).load(UrlImmagine).into(ImmagineRece);
        }
        Nome.setText(NomeUtente);
        Invia.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(CorpoRecensione.length() > 0 && CorpoRecensione.length() < 600){
                    float Punteggio = Voto.getRating();
                    String Recensione = CorpoRecensione.getText().toString();
                    if(Punteggio == 0){
                        dialogBilder = new AlertDialog.Builder(ScriviRecensioneActivity.this);
                        final View PopUpView = getLayoutInflater().inflate(R.layout.disclaimerzero, null);
                        Si = (AppCompatButton) PopUpView.findViewById(R.id.conf_button);
                        No = (AppCompatButton) PopUpView.findViewById(R.id.anal_button);
                        dialogBilder.setView(PopUpView);
                        MettiZero = dialogBilder.create();
                        MettiZero.show();
                        Si.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Inserisci_Recensione(TitoloFilm, Recensione, Punteggio, NomeUtente);
                                MettiZero.dismiss();
                                onBackPressed();
                            }
                        });
                        No.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                MettiZero.dismiss();
                            }
                        });
                    }else{
                        Inserisci_Recensione(TitoloFilm, Recensione, Punteggio, NomeUtente);
                        onBackPressed();
                    }
                }else{
                    if(CorpoRecensione.length() <= 0){
                        Toast.makeText(ScriviRecensioneActivity.this, "Scrivi qualcosa", Toast.LENGTH_SHORT).show();
                    }else if(CorpoRecensione.length() >= 600){
                        Toast.makeText(ScriviRecensioneActivity.this, "Superato la lunghezza massima della recensione", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Annulla.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento recensione annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento recensione annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void Inserisci_Recensione(String titoloFilm, String recensione, float punteggio, String nomeUtente) {
        String titoloMod = titoloFilm.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(ScriviRecensioneActivity.this , "Recensione aggiunta" , Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScriviRecensioneActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Corpo_Recensione", recensione);
                params.put("Valutazione",String.valueOf(punteggio));
                params.put("Titolo_Film_Recensito", titoloMod);
                params.put("User_Recensore", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}