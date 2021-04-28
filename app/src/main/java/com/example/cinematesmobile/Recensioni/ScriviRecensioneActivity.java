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
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

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
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
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
                                String titoloMod = TitoloFilm.replaceAll("'", "/");
                                Call<DBModelResponseToInsert> scrivirecensioneCall = retrofitServiceDBInterno.ScriviRecenisone(Recensione, String.valueOf(Punteggio), titoloMod, NomeUtente);
                                scrivirecensioneCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                    @Override public void onResponse(Call<DBModelResponseToInsert> call, Response<DBModelResponseToInsert> response) {
                                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                        if(dbModelResponseToInsert != null) {
                                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                Toast.makeText(ScriviRecensioneActivity.this , "Recensione aggiunta con successo." , Toast.LENGTH_SHORT).show();
                                                MettiZero.dismiss();
                                                onBackPressed();
                                            }else{
                                                Toast.makeText(ScriviRecensioneActivity.this , "Impossibile aggiungere recenisone." , Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(ScriviRecensioneActivity.this , "Impossibile aggiungere recenisone." , Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override public void onFailure(Call<DBModelResponseToInsert> call, Throwable t) {
                                        Toast.makeText(ScriviRecensioneActivity.this , "Ops qualcosa è andato storto." , Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        No.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                MettiZero.dismiss();
                            }
                        });
                    }else{
                        String titoloMod = TitoloFilm.replaceAll("'", "/");
                        Call<DBModelResponseToInsert> scrivirecensioneCall = retrofitServiceDBInterno.ScriviRecenisone(Recensione, String.valueOf(Punteggio), titoloMod, NomeUtente);
                        scrivirecensioneCall.enqueue(new Callback<DBModelResponseToInsert>() {
                            @Override public void onResponse(Call<DBModelResponseToInsert> call, Response<DBModelResponseToInsert> response) {
                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                if(dbModelResponseToInsert != null) {
                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                        Toast.makeText(ScriviRecensioneActivity.this , "Recensione aggiunta con successo." , Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }else{
                                        Toast.makeText(ScriviRecensioneActivity.this , "Impossibile aggiungere recenisone." , Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(ScriviRecensioneActivity.this , "Impossibile aggiungere recenisone." , Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override public void onFailure(Call<DBModelResponseToInsert> call, Throwable t) {
                                Toast.makeText(ScriviRecensioneActivity.this , "Ops qualcosa è andato storto." , Toast.LENGTH_SHORT).show();
                            }
                        });
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
}