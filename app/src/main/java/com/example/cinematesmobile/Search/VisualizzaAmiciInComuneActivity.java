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
import com.example.cinematesmobile.ModelDBInterno.DBModelUserAmiciResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizzaAmiciInComuneActivity extends AppCompatActivity {

    private String Username, Proprietario;
    private RecyclerView ListaAmici;
    private AppCompatImageButton PreviouslyAmici;
    private List<DBModelUserAmici> UtentiAmici = new ArrayList<>();
    private MieiAmiciAdapter mieiAmiciAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_amici_in_comune);
        Username = getIntent().getExtras().getString("Nome_Utente");
        Proprietario = getIntent().getExtras().getString("Nome_Proprietario");
        PreviouslyAmici = findViewById(R.id.previously_amici);
        ListaAmici = findViewById(R.id.lista_amici_in_comune);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<DBModelUserAmiciResponce> dbModelUserAmiciResponceCall = retrofitServiceDBInterno.getAmiciInComune(Username);
        dbModelUserAmiciResponceCall.enqueue(new Callback<DBModelUserAmiciResponce>() {
            @Override public void onResponse(Call<DBModelUserAmiciResponce> call, Response<DBModelUserAmiciResponce> response) {
                DBModelUserAmiciResponce dbModelUserAmiciResponce = response.body();
                if(dbModelUserAmiciResponce != null){
                    UtentiAmici = dbModelUserAmiciResponce.getResults();
                    ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.VERTICAL, false));
                    mieiAmiciAdapter = new MieiAmiciAdapter(VisualizzaAmiciInComuneActivity.this, UtentiAmici, Username, Proprietario);
                    ListaAmici.setAdapter(mieiAmiciAdapter);
                }else{
                    Toast.makeText(VisualizzaAmiciInComuneActivity.this, "Non sono presenti amici.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<DBModelUserAmiciResponce> call, Throwable t) {
                Toast.makeText(VisualizzaAmiciInComuneActivity.this, "Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        PreviouslyAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}