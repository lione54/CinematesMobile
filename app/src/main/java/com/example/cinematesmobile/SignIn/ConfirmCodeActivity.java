package com.example.cinematesmobile.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cinematesmobile.R;

public class ConfirmCodeActivity extends AppCompatActivity {
    private Button verificaPIN;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmcode);
        verificaPIN = findViewById(R.id.btnverifyPIN);
        verificaPIN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(ConfirmCodeActivity.this, RegisterFieldsActivity.class);
                startActivity(intent);
            }
        });
    }
}