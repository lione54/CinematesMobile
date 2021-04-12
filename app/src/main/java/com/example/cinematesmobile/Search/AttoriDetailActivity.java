package com.example.cinematesmobile.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.ImmagineProfiloAttoriAdapter;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.AttoriDetails;
import com.example.cinematesmobile.Search.Model.AttoriImage;
import com.example.cinematesmobile.Search.Model.AttoriImmageResult;
import com.example.cinematesmobile.Search.Model.AttoriResponse;
import com.example.cinematesmobile.Search.Model.AttoriResponseResults;
import com.example.cinematesmobile.Search.Model.AttoriResponseResultsKnownFor;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttoriDetailActivity extends AppCompatActivity {

    private KenBurnsView attoriDetailProfileImageView;
    private LinearLayoutCompat AlsoKnowAsLayout;
    private LinearLayoutCompat BirthdayLayout;
    private LinearLayoutCompat PlaceLayout;
    private LinearLayoutCompat DeathdayLayout;
    private LinearLayoutCompat RoleLayout;
    private LinearLayoutCompat HomepageLayout;
    private LinearLayoutCompat BioLayout;
    private LinearLayoutCompat ProfileImageLayout, AttoriDetailFilmfattiLayout;
    private AppCompatTextView NomeAttore;
    private AppCompatTextView AlsoKnowAs;
    private AppCompatTextView Birthday;
    private AppCompatTextView Place;
    private AppCompatTextView Deathday;
    private AppCompatTextView Role;
    private AppCompatTextView Homepage;
    private AppCompatTextView Bio, AttoriFilmfatti;
    private RecyclerView ProfileImageRecycleView;
    private ImmagineProfiloAttoriAdapter immagineProfiloAttoriAdapter;
    private RetrofitService retrofitService;
    private AppCompatImageButton previous;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attori_detail);
        Intent intent = getIntent();
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        attoriDetailProfileImageView = findViewById(R.id.attori_detail_profile_image);
        AlsoKnowAsLayout = findViewById(R.id.attori_detail_also_know_layout);
        BirthdayLayout = findViewById(R.id.attori_detail_birthday_layout);
        PlaceLayout = findViewById(R.id.attori_detail_place_layout);
        DeathdayLayout = findViewById(R.id.attori_detail_death_layout);
        RoleLayout = findViewById(R.id.attori_detail_role_layout);
        HomepageLayout = findViewById(R.id.attori_detail_hp_layout);
        BioLayout = findViewById(R.id.attori_detail_bio_layout);
        ProfileImageLayout = findViewById(R.id.attori_detail_profile_images_layout);
        AttoriDetailFilmfattiLayout = findViewById(R.id.attori_detail_filmfatti_layout);
        NomeAttore = findViewById(R.id.attori_detail_name);
        AlsoKnowAs = findViewById(R.id.attori_detail_also_know);
        Birthday = findViewById(R.id.attori_detail_birthday);
        Place = findViewById(R.id.attori_detail_place);
        Deathday = findViewById(R.id.attori_detail_death);
        Role = findViewById(R.id.attori_detail_role);
        Homepage = findViewById(R.id.attori_detail_hp);
        Bio = findViewById(R.id.attori_detail_bio);
        AttoriFilmfatti = findViewById(R.id.attori_filmfatti);
        previous = findViewById(R.id.previously);
        ProfileImageRecycleView = findViewById(R.id.attori_detail_profilo_image_recyclerView);
        ProfileImageRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        String lingua = "it-IT";
        if(intent != null && intent.getExtras() != null){
            if(intent.getExtras().getString("id") != null) {
                int id = Integer.parseInt(intent.getExtras().getString("id"));
                Call<AttoriDetails> attoriDetailsCall = retrofitService.getAttoriDetails(id, BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
                attoriDetailsCall.enqueue(new Callback<AttoriDetails>() {
                    @Override public void onResponse(@NonNull Call<AttoriDetails> call,@NonNull Response<AttoriDetails> response) {
                        AttoriDetails attoriDetailsResponse = response.body();
                        if(attoriDetailsResponse != null){
                            PreparePersonDetail(attoriDetailsResponse);
                        }else{
                            Toast.makeText(AttoriDetailActivity.this, "Dettagli Non Trovati",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override public void onFailure(@NonNull Call<AttoriDetails> call,@NonNull Throwable t) {
                        Toast.makeText(AttoriDetailActivity.this, "Ops Qualcosa è Andato Storto",Toast.LENGTH_SHORT).show();
                    }
                });
                Call<AttoriImage> attoriImageCall = retrofitService.getAttoriImage(id, BuildConfig.THE_MOVIE_DB_APY_KEY);
                attoriImageCall.enqueue(new Callback<AttoriImage>() {
                    @Override public void onResponse(Call<AttoriImage> call, Response<AttoriImage> response) {
                        AttoriImage attoriImage = response.body();
                        if(attoriImage != null){
                            List<AttoriImmageResult> attoriImmageResultList = attoriImage.getProfiles();
                            if(attoriImmageResultList != null && attoriImmageResultList.size() > 0){
                                ProfileImageLayout.setVisibility(View.VISIBLE);
                                immagineProfiloAttoriAdapter = new ImmagineProfiloAttoriAdapter(AttoriDetailActivity.this,attoriImmageResultList);
                                ProfileImageRecycleView.setAdapter(immagineProfiloAttoriAdapter);
                                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(AttoriDetailActivity.this,R.anim.layout_scorri_destra);
                                ProfileImageRecycleView.setLayoutAnimation(controller);
                                ProfileImageRecycleView.scheduleLayoutAnimation();
                            }else{
                                ProfileImageLayout.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override public void onFailure(Call<AttoriImage> call, Throwable t) {
                        Toast.makeText(AttoriDetailActivity.this, "Ops Qualcosa è Andato Storto",Toast.LENGTH_SHORT).show();
                    }
                });
                /*AGGIUNTA NUOVA*/
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    private void PreparePersonDetail(AttoriDetails attoriDetailsResponse) {
        String lingua1 = "it-IT";
        String profilePath = attoriDetailsResponse.getProfile_path();
        String nome = attoriDetailsResponse.getName();
        String birthday = attoriDetailsResponse.getBirthday();
        String place = attoriDetailsResponse.getPlace_of_birth();
        String deathday = attoriDetailsResponse.getDeathday();
        String role = attoriDetailsResponse.getKnown_for_department();
        String homepage = attoriDetailsResponse.getHomepage();
        String bio = attoriDetailsResponse.getBiography();
        List<String> alsoknow = attoriDetailsResponse.getAlso_known_as();
        Picasso.with(this).load(profilePath).into(attoriDetailProfileImageView);
        if(nome != null){
            if(nome.length() > 0){
                NomeAttore.setText(nome);
                NomeAttore.setVisibility(View.VISIBLE);
            }else{
                NomeAttore.setVisibility(View.GONE);
            }
        }else{
            NomeAttore.setVisibility(View.GONE);
        }
        if(alsoknow != null){
            if(alsoknow.size() > 0){
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 0; i<alsoknow.size(); i++){
                    if(i == alsoknow.size() - 1){
                        stringBuilder.append(alsoknow.get(i));
                    }else{
                        stringBuilder.append(alsoknow.get(i)).append(", ");
                    }
                }
                AlsoKnowAs.setText(stringBuilder.toString());
                AlsoKnowAsLayout.setVisibility(View.VISIBLE);
            }else{
                AlsoKnowAsLayout.setVisibility(View.GONE);
            }
        }else{
            AlsoKnowAsLayout.setVisibility(View.GONE);
        }
        if(birthday != null){
            if(birthday.length() > 0){
                Birthday.setText(birthday);
                BirthdayLayout.setVisibility(View.VISIBLE);
            }else{
                BirthdayLayout.setVisibility(View.GONE);
            }
        }else{
            BirthdayLayout.setVisibility(View.GONE);
        }
        if(place != null){
            if(place.length() > 0){
                Place.setText(place);
                PlaceLayout.setVisibility(View.VISIBLE);
            }else{
                PlaceLayout.setVisibility(View.GONE);
            }
        }else{
            PlaceLayout.setVisibility(View.GONE);
        }
        if(deathday != null){
            if(deathday.length() > 0){
                Deathday.setText(deathday);
                DeathdayLayout.setVisibility(View.VISIBLE);
            }else{
                DeathdayLayout.setVisibility(View.GONE);
            }
        }else{
                DeathdayLayout.setVisibility(View.GONE);
        }
        if(role != null){
            if(role.length() > 0){
                Role.setText(role);
                RoleLayout.setVisibility(View.VISIBLE);
            }else{
                RoleLayout.setVisibility(View.GONE);
            }
        }else{
                RoleLayout.setVisibility(View.GONE);
        }
        if(homepage != null){
            if(homepage.length() > 0){
                Homepage.setText(homepage);
                HomepageLayout.setVisibility(View.VISIBLE);
            }else{
                HomepageLayout.setVisibility(View.GONE);
            }
        }else{
            HomepageLayout.setVisibility(View.GONE);
        }
        if(bio != null){
            if(bio.length() > 0){
                Bio.setText(bio);
                BioLayout.setVisibility(View.VISIBLE);
            }else{
                BioLayout.setVisibility(View.GONE);
            }
        }else{
                BioLayout.setVisibility(View.GONE);
        }
        Call<AttoriResponse> attoriResponseCall = retrofitService.getPersonByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua1, nome);
        attoriResponseCall.enqueue(new Callback<AttoriResponse>() {
            @Override public void onResponse(@NonNull Call<AttoriResponse> call,@NonNull Response<AttoriResponse> response) {
                AttoriResponse attoriResponse = response.body();
                if (attoriResponse != null) {
                    List<AttoriResponseResults> attoriResponseResults = attoriResponse.getResults();
                    PrendiFilmFatti(attoriResponseResults, attoriResponseResults.size(), attoriDetailsResponse.getId());
                }
            }

            @Override public void onFailure(@NonNull  Call<AttoriResponse> call,@NonNull Throwable t) {
                Toast.makeText(AttoriDetailActivity.this, "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PrendiFilmFatti(List<AttoriResponseResults> attoriResponseResults, int size, Integer id) {
        List<List<AttoriResponseResultsKnownFor>> knownFors = new ArrayList<>();
        for( int pointer = 0; pointer < size; pointer++){
            if(attoriResponseResults.get(pointer).getId() == id){
                knownFors.add(attoriResponseResults.get(pointer).getKnown_for());
            }
        }
        if(!(knownFors.isEmpty())){
            StringBuilder stringBuilder = new StringBuilder();
            if(knownFors.size() > 0) {
                for (int i = 0; i < knownFors.get(0).size(); i++) {
                    if(i == ((knownFors.get(0).size()) - 1)){
                        stringBuilder.append(knownFors.get(0).get(i).getTitle()).append(".");
                    }else{
                        stringBuilder.append(knownFors.get(0).get(i).getTitle()).append(", ");
                    }
                }
            }
            if (stringBuilder != null && stringBuilder.length() > 0){
                AttoriFilmfatti.setText(stringBuilder);
                AttoriDetailFilmfattiLayout.setVisibility(View.VISIBLE);
            }else{
                AttoriDetailFilmfattiLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}