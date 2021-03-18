package com.example.cinematesmobile.Frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cinematesmobile.R;


public class Profilo extends Fragment{

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
          View v = inflater.inflate(R.layout.fragment_profilo, container, false);
         //Button recensione = v.findViewById(R.id.button5);
        /*recensione.setOnClickListener(new View.OnClickListener() {
             @Override public void onClick(View v) {
                 RecensioneFragment recensioneFragment = new RecensioneFragment();
                 FragmentTransaction transaction = getFragmentManager().beginTransaction();
                 transaction.replace(R.id.my_nav_host_fragment,recensioneFragment);
                 transaction.addToBackStack(null);
                 transaction.commit();
             }
         });*/

     return v;
    }
}