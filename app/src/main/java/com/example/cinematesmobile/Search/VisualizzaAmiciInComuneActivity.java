package com.example.cinematesmobile.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
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
import com.example.cinematesmobile.Frag.Adapter.MieiAmiciAdapter;
import com.example.cinematesmobile.Frag.ListeAmiciActivity;
import com.example.cinematesmobile.Frag.Model.DBModelUserAmici;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizzaAmiciInComuneActivity extends AppCompatActivity {

    private String Username, Proprietario;
    private RecyclerView ListaAmici;
    private AppCompatImageButton PreviouslyAmici;
    private List<DBModelUserAmici> UtentiAmici = new ArrayList<>();
    private MieiAmiciAdapter mieiAmiciAdapter;
    String Foto_Mod;
    public static final String JSON_ARRAY = "dbdata";
    private static final String URL = "http://192.168.1.9/cinematesdb/ListaAmiciInComune.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_amici_in_comune);
        Username = getIntent().getExtras().getString("Nome_Utente");
        Proprietario = getIntent().getExtras().getString("Nome_Proprietario");
        PreviouslyAmici = findViewById(R.id.previously_amici);
        ListaAmici = findViewById(R.id.lista_amici_in_comune);
        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.HORIZONTAL, false));
        PrendiAmici(Username);
        PreviouslyAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void PrendiAmici(String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String str_username = object.getString("E_Amico_Di");
                        String str_foto_profilo = object.getString("Foto_Profilo");
                        if(!(str_foto_profilo.equals("null"))) {
                            Foto_Mod = "http://192.168.1.9/cinematesdb/" + str_foto_profilo;
                        }else {
                            Foto_Mod = "null";
                        }
                        DBModelUserAmici dbModelUserAmici = new DBModelUserAmici(username, str_username, Foto_Mod);
                        UtentiAmici.add(dbModelUserAmici);
                    }
                    if(UtentiAmici.isEmpty()){
                        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieiAmiciAdapter = new MieiAmiciAdapter(VisualizzaAmiciInComuneActivity.this, UtentiAmici, username, Proprietario);
                        ListaAmici.setAdapter(mieiAmiciAdapter);
                        Toast.makeText(VisualizzaAmiciInComuneActivity.this, "Non Sono Presenti Amici.",Toast.LENGTH_SHORT).show();
                    }else {
                        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieiAmiciAdapter = new MieiAmiciAdapter(VisualizzaAmiciInComuneActivity.this, UtentiAmici, username, Proprietario);
                        ListaAmici.setAdapter(mieiAmiciAdapter);
                    }
                }catch (Exception e){
                    Toast.makeText(VisualizzaAmiciInComuneActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(VisualizzaAmiciInComuneActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario",username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}