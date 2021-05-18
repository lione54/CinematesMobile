package com.example.cinematesmobile.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.Calendar;
import java.util.List;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFieldsActivity extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private AppCompatButton Finito;
    private TextInputLayout InsNome, InsCognome, InsPasswd, InsRipetiPasswd, InsUsername, InsBday;
    private TextInputEditText Nome, Cognome, Passwd, RipetiPasswd, Username;
    private RadioGroup SelezioneSesso;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private String EmailProprietario, CodVerifica;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerfields);
        InsNome = findViewById(R.id.InsertNome_layout);
        InsCognome = findViewById(R.id.InsertCognome_layout);
        InsPasswd = findViewById(R.id.InsertPassword_layout);
        InsRipetiPasswd = findViewById(R.id.RepeatPassword_layout);
        InsUsername = findViewById(R.id.InsertUsername_layout);
        Nome = findViewById(R.id.InsertNome);
        Cognome = findViewById(R.id.InsertCognome);
        Passwd = findViewById(R.id.InsertPassword);
        SelezioneSesso = findViewById(R.id.Gender);
        RipetiPasswd = findViewById(R.id.RepeatPassword);
        Username = findViewById(R.id.InsertUsername);
        InsBday = findViewById(R.id.InsertBday);
        EmailProprietario = getIntent().getExtras().getString("EmailProprietario");
        CodVerifica = getIntent().getExtras().getString("CodeVerifica");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Finito = findViewById(R.id.Confirmation);
        // initiate the date picker and a button
        date = (EditText) findViewById(R.id.Bday);
        // perform click event on edit text
        date.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(RegisterFieldsActivity.this, R.style.datepicker,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Finito.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int camposelezionato = SelezioneSesso.getCheckedRadioButtonId();
                final String[] Nuovosesso = {null};
                if(Nome.getText().length() > 0 && Cognome.getText().length() > 0 && Passwd.getText().length() > 0 && RipetiPasswd.getText().length() > 0 && Username.getText().length() > 0 && date.getText().length() > 0){
                    if(Passwd.length() != RipetiPasswd.length()){
                        InsRipetiPasswd.setError("La lunghezza della nuova password non corrisponde alla precedente");
                    }else if(!(Passwd.getText().toString().equals(RipetiPasswd.getText().toString()))){
                        InsRipetiPasswd.setError("Le password non corrispondono");
                    }else {
                        if(camposelezionato == -1){
                            Toast.makeText(RegisterFieldsActivity.this, "Seleziona un campo di ricerca", Toast.LENGTH_SHORT).show();
                        }else{
                            Nuovosesso[0] = CambiaSesso(camposelezionato);
                            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaUsername(EmailProprietario);
                            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                    DBModelVerifica dbModelVerifica = response.body();
                                    if(dbModelVerifica != null) {
                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                        if (verificaResults.get(0).getCodVerifica() == 0) {
                                            Call<DBModelResponseToInsert> insertCall = retrofitServiceDBInterno.InserisciNuovoUtente(EmailProprietario, CodVerifica, Nome.getText().toString(), Cognome.getText().toString(), RipetiPasswd.getText().toString(), date.getText().toString(), Nuovosesso[0], Username.getText().toString());
                                            insertCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if(dbModelResponseToInsert != null) {
                                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                            Intent intent = new Intent(RegisterFieldsActivity.this, RegisterFieldsActivitySuccess.class);
                                                            intent.putExtra("UserProprietario", Username.getText().toString());
                                                            intent.putExtra("Passwd", RipetiPasswd.getText().toString());
                                                            startActivity(intent);
                                                        }else {
                                                            Toast.makeText(RegisterFieldsActivity.this, "Aggiunta nuovo utente fallita.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else{
                                                        Toast.makeText(RegisterFieldsActivity.this, "Impossibile aggiungere nuovo utente.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(RegisterFieldsActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            InsUsername.setError("Username già in uso");
                                        }
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                    Toast.makeText(RegisterFieldsActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }else{
                    if(Nome.getText().length() > 0){
                        InsNome.setError("Scrivi qui il tuo nome");
                    }else if(Cognome.getText().length() > 0){
                        InsCognome.setError("Scrivi qui il tuo cognome");
                    }else if(Passwd.getText().length() > 0){
                        InsPasswd.setError("Scrivi qui la tua password");
                    }else if(RipetiPasswd.getText().length() > 0){
                        InsRipetiPasswd.setError("Ripeti qui la tua password");
                    }else if(Username.getText().length() > 0){
                        InsUsername.setError("Scrivi qui il tuo username");
                    }else if(date.getText().length() > 0){
                        InsBday.setError("Scrivi qui la tua data di nascita");
                    }
                }
            }
        });
    }

    private String CambiaSesso(int camposelezionato) {
        String Sesso = null;
        switch (camposelezionato){
            case R.id.femmina:
                Sesso = "F";
                break;
            case R.id.maschio:
                Sesso = "M";
                break;
            case R.id.altro:
                Sesso = "O";
                break;
        }
        return Sesso;
    }
}
