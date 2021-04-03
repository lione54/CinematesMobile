package com.example.cinematesmobile.Frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.cinematesmobile.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    private Bundle bundle = new Bundle();
    private String EmailProprietario = "mattia.golino@gmail.com";
    private String UsernameProprietario = "lione54";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                Fragment fragment;
                switch (menuitem.getItemId()) {
                    case R.id.Profilo:
                        setTitle("Profilo");
                        fragment = new ProfiloFragment();
                        bundle.putString("Email", EmailProprietario);
                        bundle.putString("Username", UsernameProprietario);
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                        break;
                    case R.id.home_:
                        setTitle("Home");
                        fragment = new HomeFragment();
                        bundle.putString("Email", EmailProprietario);
                        bundle.putString("Username", UsernameProprietario);
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                        break;
                    case R.id.toprated_:
                        setTitle("TopRated");
                        fragment = new FragmentPreferiti();
                        bundle.putString("Email", EmailProprietario);
                        bundle.putString("Username", UsernameProprietario);
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                        break;
                    case R.id.cerca_:
                        setTitle("Ricerca");
                        fragment = new SearchFragment();
                        bundle.putString("Email", EmailProprietario);
                        bundle.putString("Username", UsernameProprietario);
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                        break;
                    case R.id.notifiche_:
                        setTitle("Notifiche");
                        fragment = new FragmentNotifiche();
                        bundle.putString("Email", EmailProprietario);
                        bundle.putString("Username", UsernameProprietario);
                        fragment.setArguments(bundle);
                        loadFragment(fragment);
                        break;
                }
                return false;
            }
        });
    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.my_nav_host_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
