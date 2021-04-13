package com.example.cinematesmobile.Frag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.Frag.Adapter.MieRecensioniAdapter;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Adapter.RecensioniAdapter;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProprieRecensioniActivity extends AppCompatActivity {

    private String Username;
    private RecyclerView MieRecenisoni;
    private AppCompatImageButton PreviouslyRecensioni;
    private List<DBModelRecensioni> recensioniList = new ArrayList<>();
    private MieRecensioniAdapter mieRecensioniAdapter;
    public static final String JSON_ARRAY = "dbdata";
    private static final String RECURL = "http://192.168.1.9/cinematesdb/PrendiMieRecensioniDaDB.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proprie_recensioni);
        Username = getIntent().getExtras().getString("Nome_Utente");
        PreviouslyRecensioni = findViewById(R.id.previously_recensioni);
        MieRecenisoni = findViewById(R.id.mie_recensioni);
        PrendiMieRecensioni(Username);
        PreviouslyRecensioni.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void PrendiMieRecensioni(String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id = object.getString("Id_Recensione");
                            String User = object.getString("User_Recensore");
                            String Corpo_Rece = object.getString("Testo_Recensione");
                            String str_Val = object.getString("Valutazione");
                            String Data = object.getString("Data_Pubblicazione_Recensione");
                            String Foto = object.getString("Foto_Profilo");
                            String Foto_Mod = "http://192.168.1.9/cinematesdb/"+ Foto;
                            String Titolo = object.getString("Titolo_Film_Recensito");
                            String titoloMod = Titolo.replaceAll("/", "'");
                            Integer Id_Recensione = Integer.valueOf(str_id);
                            Float Valutazione = Float.valueOf(str_Val);
                            DBModelRecensioni dbModelRecensioni = new DBModelRecensioni(Id_Recensione, Valutazione, User, Data, Corpo_Rece, titoloMod, Foto_Mod);
                            recensioniList.add(dbModelRecensioni);
                        }
                    if(recensioniList.isEmpty()){
                        MieRecenisoni.setLayoutManager(new LinearLayoutManager(ProprieRecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieRecensioniAdapter = new MieRecensioniAdapter(ProprieRecensioniActivity.this, recensioniList);
                        MieRecenisoni.setAdapter(mieRecensioniAdapter);
                        Toast.makeText(ProprieRecensioniActivity.this, "Nessun Utente Ha Recensito Questo Film.",Toast.LENGTH_SHORT).show();
                    }else{
                        MieRecenisoni.setLayoutManager(new LinearLayoutManager(ProprieRecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieRecensioniAdapter = new MieRecensioniAdapter(ProprieRecensioniActivity.this, recensioniList);
                        MieRecenisoni.setAdapter(mieRecensioniAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProprieRecensioniActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProprieRecensioniActivity.this);
        requestQueue.add(stringRequest);
    }
}