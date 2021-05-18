package com.example.cinematesmobile.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SignInActivity extends AppCompatActivity {

    private AppCompatButton Continua, Indietro, Ok;
    private TextInputEditText Email;
    private TextInputLayout InsEmail;
    private AlertDialog.Builder CodeVerifica;
    private AlertDialog CodiceVerifica;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Continua = findViewById(R.id.continua);
        Indietro = findViewById(R.id.indietro);
        Email = findViewById(R.id.InserMail);
        InsEmail = findViewById(R.id.InserMail_layout);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Continua.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(Email.getText().length() > 0) {
                    if(Email.getText().toString().matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}")){
                        int Cod = generateRandom();
                        String CodVer = String.valueOf(Cod);
                        Properties properties = new Properties();
                        properties.put("mail.smtp.auth","true");
                        properties.put("mail.smtp.starttls.enable","true");
                        properties.put("mail.smtp.host","smtp.gmail.com");
                        properties.put("mail.smtp.port","587");
                        Session session = Session.getInstance(properties, new javax.mail.Authenticator(){
                            @Override protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(BuildConfig.Username, BuildConfig.Password);
                            }
                        });
                        try {
                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(BuildConfig.Username));
                            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email.getText().toString()));
                            message.setSubject("Codice Per Verifica Email.");
                            message.setText("Benvenuto nuovo utente,\nutilizza il seguente codice per proseguire nella registrazione in Cinemates.\nCodice:" + CodVer + ".\nCordiali Saluti,\nIl Team di Cinemates.");
                            Transport.send(message);
                            Call<DBModelResponseToInsert> codverifcaCall = retrofitServiceDBInterno.InsertCodVerifica("null" ,Email.getText().toString(), CodVer, "Nuovo");
                            codverifcaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                            CodeVerifica = new AlertDialog.Builder(SignInActivity.this);
                                            final View PopUpView = getLayoutInflater().inflate(R.layout.popup_codiceverifica, null);
                                            Ok = PopUpView.findViewById(R.id.ok_button);
                                            CodeVerifica.setView(PopUpView);
                                            CodiceVerifica = CodeVerifica.create();
                                            CodiceVerifica.show();
                                            Ok.setOnClickListener(new View.OnClickListener() {
                                                @Override public void onClick(View v) {
                                                    Intent intent = new Intent(SignInActivity.this, ConfirmCodeActivity.class);
                                                    intent.putExtra("EmailProprietario", Email.getText().toString());
                                                    startActivity(intent);
                                                }
                                            });
                                        }else {
                                            Toast.makeText(SignInActivity.this, "Inserimento codice verifica fallito.", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(SignInActivity.this, "Impossibile inserire codice verifica.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(SignInActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (MessagingException e){
                            Toast.makeText(SignInActivity.this, "Impossibile inviare email.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        InsEmail.setError("Inserisci una email valida");
                    }
                }else{
                    InsEmail.setError("Scrivi la tua email");
                }
            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Indietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public int generateRandom() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(99999);
    }
}