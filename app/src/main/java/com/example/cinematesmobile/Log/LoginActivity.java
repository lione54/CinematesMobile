package com.example.cinematesmobile.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.esotericsoftware.minlog.Log;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaPasswdResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecuperaUsername;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.SignIn.ConfirmCodeActivity;
import com.example.cinematesmobile.SignIn.RegisterFieldsActivity;
import com.example.cinematesmobile.SignIn.SignInActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
    private AppCompatButton Login;
    private LoginButton AccediConFB;
    private AppCompatButton BottoneFasullo;
    private AppCompatButton Conferma;
    private AppCompatButton Annulla;
    private AppCompatButton AccediConGoogle;
    private AppCompatTextView PasswdDimenticata;
    private CallbackManager callbackManager;
    private String Password;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private AlertDialog.Builder InsEmailVerifica, InsCodVer, dialogBilderPass;
    private AlertDialog EmailVerify,CodVerify,CambiaPassword;
    private static final int RC_SIGN_IN = 1;

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
        AccediConFB =(LoginButton) findViewById(R.id.facebookButton);
        AccediConGoogle = findViewById(R.id.logingoogle_button);
        PasswdDimenticata = findViewById(R.id.PasswdDimenticata);
        BottoneFasullo = findViewById(R.id.loginfacebook_button);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
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
        callbackManager = CallbackManager.Factory.create();
        AccediConFB.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
            }
        };
        BottoneFasullo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                    AccediConFB.performClick();
            }
        });
        AccediConFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override public void onSuccess(LoginResult loginResult) {
                AccessToken.getCurrentAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email = object.getString("email");
                            String jname = object.getString("name");
                            String [] nome = jname.split(" ");
                            String bday = object.getString("birthday");
                            Call<DBModelVerifica> verificaEmailCall = retrofitServiceDBInterno.VerificaEmailInserita(email);
                            verificaEmailCall.enqueue(new Callback<DBModelVerifica>() {
                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call, @NonNull Response<DBModelVerifica> response) {
                                    DBModelVerifica dbModelVerifica = response.body();
                                    if (dbModelVerifica != null) {
                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                        if (verificaResults.get(0).getCodVerifica() == 1) {
                                            Call<DBModelRecuperaPasswdResponce> recuperaPasswdResponceCall = retrofitServiceDBInterno.RecuperaPasswd(email);
                                            recuperaPasswdResponceCall.enqueue(new Callback<DBModelRecuperaPasswdResponce>() {
                                                @Override public void onResponse(Call<DBModelRecuperaPasswdResponce> call, Response<DBModelRecuperaPasswdResponce> response) {
                                                    DBModelRecuperaPasswdResponce dbModelRecuperaPasswdResponce = response.body();
                                                    if (dbModelRecuperaPasswdResponce != null && (!(dbModelRecuperaPasswdResponce.getPasswd().equals("Error")))) {
                                                        Password = dbModelRecuperaPasswdResponce.getPasswd();
                                                        Call<DBModelRecuperaUsername> recuperaUsernameCall = retrofitServiceDBInterno.RecuperaUsername(email, Password);
                                                        recuperaUsernameCall.enqueue(new Callback<DBModelRecuperaUsername>() {
                                                            @Override public void onResponse(@NonNull Call<DBModelRecuperaUsername> call, @NonNull Response<DBModelRecuperaUsername> response) {
                                                                DBModelRecuperaUsername dbModelRecuperaUsername = response.body();
                                                                if (dbModelRecuperaUsername != null) {
                                                                    if (!(dbModelRecuperaUsername.getUsername().equals("Error"))) {
                                                                        Paper.book().write("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                                        Paper.book().write("Passwd", Password);
                                                                        Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                                                        intent.putExtra("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                                        startActivity(intent);
                                                                    } else {
                                                                        Toast.makeText(LoginActivity.this, "Errore nel recupero username.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(LoginActivity.this, "Impossibile recuperare username.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            @Override public void onFailure(@NonNull Call<DBModelRecuperaUsername> call, @NonNull Throwable t) {
                                                                Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                                @Override public void onFailure(Call<DBModelRecuperaPasswdResponce> call, Throwable t) {
                                                    Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            Call<DBModelResponseToInsert> codverifcaCall = retrofitServiceDBInterno.InsertCodVerifica("null" ,email, "00000", "Nuovo");
                                            codverifcaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if(dbModelResponseToInsert != null){
                                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                                            Intent intent = new Intent(LoginActivity.this, RegisterFieldsActivity.class);
                                                            intent.putExtra("EmailProprietario", email);
                                                            intent.putExtra("CodeVerifica", "00000");
                                                            intent.putExtra("TipoLog", "Facebook");
                                                            intent.putExtra("Nome", nome[0]);
                                                            intent.putExtra("Cognome", nome[1]);
                                                            intent.putExtra("bday", bday);
                                                            startActivity(intent);
                                                        }else {
                                                            Toast.makeText(LoginActivity.this, "Inserimento codice verifica fallito.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else{
                                                        Toast.makeText(LoginActivity.this, "Impossibile inserire codice verifica.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call, @NonNull Throwable t) {
                                    Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,birthday,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
            @Override public void onCancel() {
                Toast.makeText(LoginActivity.this, "Procedura di accesso con facebook annullata.", Toast.LENGTH_SHORT).show();
            }
            @Override public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "C'è stato un problema, perfavre ritenta.", Toast.LENGTH_SHORT).show();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        AccediConGoogle.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
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
                                Call<DBModelResponseToInsert> codverifcaCall = retrofitServiceDBInterno.InsertCodVerifica("null",InsertEmailPassDim.getText().toString(), CodVer, "PassDim");
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

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String Email = account.getEmail();
                Call<DBModelVerifica> verificaEmailCall = retrofitServiceDBInterno.VerificaEmailInserita(Email);
                verificaEmailCall.enqueue(new Callback<DBModelVerifica>() {
                    @Override public void onResponse(@NonNull Call<DBModelVerifica> call, @NonNull Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if (dbModelVerifica != null) {
                            List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                            if (verificaResults.get(0).getCodVerifica() == 1) {
                                Call<DBModelRecuperaPasswdResponce> recuperaPasswdResponceCall = retrofitServiceDBInterno.RecuperaPasswd(Email);
                                recuperaPasswdResponceCall.enqueue(new Callback<DBModelRecuperaPasswdResponce>() {
                                    @Override public void onResponse(Call<DBModelRecuperaPasswdResponce> call, Response<DBModelRecuperaPasswdResponce> response) {
                                        DBModelRecuperaPasswdResponce dbModelRecuperaPasswdResponce = response.body();
                                        if (dbModelRecuperaPasswdResponce != null && (!(dbModelRecuperaPasswdResponce.getPasswd().equals("Error")))) {
                                            Password = dbModelRecuperaPasswdResponce.getPasswd();
                                            Call<DBModelRecuperaUsername> recuperaUsernameCall = retrofitServiceDBInterno.RecuperaUsername(Email, Password);
                                            recuperaUsernameCall.enqueue(new Callback<DBModelRecuperaUsername>() {
                                                @Override public void onResponse(@NonNull Call<DBModelRecuperaUsername> call, @NonNull Response<DBModelRecuperaUsername> response) {
                                                    DBModelRecuperaUsername dbModelRecuperaUsername = response.body();
                                                    if (dbModelRecuperaUsername != null) {
                                                        if (!(dbModelRecuperaUsername.getUsername().equals("Error"))) {
                                                            Paper.book().write("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                            Paper.book().write("Passwd", Password);
                                                            Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                                                            intent.putExtra("UserProprietario", dbModelRecuperaUsername.getUsername());
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "Errore nel recupero username.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, "Impossibile recuperare username.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelRecuperaUsername> call, @NonNull Throwable t) {
                                                    Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                    @Override public void onFailure(Call<DBModelRecuperaPasswdResponce> call, Throwable t) {
                                        Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Call<DBModelResponseToInsert> codverifcaCall = retrofitServiceDBInterno.InsertCodVerifica("null" ,Email, "00000", "Nuovo");
                                codverifcaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                        if(dbModelResponseToInsert != null){
                                            if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                                Intent intent = new Intent(LoginActivity.this, RegisterFieldsActivity.class);
                                                intent.putExtra("EmailProprietario", Email);
                                                intent.putExtra("CodeVerifica", "00000");
                                                intent.putExtra("TipoLog", "Google");
                                                startActivity(intent);
                                            }else {
                                                Toast.makeText(LoginActivity.this, "Inserimento codice verifica fallito.", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(LoginActivity.this, "Impossibile inserire codice verifica.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                        Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelVerifica> call, @NonNull Throwable t) {
                        Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    }
                });
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
        }
    }
}