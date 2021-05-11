package com.example.cinematesmobile.Main;

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

import com.example.cinematesmobile.Log.ui.login.LoginActivity;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.SignIn.SignInActivity;

import java.util.Timer;
import java.util.TimerTask;

import static android.view.animation.AnimationUtils.loadAnimation;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logo;
    private Button Accedi;
    private Button Registrati;
    private TextView SubTitle1;
    private TextView SubTitle2;

    private Animation animationStart(Animation.AnimationListener animationListener) {
        Animation anim = AnimationUtils.loadAnimation(WelcomeActivity.this, R.anim.on_click_animation);
        anim.setAnimationListener(animationListener);
        return anim;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logo = findViewById(R.id.logo_cinemates);
        logo.startAnimation(loadAnimation(WelcomeActivity.this, R.anim.entry_animation));

        final Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setContentView(R.layout.activity_welcome_continua);
                Accedi = findViewById(R.id.accedi);
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

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animationStart(animationListener));
            }
        });
    }

}