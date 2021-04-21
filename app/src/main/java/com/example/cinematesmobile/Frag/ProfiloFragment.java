package com.example.cinematesmobile.Frag;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.VisualizzaImmaginiActivity;
import com.example.cinematesmobile.SignIn.RegisterFieldsActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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

    final int PICK_IMAGE_REQUEST = 234;
    private LinearLayout VaiARecensioniScritte, VaiAgliAmici, VaiAListeFilm;
    private AppCompatImageView CambiaPass, Cambia_Nome, Cambia_Cognome, Cambia_Email, Cambia_Descrizione, Cambia_DataNascita, Cambia_Gender;
    private String UsernameProprietario;
    private String EmailProprietario;
    private KenBurnsView ImmagineCopertina;
    private String Username, Nome, Cognome, Email, Passwd, Foto_Profilo, Descrizione, DataNascita, Sesso, FotoCopertina;
    private Integer  Recensioni_Scritte, TotaleAmici, NumeroListe;
    public CircleImageView ImmagineProfilo, NuovaFoto;
    private AlertDialog.Builder dialogBilderPass, dialogBilderFotoProfilo, dialogBilderFotoCopertina, dialogBilderNome, dialogBilderCognome, dialogBilderEmail, dialogBilderConfermaEmail, dialogBilderDescrizione, dialogBilderDataNascita, dialogBilderGender;
    private AlertDialog CambiaPassword, CambiaFotoProfilo, CambiaFotoCopertina, CambiaNome, CambiaCognome, CambiaEmail, ConfermaCambiaEmail, CambiaDescrizione, CambiaDataNascita, CambiaGender;
    private Bitmap bitmap;
    private DatePickerDialog datePickerDialog;
    private AppCompatButton Conferma,Annulla, Scegli;
    private Bundle bundle = new Bundle();
    private TextInputLayout VecchiaPass, NuovaPass, ConfermaPass;
    private TextInputEditText InserisciVecchiaPass, InserisciNuovaPass, ConfermaNuovaPass, NuovoAttributo;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroListePersonalizzate, NumeroAmici;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, PasswordUser, DescrizioneUser, DataNascitaUser, SessoUser;
    private static final String PROFURL = "http://192.168.1.9/cinematesdb/PrendiUserDataDaDB.php";
    private static final String PASSURL = "http://192.168.1.9/cinematesdb/CambiaPassword.php";
    private static final String FOPURL = "http://192.168.1.9/cinematesdb/CambiaFotoProfilo.php";
    private static final String COPURL = "http://192.168.1.9/cinematesdb/CambiaFotoCopertina.php";
    private static final String INSURL = "http://192.168.1.9/cinematesdb/CambiaAttributo.php";
    public static final String JSON_ARRAY = "dbdata";
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
        CaricaProfilo(UsernameProprietario, EmailProprietario);
        return v;
    }

    @Override public void onResume() {
        super.onResume();
        CaricaProfilo(UsernameProprietario, EmailProprietario);
    }

    private void CaricaProfilo(String usernameProprietario, String emailProprietario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            Username = object.getString("UserName");
                            Nome = object.getString("Nome");
                            Cognome = object.getString("Cognome");
                            Email = object.getString("Email");
                            Passwd = object.getString("Passwd");
                            String  str_rece = object.getString("Recensioni_Scritte");
                            Foto_Profilo = object.getString("Foto_Profilo");
                            Descrizione = object.getString("Descrizione_Profilo");
                            DataNascita = object.getString("Data_Nascita");
                            Sesso = object.getString("Sesso");
                            String  str_amici = object.getString("Totale_Amici");
                            FotoCopertina = object.getString("Foto_Copertina");
                            String  str_liste = object.getString("Numero_Liste_Personalizzate");
                            Recensioni_Scritte = Integer.valueOf(str_rece);
                            TotaleAmici = Integer.valueOf(str_amici);
                            NumeroListe = Integer.valueOf(str_liste);
                        }
                    if(!(Foto_Profilo.equals("null"))){
                        String Foto = "http://192.168.1.9/cinematesdb/"+ Foto_Profilo;
                        Glide.with(getContext()).load(Foto).into(ImmagineProfilo);
                    }else{
                        ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_cineblack);
                    }
                    if(!(FotoCopertina.equals("null"))){
                        String Foto = "http://192.168.1.9/cinematesdb/"+ FotoCopertina;
                        Glide.with(getContext()).load(Foto).into(ImmagineCopertina);
                    }else{
                        //ImmagineCopertina.setImageResource(R.drawable.ic_baseline_person_24_cineblack);
                    }
                    UsernameProfilo.setText(Username);
                    NumeroRecensioniScritte.setText(String.valueOf(Recensioni_Scritte));
                    NumeroListePersonalizzate.setText(String.valueOf(NumeroListe));
                    NumeroAmici.setText(String.valueOf(TotaleAmici));
                    NomeUser.setText(Nome);
                    CognomeUser.setText(Cognome);
                    EmailUser.setText(Email);
                    String Pass = Passwd.replaceAll("[a-zA-Z0-9]", "\\*");
                    PasswordUser.setText(Pass);
                    if(!(Descrizione.equals("null"))){
                        DescrizioneUser.setText(Descrizione);
                    }else{
                        DescrizioneUser.setText("Descrizione Non Inserita Dall'User.");
                    }
                    DataNascitaUser.setText(DataNascita);
                    SessoUser.setText(Sesso);
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
                            intent2.putExtra("Nome_Utente", Username);
                            startActivity(intent2);
                        }
                    });
                    VaiAgliAmici.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent intent2 = new Intent(getActivity(), ListeAmiciActivity.class);
                            intent2.putExtra("Nome_Utente", Username);
                            intent2.putExtra("Nome_Proprietario", Username);
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
                                    if(InserisciVecchiaPass.length() == 0 || !(InserisciVecchiaPass.getText().toString().equals(Passwd))){
                                        if(InserisciVecchiaPass.length() == 0){
                                            VecchiaPass.setError("Inserisci Vecchia Password");
                                        }else{
                                            VecchiaPass.setError("La Vecchia Password Non Corrisponde");
                                        }
                                    }else{
                                        if(InserisciNuovaPass.length() == 0){
                                                NuovaPass.setError("Inserisci Nuova Password");
                                        }else if(ConfermaNuovaPass.length() == 0){
                                                ConfermaPass.setError("Inserisci Nuova Password");
                                        }else if(InserisciNuovaPass.length() != ConfermaNuovaPass.length()){
                                                ConfermaPass.setError("La Nuova Password Non Corrisponde");
                                        }else {
                                            cambiaPassword(usernameProprietario, InserisciNuovaPass.getText().toString());
                                            Fragment fragment = new ProfiloFragment();
                                            bundle.putString("Email", EmailProprietario);
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaPassword.dismiss();
                                        }
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    CambiaPassword.dismiss();
                                    Toast.makeText(getContext() , "Cambiamento Password Annullato", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    ImmagineProfilo.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent visualizzaImmagineintent = new Intent(getActivity(), VisualizzaImmaginiActivity.class);
                            String Foto = "http://192.168.1.9/cinematesdb/"+ Foto_Profilo;
                            visualizzaImmagineintent.putExtra("image_url", Foto);
                            startActivity(visualizzaImmagineintent);
                        }
                    });
                    ImmagineCopertina.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            Intent visualizzaImmagineintent = new Intent(getActivity(), VisualizzaImmaginiActivity.class);
                            String Foto = "http://192.168.1.9/cinematesdb/"+ FotoCopertina;
                            visualizzaImmagineintent.putExtra("image_url", Foto);
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
                                    scegliImmagine();
                                }
                            });
                            Conferma.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    try {
                                        UpdateFotoProfilo(Username, getImgepath(bitmap));
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaFotoProfilo.dismiss();
                                    }catch (Exception e){
                                        return;
                                    }

                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    CambiaFotoProfilo.dismiss();
                                    Toast.makeText(getContext() , "Cambiamento Foto Annullato", Toast.LENGTH_LONG).show();
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
                                    scegliImmagine();
                                }
                            });
                            Conferma.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    try {
                                        UpdateFotoCopertina(Username, getImgepath(bitmap));
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaFotoCopertina.dismiss();
                                    }catch (Exception e){
                                        return;
                                    }

                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    CambiaFotoCopertina.dismiss();
                                    Toast.makeText(getContext() , "Cambiamento Foto Annullato", Toast.LENGTH_LONG).show();
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
                                        InserisciNuovoAttributo(NuovoAttributo.getText().toString(), Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaNome.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Nome", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Nome Annullato", Toast.LENGTH_LONG).show();
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
                                        InserisciNuovoAttributo(NuovoAttributo.getText().toString(), Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaCognome.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Cognome", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Cognome Annullato", Toast.LENGTH_LONG).show();
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
                                        InserisciNuovoAttributo(NuovoAttributo.getText().toString(), Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaDescrizione.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Descrizione", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Descrizione Annullato", Toast.LENGTH_LONG).show();
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
                                        InserisciNuovoAttributo(NuovoAttributo.getText().toString(), Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaEmail.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Descrizione", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Descrizione Annullato", Toast.LENGTH_LONG).show();
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
                                        InserisciNuovoAttributo(NuovoAttributo.getText().toString(), Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaDataNascita.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Data Di Nascita", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Data Di Nascita Annullato", Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(getContext(), "Seleziona Un Campo Di Ricerca.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Nuovosesso[0] = CambiaSesso(camposelezionato);
                                    }
                                    if(Nuovosesso[0].length() != 0){
                                        String Tipo = "Gender";
                                        InserisciNuovoAttributo(Nuovosesso[0], Tipo, UsernameProprietario);
                                        Fragment fragment = new ProfiloFragment();
                                        bundle.putString("Email", EmailProprietario);
                                        bundle.putString("Username", UsernameProprietario);
                                        fragment.setArguments(bundle);
                                        loadFragment(fragment);
                                        CambiaGender.dismiss();
                                    }else{
                                        Toast.makeText(getContext() , "Inserisci Sesso", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    Toast.makeText(getContext() , "Cambiamento Sesso Annullato", Toast.LENGTH_LONG).show();
                                    CambiaGender.dismiss();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Username_Proprietario", usernameProprietario);
                params.put("Email_Proprietario", emailProprietario);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
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

    private void InserisciNuovoAttributo(String nuovoAttributo, String tipo, String usernameProprietario) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(getContext() , tipo + " Aggiornato", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", usernameProprietario);
                params.put("Tipo_Attributo", tipo);
                params.put("Nuovo_Attributo", nuovoAttributo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void UpdateFotoCopertina(String username, String imgepath) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, COPURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(getContext() , "Foto Copertina Aggiornata", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", username);
                params.put("nome", imgepath);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void UpdateFotoProfilo(String username, String image) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOPURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(getContext() , "Foto Profilo Aggiornata", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", username);
                params.put("nome", image);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void scegliImmagine() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleziona Immagine"), PICK_IMAGE_REQUEST);
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

    private void cambiaPassword(String usernameProprietario, String nuovaPassword) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(getContext() , "Password Aggiornata", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Nuova_Password", nuovaPassword);
                params.put("User_Proprietario", usernameProprietario);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}