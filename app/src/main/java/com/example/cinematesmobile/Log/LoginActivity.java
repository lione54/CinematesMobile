package com.example.cinematesmobile.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Frag.ProfiloFragment;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaUsername;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText Email, Passwd, InsertEmailPassDim, CodiceVer, NuovaPasswd, ConfermaNuovaPass;
    private TextInputLayout InsEmail, InsPasswd, InsertEmailPassDimLayout, NuovoCodiceVer, NuovaPasswdLayout, ConfermaNuovaPassLayout;
    private AppCompatButton Login, AccediConFB, AccediConGoogle, Conferma, Annulla;
    private AppCompatTextView PasswdDimenticata;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private AlertDialog.Builder InsEmailVerifica, InsCodVer, dialogBilderPass;
    private AlertDialog EmailVerify,CodVerify,CambiaPassword;

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
                                    InsEmail.setError("Inserisci una email/username valida");
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
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                InsEmailVerifica = new AlertDialog.Builder(LoginActivity.this);
                final View PopUpViewEmail = getLayoutInflater().inflate(R.layout.verifica_email_popup, null);
                Conferma = (AppCompatButton) PopUpViewEmail.findViewById(R.id.conferma_button_email_passDimn);
                Annulla = (AppCompatButton) PopUpViewEmail.findViewById(R.id.annulla_button_email_passDimn);
                InsertEmailPassDim =  PopUpViewEmail.findViewById(R.id.Insertemail_passdim);
                InsertEmailPassDimLayout =  PopUpViewEmail.findViewById(R.id.Insertemail_passdim_layout);
                InsEmailVerifica.setView(PopUpViewEmail);
                EmailVerify = InsEmailVerifica.create();
                EmailVerify.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(InsertEmailPassDim.length() != 0 && InsertEmailPassDim.getText().toString().matches("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,4}")){
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
                                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(InsertEmailPassDim.getText().toString()));
                                message.setSubject("Codice Per Verifica Identità.");
                                message.setText("Salve " + InsertEmailPassDim.getText().toString() + ",\nutilizza il seguente codice per verificare la tua identità e cambiare password.\nCodice:" + CodVer + ".\nCordiali Saluti,\nIl Team di Cinemates.");
                                Transport.send(message);
                                Call<DBModelResponseToInsert> codverifcaCall = retrofitServiceDBInterno.InsertCodVerifica("null",InsertEmailPassDim.getText().toString(), CodVer, "Nuovo");
                                codverifcaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                        if(dbModelResponseToInsert != null) {
                                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                EmailVerify.dismiss();
                                                InsCodVer = new AlertDialog.Builder(LoginActivity.this);
                                                final View PopUpView = getLayoutInflater().inflate(R.layout.popup_codiceverifica_passwddim, null);
                                                Conferma = PopUpView.findViewById(R.id.ok_button);
                                                CodiceVer = PopUpView.findViewById(R.id.editTextNumberpasswddim);
                                                NuovoCodiceVer = PopUpView.findViewById(R.id.editTextNumberpasswddim_layout);
                                                InsCodVer.setView(PopUpView);
                                                CodVerify = InsCodVer.create();
                                                CodVerify.show();
                                                Conferma.setOnClickListener(new View.OnClickListener() {
                                                    @Override public void onClick(View v) {
                                                        if(CodiceVer.getText().length() > 0){
                                                            Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaCodiceVerifica(InsertEmailPassDim.getText().toString(), CodiceVer.getText().toString());
                                                            verificaCall.enqueue(new Callback<DBModelVerifica>() {
                                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                    DBModelVerifica dbModelVerifica = response.body();
                                                                    if(dbModelVerifica != null) {
                                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                        if (verificaResults.get(0).getCodVerifica() == 1) {
                                                                            CodVerify.dismiss();
                                                                            dialogBilderPass = new AlertDialog.Builder(LoginActivity.this);
                                                                            final View PopUpView = getLayoutInflater().inflate(R.layout.activity_passforgottenform, null);
                                                                            Conferma = PopUpView.findViewById(R.id.confermanuovapassdim);
                                                                            NuovaPasswd = PopUpView.findViewById(R.id.confermapasswordNuova);
                                                                            NuovaPasswdLayout = PopUpView.findViewById(R.id.confermapasswordNuova_layout);
                                                                            ConfermaNuovaPass = PopUpView.findViewById(R.id.riconfermanuovapassword);
                                                                            ConfermaNuovaPassLayout= PopUpView.findViewById(R.id.riconfermanuovapassword_layout);
                                                                            dialogBilderPass.setView(PopUpView);
                                                                            CambiaPassword = dialogBilderPass.create();
                                                                            CambiaPassword.show();
                                                                            Conferma.setOnClickListener(new View.OnClickListener() {
                                                                                @Override public void onClick(View v) {
                                                                                    if(NuovaPasswd.length() == 0){
                                                                                        NuovaPasswdLayout.setError("Inserisci nuova password");
                                                                                    }else if(ConfermaNuovaPass.length() == 0){
                                                                                        ConfermaNuovaPassLayout.setError("Inserisci nuova password");
                                                                                    }else if(NuovaPasswd.length() != ConfermaNuovaPass.length()){
                                                                                        ConfermaNuovaPassLayout.setError("La lunghezza della nuova password non corrisponde");
                                                                                    }else if(!(NuovaPasswd.getText().toString().equals(ConfermaNuovaPass.getText().toString()))){
                                                                                        ConfermaNuovaPassLayout.setError("Le password non corrispondono");
                                                                                    }else {
                                                                                        String Tipo = "Password";
                                                                                        Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(InsertEmailPassDim.getText().toString(), Tipo, ConfermaNuovaPass.getText().toString());
                                                                                        cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                                                            @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                                                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                                                                if(dbModelResponseToInsert != null) {
                                                                                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                                                                        Toast.makeText(LoginActivity.this, "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                                                                                        CambiaPassword.dismiss();
                                                                                                    }else{
                                                                                                        Toast.makeText(LoginActivity.this, "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                                                                                    }
                                                                                                }else {
                                                                                                    Toast.makeText(LoginActivity.this, "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                                                                                }
                                                                                            }
                                                                                            @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                                                                Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }
                                                                            });
                                                                        }else{
                                                                            NuovoCodiceVer.setError("Inserisci il codice corretto");
                                                                        }
                                                                    }else{
                                                                        Toast.makeText(LoginActivity.this, "Impossibile verificare codice.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                    Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }else{
                                                            NuovoCodiceVer.setError("Inserisci codice verifica");
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                        Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }catch (MessagingException e){
                                Toast.makeText(LoginActivity.this, "Impossibile inviare email.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            if(InsertEmailPassDim.length() == 0){
                                InsertEmailPassDimLayout.setError("Inserisci email");
                            }else{
                                InsertEmailPassDimLayout.setError("Inserisci una email valida");
                            }
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(LoginActivity.this, "Procedura di password dimenticata annullata.", Toast.LENGTH_SHORT).show();
                        EmailVerify.dismiss();
                    }
                });
            }
        });
    }
    public int generateRandom() {
        Random r = new Random( System.currentTimeMillis() );
        return 10000 + r.nextInt(99999);
    }
}