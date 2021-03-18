package com.example.cinematesmobile.Frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.SearchActivity;
import com.example.cinematesmobile.SignIn.ConfirmCodeActivity;
import com.example.cinematesmobile.SignIn.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                Fragment fragment;
                switch (menuitem.getItemId()) {
                    case R.id.profilo_:
                        setTitle("Profilo");
                        fragment = new Profilo();
                        loadFragment(fragment);
                        break;
                    case R.id.home_:
                        setTitle("Home");
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.toprated_:
                        setTitle("TopRated");
                        fragment = new Fragment_Preferiti();
                        loadFragment(fragment);
                        break;
                    case R.id.cerca_:
                        setTitle("Ricerca");
                        Intent intent = new Intent(FragmentActivity.this, SearchActivity.class);
                        startActivity(intent);

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
