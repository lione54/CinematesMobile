package com.example.cinematesmobile.Frag;

import android.Manifest.permission;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Model.DBModelProfiloUtente;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Search.VisualizzaImmaginiActivity;
import com.example.cinematesmobile.SignIn.LogOutActivitySuccess;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfiloFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfiloFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final int PICK_IMAGE_REQUEST = 234;
    private final int PERMISSION_CODE = 1001;
    private LinearLayout VaiARecensioniScritte, VaiAgliAmici, VaiAListeFilm;
    private AppCompatImageView CambiaPass, Cambia_Nome, Cambia_Cognome, Cambia_Email, Cambia_Descrizione, Cambia_DataNascita, Cambia_Gender;
    private String UsernameProprietario;
    private String EmailProprietario;
    private KenBurnsView ImmagineCopertina;
    public CircleImageView ImmagineProfilo, NuovaFoto;
    private AlertDialog.Builder dialogBilderPass, dialogBilderFotoProfilo, dialogBilderFotoCopertina, dialogBilderNome, dialogBilderCognome, dialogBilderEmail, dialogBilderConfermaEmail, dialogBilderDescrizione, dialogBilderDataNascita, dialogBilderGender;
    private AlertDialog CambiaPassword, CambiaFotoProfilo, CambiaFotoCopertina, CambiaNome, CambiaCognome, CambiaEmail, ConfermaCambiaEmail, CambiaDescrizione, CambiaDataNascita, CambiaGender;
    private Bitmap bitmap;
    private AppCompatImageButton Logout;
    private DatePickerDialog datePickerDialog;
    private AppCompatButton Conferma,Annulla, Scegli;
    private Bundle bundle = new Bundle();
    private TextInputLayout VecchiaPass, NuovaPass, ConfermaPass;
    private TextInputEditText InserisciVecchiaPass, InserisciNuovaPass, ConfermaNuovaPass, NuovoAttributo;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroListePersonalizzate, NumeroAmici;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, PasswordUser, DescrizioneUser, DataNascitaUser, SessoUser;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private RadioGroup CampiRicerca;

    public ProfiloFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfiloFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfiloFragment newInstance(String param1, String param2) {
        ProfiloFragment fragment = new ProfiloFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        UsernameProprietario = this.getArguments().getString("Username");
        EmailProprietario = this.getArguments().getString("Email");
        View v = inflater.inflate(R.layout.fragment_profilo, container, false);
        VaiARecensioniScritte = v.findViewById(R.id.vai_a_recensioni_scritte);
        VaiAgliAmici = v.findViewById(R.id.vai_agli_amici);
        VaiAListeFilm = v.findViewById(R.id.Vai_a_liste_film);
        ImmagineProfilo = v.findViewById(R.id.immagine_profilo);
        UsernameProfilo = v.findViewById(R.id.Username_Profilo);
        ImmagineCopertina = v.findViewById(R.id.foto_copertina);
        NumeroRecensioniScritte = v.findViewById(R.id.Numero_recensioni_scritte);
        NumeroListePersonalizzate = v.findViewById(R.id.Numero_liste_personalizzate);
        Cambia_Nome = v.findViewById(R.id.cambia_nome);
        Cambia_Cognome = v.findViewById(R.id.cambia_cognome);
        Cambia_Email = v.findViewById(R.id.cambia_indirizzo_email);
        Cambia_Descrizione = v.findViewById(R.id.cambia_descrizione);
        Cambia_DataNascita = v.findViewById(R.id.cambia_datanascita);
        Cambia_Gender = v.findViewById(R.id.cambia_gender);
        NumeroAmici = v.findViewById(R.id.Numero_amici);
        NomeUser = v.findViewById(R.id.Nome_user);
        CognomeUser = v.findViewById(R.id.Cognome_user);
        EmailUser = v.findViewById(R.id.Email_User);
        PasswordUser = v.findViewById(R.id.Password_user);
        DescrizioneUser = v.findViewById(R.id.Descrizione_User);
        DataNascitaUser = v.findViewById(R.id.Data_nascita_user);
        SessoUser = v.findViewById(R.id.Sesso_user);
        CambiaPass = v.findViewById(R.id.cambia_password);
        Logout = v.findViewById(R.id.LogoutButton);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelProfiloUtenteResponce> userResultsCall = retrofitServiceDBInterno.PrendiInfoUtente(UsernameProprietario);
        userResultsCall.enqueue(new Callback<DBModelProfiloUtenteResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelProfiloUtenteResponce> call,@NonNull Response<DBModelProfiloUtenteResponce> response) {
                DBModelProfiloUtenteResponce profiloUtenteResponce = response.body();
                if(profiloUtenteResponce != null){
                    List<DBModelProfiloUtente> profiloUtenteList = profiloUtenteResponce.getResults();
                    if(!(profiloUtenteList.isEmpty())) {
                        PrepareProfiloDetail(profiloUtenteList);
                    }else{
                        Toast.makeText(getContext() , "Caricamento informazioni fallito", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext() , "Impossibile caricare informazioni", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelProfiloUtenteResponce> call,@NonNull Throwable t) {
                Toast.makeText(getContext() , "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogOutActivitySuccess.class);
                startActivity(intent);
            }
        });
        return v;
    }

    private void PrepareProfiloDetail(List<DBModelProfiloUtente> profiloUtenteList) {
        if(profiloUtenteList.get(0).getFoto_Profilo() != null){
            Glide.with(getContext()).load(profiloUtenteList.get(0).getFoto_Profilo()).into(ImmagineProfilo);
        }else{
            ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }
        if(profiloUtenteList.get(0).getFoto_Copertina() != null){
            Glide.with(getContext()).load(profiloUtenteList.get(0).getFoto_Copertina()).into(ImmagineCopertina);
        }
        UsernameProfilo.setText(profiloUtenteList.get(0).getUserName());
        NumeroRecensioniScritte.setText(profiloUtenteList.get(0).getRecensioni_Scritte());
        NumeroListePersonalizzate.setText(profiloUtenteList.get(0).getNumero_Liste_Personalizzate());
        NumeroAmici.setText(profiloUtenteList.get(0).getTotale_Amici());
        NomeUser.setText(profiloUtenteList.get(0).getNome());
        CognomeUser.setText(profiloUtenteList.get(0).getCognome());
        EmailUser.setText(profiloUtenteList.get(0).getEmail());
        String Pass = profiloUtenteList.get(0).getPasswd().replaceAll("[a-zA-Z0-9]", "\\*");
        PasswordUser.setText(Pass);
        if(profiloUtenteList.get(0).getDescrizione_Profilo() != null){
            DescrizioneUser.setText(profiloUtenteList.get(0).getDescrizione_Profilo());
        }else{
            DescrizioneUser.setText("Descrizione non inserita dall'user");
        }
        DataNascitaUser.setText(profiloUtenteList.get(0).getData_Nascita());
        SessoUser.setText(profiloUtenteList.get(0).getSesso());
        VaiAListeFilm.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Fragment fragment = new FragmentPreferiti();
                bundle.putString("Email", EmailProprietario);
                bundle.putString("Username", UsernameProprietario);
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
        VaiARecensioniScritte.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), ProprieRecensioniActivity.class);
                intent2.putExtra("Nome_Utente", UsernameProprietario);
                startActivity(intent2);
            }
        });
        VaiAgliAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), ListeAmiciActivity.class);
                intent2.putExtra("Nome_Utente", UsernameProprietario);
                intent2.putExtra("Nome_Proprietario", UsernameProprietario);
                startActivity(intent2);
            }
        });
        CambiaPass.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderPass = new AlertDialog.Builder(getContext());
                final View PopUpView = getLayoutInflater().inflate(R.layout.cambia_pass_pop_up, null);
                Conferma = (AppCompatButton) PopUpView.findViewById(R.id.conferma_button);
                Annulla = (AppCompatButton) PopUpView.findViewById(R.id.annulla_button);
                InserisciVecchiaPass = PopUpView.findViewById(R.id.Inserisci_VecchiaPass);
                InserisciNuovaPass =  PopUpView.findViewById(R.id.Inserisci_NuovaPass);
                ConfermaNuovaPass =  PopUpView.findViewById(R.id.Conferma_NuovaPass);
                VecchiaPass = PopUpView.findViewById(R.id.layout_vecchia_pass);
                NuovaPass = PopUpView.findViewById(R.id.layout_nuova_pass);
                ConfermaPass = PopUpView.findViewById(R.id.layout_conferma_pass);
                dialogBilderPass.setView(PopUpView);
                CambiaPassword = dialogBilderPass.create();
                CambiaPassword.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(InserisciVecchiaPass.length() == 0 || !(InserisciVecchiaPass.getText().toString().equals(profiloUtenteList.get(0).getPasswd()))){
                            if(InserisciVecchiaPass.length() == 0){
                                VecchiaPass.setError("Inserisci vecchia password");
                            }else{
                                VecchiaPass.setError("La vecchia password non corrisponde");
                            }
                        }else{
                            if(InserisciNuovaPass.length() == 0){
                                NuovaPass.setError("Inserisci nuova password");
                            }else if(ConfermaNuovaPass.length() == 0){
                                ConfermaPass.setError("Inserisci nuova password");
                            }else if(InserisciNuovaPass.length() != ConfermaNuovaPass.length()){
                                ConfermaPass.setError("La lunghezza della nuova password non corrisponde");
                            }else if(!(InserisciNuovaPass.getText().toString().equals(ConfermaNuovaPass.getText().toString()))){
                                ConfermaPass.setError("Le password non corrispondono");
                            }else {
                                String Tipo = "Password";
                                Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, InserisciNuovaPass.getText().toString());
                                cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                    @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                        DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                        if(dbModelResponseToInsert != null) {
                                            if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                                Fragment fragment = new ProfiloFragment();
                                                bundle.putString("Email", EmailProprietario);
                                                bundle.putString("Username", UsernameProprietario);
                                                fragment.setArguments(bundle);
                                                loadFragment(fragment);
                                                CambiaPassword.dismiss();
                                            }else{
                                                Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                            }
                                        }else {
                                            Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                        Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        CambiaPassword.dismiss();
                        Toast.makeText(getContext() , "Cambiamento password annullato", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ImmagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(getActivity(), VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", profiloUtenteList.get(0).getFoto_Profilo());
                startActivity(visualizzaImmagineintent);
            }
        });
        ImmagineCopertina.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(getActivity(), VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", profiloUtenteList.get(0).getFoto_Copertina());
                startActivity(visualizzaImmagineintent);
            }
        });
        ImmagineProfilo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                dialogBilderFotoProfilo = new AlertDialog.Builder(getContext());
                final View PopUpViewFoto = getLayoutInflater().inflate(R.layout.cambia_foto_profilo, null);
                Conferma = (AppCompatButton) PopUpViewFoto.findViewById(R.id.conferma_button_foto);
                Annulla = (AppCompatButton) PopUpViewFoto.findViewById(R.id.annulla_button_foto);
                Scegli = (AppCompatButton) PopUpViewFoto.findViewById(R.id.scegli_foto);
                NuovaFoto = PopUpViewFoto.findViewById(R.id.nuova_foto_profilo);
                dialogBilderFotoProfilo.setView(PopUpViewFoto);
                CambiaFotoProfilo = dialogBilderFotoProfilo.create();
                CambiaFotoProfilo.show();
                Scegli.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(ContextCompat.checkSelfPermission(getContext(), permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                String [] permissions = {permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions, PERMISSION_CODE);
                            }else{
                                scegliImmagine();
                            }
                        }else{
                            scegliImmagine();
                        }
                    }
                });
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        try {
                            Call<DBModelResponseToInsert> cambiaFotoProfiloCall = retrofitServiceDBInterno.CambiaFotoProfilo(UsernameProprietario, getImgepath(bitmap));
                            cambiaFotoProfiloCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento foto profilo avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaFotoProfilo.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento foto profilo fallito" , Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "Impossibile cambiare foto profilo" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto" , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            return;
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento foto profilo annullato", Toast.LENGTH_SHORT).show();
                        CambiaFotoProfilo.dismiss();
                    }
                });
                return false;
            }
        });
        ImmagineCopertina.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                dialogBilderFotoCopertina = new AlertDialog.Builder(getContext());
                final View PopUpViewFoto = getLayoutInflater().inflate(R.layout.cambia_foto_profilo, null);
                Conferma = (AppCompatButton) PopUpViewFoto.findViewById(R.id.conferma_button_foto);
                Annulla = (AppCompatButton) PopUpViewFoto.findViewById(R.id.annulla_button_foto);
                Scegli = (AppCompatButton) PopUpViewFoto.findViewById(R.id.scegli_foto);
                NuovaFoto = PopUpViewFoto.findViewById(R.id.nuova_foto_profilo);
                dialogBilderFotoCopertina.setView(PopUpViewFoto);
                CambiaFotoCopertina = dialogBilderFotoCopertina.create();
                CambiaFotoCopertina.show();
                Scegli.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(ContextCompat.checkSelfPermission(getContext(), permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                                String [] permissions = {permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions, PERMISSION_CODE);
                            }
                        }
                        scegliImmagine();
                    }
                });
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        try {
                            Call<DBModelResponseToInsert> cambiaFotoCopertinaCall = retrofitServiceDBInterno.CambiaFotoCopertina(UsernameProprietario, getImgepath(bitmap));
                            cambiaFotoCopertinaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento foto copertina avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaFotoCopertina.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento foto copertina fallito" , Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "Impossibile cambiare foto di copertina" , Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto" , Toast.LENGTH_SHORT).show();
                                }
                            });
                        }catch (Exception e){
                            return;
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento foto copertina annullato", Toast.LENGTH_SHORT).show();
                        CambiaFotoCopertina.dismiss();
                    }
                });
                return false;
            }
        });
        Cambia_Nome.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderNome = new AlertDialog.Builder(getContext());
                final View PopUpViewNome = getLayoutInflater().inflate(R.layout.cambia_nome_popup, null);
                Conferma = (AppCompatButton) PopUpViewNome.findViewById(R.id.conferma_button_nome);
                Annulla = (AppCompatButton) PopUpViewNome.findViewById(R.id.annulla_button_nome);
                NuovoAttributo =  PopUpViewNome.findViewById(R.id.InsertNuovonome);
                dialogBilderNome.setView(PopUpViewNome);
                CambiaNome = dialogBilderNome.create();
                CambiaNome.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            String Tipo = "Nome";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, NuovoAttributo.getText().toString());
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaNome.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci nome", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento nome annullato", Toast.LENGTH_SHORT).show();
                        CambiaNome.dismiss();
                    }
                });
            }
        });
        Cambia_Cognome.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderCognome = new AlertDialog.Builder(getContext());
                final View PopUpViewCognome = getLayoutInflater().inflate(R.layout.cambia_cognome_popup, null);
                Conferma = (AppCompatButton) PopUpViewCognome.findViewById(R.id.conferma_button_cognome);
                Annulla = (AppCompatButton) PopUpViewCognome.findViewById(R.id.annulla_button_cognome);
                NuovoAttributo =  PopUpViewCognome.findViewById(R.id.InsertNuovocognome);
                dialogBilderCognome.setView(PopUpViewCognome);
                CambiaCognome = dialogBilderCognome.create();
                CambiaCognome.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            String Tipo = "Cognome";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, NuovoAttributo.getText().toString());
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaCognome.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(Call<DBModelResponseToInsert> call, Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci cognome", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento cognome annullato", Toast.LENGTH_SHORT).show();
                        CambiaCognome.dismiss();
                    }
                });
            }
        });
        Cambia_Descrizione.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderDescrizione = new AlertDialog.Builder(getContext());
                final View PopUpViewDescrizione = getLayoutInflater().inflate(R.layout.cambia_descrizione_popup, null);
                Conferma = (AppCompatButton) PopUpViewDescrizione.findViewById(R.id.conferma_button_descrizone);
                Annulla = (AppCompatButton) PopUpViewDescrizione.findViewById(R.id.annulla_button_descrizione);
                NuovoAttributo =  PopUpViewDescrizione.findViewById(R.id.InsertNuovadescrizione);
                dialogBilderDescrizione.setView(PopUpViewDescrizione);
                CambiaDescrizione = dialogBilderDescrizione.create();
                CambiaDescrizione.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            String Tipo = "Descrizione";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, NuovoAttributo.getText().toString());
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaDescrizione.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci descrizione", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento descrizione annullato.", Toast.LENGTH_SHORT).show();
                        CambiaDescrizione.dismiss();
                    }
                });
            }
        });
        Cambia_Email.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderEmail = new AlertDialog.Builder(getContext());
                final View PopUpViewEmail = getLayoutInflater().inflate(R.layout.cambia_email_popup, null);
                Conferma = (AppCompatButton) PopUpViewEmail.findViewById(R.id.conferma_button_email);
                Annulla = (AppCompatButton) PopUpViewEmail.findViewById(R.id.annulla_button_email);
                NuovoAttributo =  PopUpViewEmail.findViewById(R.id.InsertNuovaemail);
                dialogBilderEmail.setView(PopUpViewEmail);
                CambiaEmail = dialogBilderEmail.create();
                CambiaEmail.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            String Tipo = "Email";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, NuovoAttributo.getText().toString());
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaEmail.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci descrizione", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento descrizione annullato", Toast.LENGTH_SHORT).show();
                        CambiaEmail.dismiss();
                    }
                });
            }
        });
        Cambia_DataNascita.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderDataNascita = new AlertDialog.Builder(getContext());
                final View PopUpViewDataNascita = getLayoutInflater().inflate(R.layout.cambia_datanascita_popup, null);
                Conferma = (AppCompatButton) PopUpViewDataNascita.findViewById(R.id.conferma_button_bday);
                Annulla = (AppCompatButton) PopUpViewDataNascita.findViewById(R.id.annulla_button_bday);
                NuovoAttributo =  PopUpViewDataNascita.findViewById(R.id.NewBday);
                dialogBilderDataNascita.setView(PopUpViewDataNascita);
                CambiaDataNascita = dialogBilderDataNascita.create();
                CambiaDataNascita.show();
                NuovoAttributo.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        int mYear = c.get(Calendar.YEAR);
                        int mMonth = c.get(Calendar.MONTH);
                        int mDay = c.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(getContext(), R.style.datepicker,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        NuovoAttributo.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            String Tipo = "DataNascita";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, NuovoAttributo.getText().toString());
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaDataNascita.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci data di nascita", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento data di nascita annullato", Toast.LENGTH_SHORT).show();
                        CambiaDataNascita.dismiss();
                    }
                });
            }
        });
        Cambia_Gender.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderGender = new AlertDialog.Builder(getContext());
                final View PopUpViewGender = getLayoutInflater().inflate(R.layout.cambia_gender_popup, null);
                CampiRicerca = PopUpViewGender.findViewById(R.id.Gender);
                Conferma = (AppCompatButton) PopUpViewGender.findViewById(R.id.conferma_button_gender);
                Annulla = (AppCompatButton) PopUpViewGender.findViewById(R.id.annulla_button_gender);
                dialogBilderGender.setView(PopUpViewGender);
                CambiaGender = dialogBilderGender.create();
                CambiaGender.show();
                final String[] Nuovosesso = {null};
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        int camposelezionato = CampiRicerca.getCheckedRadioButtonId();
                        if (camposelezionato == -1){
                            Toast.makeText(getContext(), "Seleziona un campo di ricerca", Toast.LENGTH_SHORT).show();
                        }else{
                            Nuovosesso[0] = CambiaSesso(camposelezionato);
                        }
                        if(Nuovosesso[0].length() != 0){
                            String Tipo = "Gender";
                            Call<DBModelResponseToInsert> cabiaAttributoCall = retrofitServiceDBInterno.CambiaInformazioni(UsernameProprietario, Tipo, Nuovosesso[0]);
                            cabiaAttributoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null) {
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " avvenuto con successo" , Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaGender.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento " + Tipo + " fallito", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        Toast.makeText(getContext(), "Impissibile cambiare " + Tipo + ".", Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(getContext() , "Inserisci sesso", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext() , "Cambiamento sesso annullato", Toast.LENGTH_SHORT).show();
                        CambiaGender.dismiss();
                    }
                });
            }
        });
    }

    private String CambiaSesso(int camposelezionato) {
        String Sesso = null;
        switch (camposelezionato){
            case R.id.gender_female:
                Sesso = "F";
                break;
            case R.id.gender_male:
                Sesso = "M";
                break;
            case R.id.gender_other:
                Sesso = "O";
                break;
        }
        return Sesso;
    }

    private void scegliImmagine() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleziona immagine"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                NuovaFoto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getImgepath(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte [] imageByte = outputStream.toByteArray();
        String Immagine = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return Immagine;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.my_nav_host_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}