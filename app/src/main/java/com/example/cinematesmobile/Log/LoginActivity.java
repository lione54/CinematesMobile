package com.example.cinematesmobile.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaUsername;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText Email, Passwd;
    private TextInputLayout InsEmail, InsPasswd;
    private AppCompatButton Login, AccediConFB, AccediConGoogle;
    private AppCompatTextView PasswdDimenticata;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Paper.init(this);
        Email = findViewById(R.id.username_or_email);
        Passwd = findViewById(R.id.password_insert_login);
        InsEmail = findViewById(R.id.username_or_email_layout);
        InsPasswd = findViewById(R.id.password_insert_login_layout);
        Login = findViewById(R.id.login_seample_button);
        AccediConFB = findViewById(R.id.loginfacebook_button);
        AccediConGoogle = findViewById(R.id.logingoogle_button);
        PasswdDimenticata = findViewById(R.id.PasswdDimenticata);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(Email.getText().length() > 0 && Passwd.getText().length() > 0){
                    Call<DBModelVerifica> verificaEmailCall = retrofitServiceDBInterno.VerificaEmailInserita(Email.getText().toString());
                    verificaEmailCall.enqueue(new Callback<DBModelVerifica>() {
                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call, @NonNull Response<DBModelVerifica> response) {
                            DBModelVerifica dbModelVerifica = response.body();
                            if(dbModelVerifica != null){
                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                if (verificaResults.get(0).getCodVerifica() == 1){
                                    Call<DBModelVerifica> verificaPasswdCall = retrofitServiceDBInterno.VerificaPasswdInserita(Email.getText().toString(), Passwd.getText().toString());
                                    verificaPasswdCall.enqueue(new Callback<DBModelVerifica>() {
                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                            DBModelVerifica dbModelVerifica = response.body();
                                            if(dbModelVerifica != null) {
                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                if (verificaResults.get(0).getCodVerifica() == 1) {
                                                    if(Email.getText().toString().matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}")){
                                                        Call<DBModelRecuperaUsername> recuperaUsernameCall = retrofitServiceDBInterno.RecuperaUsername(Email.getText().toString(), Passwd.getText().toString());
                                                        recuperaUsernameCall.enqueue(new Callback<DBModelRecuperaUsername>() {
                                                            @Override public void onResponse(@NonNull Call<DBModelRecuperaUsername> call,@NonNull Response<DBModelRecuperaUsername> response) {
                                                                DBModelRecuperaUsername dbModelRecuperaUsername = response.body();
                                                                if (dbModelRecuperaUsername != null){
                                                                    if(!(dbModelRecuperaUsername.getUsername().equals("Error"))) {
                                                                        Paper.book().write("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                                        Paper.book().write("Passwd", Passwd.getText().toString());
                                                                        Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                                                        intent.putExtra("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                                        startActivity(intent);
                                                                    }else{
                                                                        Toast.makeText(LoginActivity.this, "Errore nel recupero username.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }else{
                                                                    Toast.makeText(LoginActivity.this, "Impossibile recuperare username.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            @Override public void onFailure(@NonNull Call<DBModelRecuperaUsername> call,@NonNull Throwable t) {
                                                                Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else{
                                                        Paper.book().write("UserProprietario", Email.getText().toString());
                                                        Paper.book().write("Passwd", Passwd.getText().toString());
                                                        Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                                        intent.putExtra("UserProprietario", Email.getText().toString());
                                                        startActivity(intent);
                                                    }
                                                } else {
                                                    InsPasswd.setError("Inserisci password valida");
                                                }
                                            }else{
                                                Toast.makeText(LoginActivity.this, "Impossibile verificare passwd.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                            Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else{
                                    InsEmail.setError("Inserisci una email valida");
                                }
                            }else{
                                Toast.makeText(LoginActivity.this, "Impossibile verificare email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    if(Email.getText().length() == 0){
                        InsEmail.setError("Inserisci email o username");
                    }else if(Passwd.getText().length() == 0){
                        InsPasswd.setError("Inserisci password");
                    }
                }
            }
        });
        AccediConFB.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "TBA.", Toast.LENGTH_SHORT).show();
            }
        });
        AccediConGoogle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "TBA.", Toast.LENGTH_SHORT).show();
            }
        });
        PasswdDimenticata.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "TBA.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}