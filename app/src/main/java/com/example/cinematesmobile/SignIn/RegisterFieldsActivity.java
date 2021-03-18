package com.example.cinematesmobile.SignIn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cinematesmobile.Frag.FragmentActivity;
import com.example.cinematesmobile.Main.WelcomeActivity;
import com.example.cinematesmobile.R;

public class RegisterFieldsActivity extends AppCompatActivity {

    private EditText date;
    private DatePickerDialog datePickerDialog;
    private Button Finito;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerfields);
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
                Intent intent = new Intent(RegisterFieldsActivity.this, FragmentActivity.class);
                startActivity(intent);
            }
        });
        }
    }
