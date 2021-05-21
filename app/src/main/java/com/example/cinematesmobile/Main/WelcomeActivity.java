package com.example.cinematesmobile.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Log.LoginActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.SignIn.SignInActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.animation.AnimationUtils.loadAnimation;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logo;
    private AppCompatButton Accedi, Registrati;
    private AppCompatTextView SubTitle1, SubTitle2;
    private String User, Pass;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    private Animation animationStart(Animation.AnimationListener animationListener) {
        Animation anim = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.on_click_animation);
        anim.setAnimationListener(animationListener);
        return anim;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logo = findViewById(R.id.logo_cinemates);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Paper.init(this);
        //User = Paper.book().read("UserProprietario");
        //Pass = Paper.book().read("Passwd");
        logo.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));
        final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }
            @Override public void onAnimationEnd(Animation animation) {
                    setContentView(R.layout.activity_welcome_continua);
                    Accedi = findViewById(R.id.perchè_ti_sovrapponi_all_altro_button);
                    Registrati = findViewById(R.id.registrati);
                    SubTitle1 = findViewById(R.id.desc1);
                    SubTitle2 = findViewById(R.id.desc2);
                    SubTitle1.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));
                    SubTitle2.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));
                    Accedi.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));
                    Registrati.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));
                    Accedi.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    Registrati.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent2 = new Intent(WelcomeActivity.this, SignInActivity.class);
                            startActivity(intent2);
                        }
                    });
                }
                @Override public void onAnimationRepeat(Animation animation) {

                }
            };
            logo.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if(User == null && Pass == null) {
                        v.startAnimation(animationStart(animationListener));
                    }else{
                        Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPasswdInserita(User,Pass);
                        verificaCall.enqueue(new Callback<DBModelVerifica>() {
                            @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                DBModelVerifica dbModelVerifica = response.body();
                                if(dbModelVerifica != null) {
                                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                    if (verificaResults.get(0).getCodVerifica() == 1) {
                                        Intent intent2 = new Intent(WelcomeActivity.this, FragmentActivity.class);
                                        intent2.putExtra("UserProprietario", User);
                                        startActivity(intent2);
                                    }
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                Toast.makeText(WelcomeActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
    }
}