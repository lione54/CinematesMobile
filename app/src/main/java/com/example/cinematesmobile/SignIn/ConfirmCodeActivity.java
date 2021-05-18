package com.example.cinematesmobile.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmCodeActivity extends AppCompatActivity {

    private AppCompatButton verificaPIN;
    private TextInputEditText Pin;
    private TextInputLayout Pinlayout;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private String Email;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmcode);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Email = getIntent().getExtras().getString("EmailProprietario");
        verificaPIN = findViewById(R.id.btnverifyPIN);
        Pin = findViewById(R.id.editTextNumber);
        Pinlayout = findViewById(R.id.editTextNumber_layout);
        verificaPIN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(Pin.getText().length() > 0) {
                    Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaCodiceVerifica(Email, Pin.getText().toString());
                    verificaCall.enqueue(new Callback<DBModelVerifica>() {
                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                            DBModelVerifica dbModelVerifica = response.body();
                            if(dbModelVerifica != null) {
                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                if (verificaResults.get(0).getCodVerifica() == 1) {
                                    Intent intent = new Intent(ConfirmCodeActivity.this, RegisterFieldsActivity.class);
                                    intent.putExtra("EmailProprietario", Email);
                                    intent.putExtra("CodeVerifica", Pin.getText().toString());
                                    startActivity(intent);
                                }else{
                                    Pinlayout.setError("Inserisci il codice corretto");
                                }
                            }else{
                                Toast.makeText(ConfirmCodeActivity.this, "Impossibile verificare codice.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                            Toast.makeText(ConfirmCodeActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Pinlayout.setError("Scrivi qui il codice di verifica");
                }
            }
        });
    }
}