package com.example.cinematesmobile.Recensioni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
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
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScriviRecensioneActivity extends AppCompatActivity {

    private String NomeUtente;
    private AppCompatTextView Nome;
    private Integer Id_Film;
    private AppCompatEditText CorpoRecensione;
    private AppCompatButton Invia;
    private AppCompatButton Annulla;
    private RatingBar Voto;
    private AlertDialog.Builder dialogBilder;
    private AlertDialog MettiZero;
    private AppCompatButton Si, No;
    private AppCompatImageButton Previously;
    public static final String JSON_ARRAY = "dbdata";
    private static final String INSURL = "http://192.168.1.9/cinematesdb/AggiungiRecensioneAlDatabase.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSeRecensionePresente.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrivi_recensione);
        NomeUtente= getIntent().getExtras().getString("Nome_Utente");
        Id_Film = getIntent().getExtras().getInt("Id_Film");
        Nome = findViewById(R.id.nome_utente);
        CorpoRecensione = findViewById(R.id.corp_rece);
        Previously = findViewById(R.id.previously);
        Invia = findViewById(R.id.invia_button);
        Annulla = findViewById(R.id.annu_button);
        Voto = findViewById(R.id.Votofilm);
        Nome.setText(NomeUtente);
        Invia.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(CorpoRecensione.length() > 0){
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
                                verificaSePresente(Id_Film, Recensione, Punteggio, NomeUtente);
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
                        verificaSePresente(Id_Film, Recensione, Punteggio, NomeUtente);
                        onBackPressed();
                    }
                }
            }
        });
        Annulla.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento Recensione Annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento Recensione Annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void verificaSePresente(Integer id_film, String recensione, float punteggio, String nomeUtente) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Id_Film_Recensito");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 0) {
                        Inserisci_Recensione(Id_Film, recensione, punteggio, NomeUtente);
                    }else{
                        Toast.makeText(ScriviRecensioneActivity.this , "Ha Già Inserito La Recensione Per Questo Film", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(ScriviRecensioneActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScriviRecensioneActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Corpo_Recensione", recensione);
                params.put("Valutazione",String.valueOf(punteggio));
                params.put("Id_Film_Recensito", String.valueOf(id_film));
                params.put("User_Recensore", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Inserisci_Recensione(Integer id_film, String recensione, float punteggio, String nomeUtente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(ScriviRecensioneActivity.this , "Recensione Aggiunta" , Toast.LENGTH_SHORT).show();
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
                params.put("Id_Film_Recensito", String.valueOf(id_film));
                params.put("User_Recensore", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}