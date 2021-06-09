package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.cinematesmobile.Frag.Adapter.MieiAmiciAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelUserAmici;
import com.example.cinematesmobile.ModelDBInterno.DBModelUserAmiciResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisualizzaAmiciInComuneActivity extends AppCompatActivity {

    private String UsernameCercato, Proprietario;
    private RecyclerView ListaAmici;
    private AppCompatImageButton PreviouslyAmici;
    private MieiAmiciAdapter mieiAmiciAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_amici_in_comune);
        UsernameCercato = getIntent().getExtras().getString("Nome_Utente_Cercato");
        Proprietario = getIntent().getExtras().getString("Nome_Proprietario");
        PreviouslyAmici = findViewById(R.id.previously_amici);
        ListaAmici = findViewById(R.id.lista_amici_in_comune);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.HORIZONTAL, false));
        Call<DBModelUserAmiciResponce> dbModelUserAmiciResponceCall = retrofitServiceDBInterno.getAmiciInComune(Proprietario, UsernameCercato);
        dbModelUserAmiciResponceCall.enqueue(new Callback<DBModelUserAmiciResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelUserAmiciResponce> call,@NonNull Response<DBModelUserAmiciResponce> response) {
                DBModelUserAmiciResponce dbModelUserAmiciResponce = response.body();
                if(dbModelUserAmiciResponce != null){
                    List<DBModelUserAmici> UtentiAmici = dbModelUserAmiciResponce.getResults();
                    if(!(UtentiAmici.isEmpty())) {
                        ListaAmici.setLayoutManager(new LinearLayoutManager(VisualizzaAmiciInComuneActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieiAmiciAdapter = new MieiAmiciAdapter(VisualizzaAmiciInComuneActivity.this, UtentiAmici, UsernameCercato, Proprietario);
                        ListaAmici.setAdapter(mieiAmiciAdapter);
                    }
                }else{
                    Toast.makeText(VisualizzaAmiciInComuneActivity.this, "Non sono presenti amici",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelUserAmiciResponce> call,@NonNull Throwable t) {
                Toast.makeText(VisualizzaAmiciInComuneActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        PreviouslyAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}