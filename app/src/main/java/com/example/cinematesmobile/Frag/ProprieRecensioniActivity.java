package com.example.cinematesmobile.Frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.cinematesmobile.Frag.Adapter.MieRecensioniAdapter;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProprieRecensioniActivity extends AppCompatActivity {

    private String Username;
    private RecyclerView MieRecenisoni;
    private AppCompatImageButton PreviouslyRecensioni;
    private List<DBModelRecensioni> recensioniList = new ArrayList<>();
    private MieRecensioniAdapter mieRecensioniAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proprie_recensioni);
        Username = getIntent().getExtras().getString("Nome_Utente");
        PreviouslyRecensioni = findViewById(R.id.previously_recensioni);
        MieRecenisoni = findViewById(R.id.mie_recensioni);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelRecensioniResponce> mieRecensioniCall = retrofitServiceDBInterno.PrendiMieRecensioni(Username);
        mieRecensioniCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                if (dbModelRecensioniResponce != null) {
                    recensioniList = dbModelRecensioniResponce.getResults();
                    if(!(recensioniList.isEmpty())){
                        MieRecenisoni.setLayoutManager(new LinearLayoutManager(ProprieRecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        mieRecensioniAdapter = new MieRecensioniAdapter(ProprieRecensioniActivity.this, recensioniList);
                        MieRecenisoni.setAdapter(mieRecensioniAdapter);
                    }else{
                        Toast.makeText(ProprieRecensioniActivity.this, "Nessuna recensione da mostrare",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ProprieRecensioniActivity.this, "Impossibile caricare le recensioni",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {
                Toast.makeText(ProprieRecensioniActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
            }
        });
        PreviouslyRecensioni.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}