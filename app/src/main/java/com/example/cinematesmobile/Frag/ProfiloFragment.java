package com.example.cinematesmobile.Frag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cinematesmobile.Recensioni.RecensioniActivity;
import com.example.cinematesmobile.Recensioni.ScriviRecensioneActivity;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

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

    private LinearLayout CambiaPass, VaiARecensioniScritte, VaiAgliAmici, VaiAListeFilm;
    private String UsernameProprietario;
    private String EmailProprietario;
    private String Username, Nome, Cognome, Email, Passwd, Foto_Profilo, Descrizione, DataNascita, Sesso;
    private Integer  Recensioni_Scritte, TotaleAmici, NumeroListe;
    public CircleImageView ImmagineProfilo;
    private AlertDialog.Builder dialogBilder;
    private AlertDialog CambiaPassword;
    private AppCompatButton Conferma,Annulla;
    private Bundle bundle = new Bundle();
    private TextInputLayout VecchiaPass, NuovaPass, ConfermaPass;
    private TextInputEditText InserisciVecchiaPass, InserisciNuovaPass, ConfermaNuovaPass;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroListePersonalizzate, NumeroAmici;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, PasswordUser, DescrizioneUser, DataNascitaUser, SessoUser;
    private static final String PROFURL = "http://192.168.1.9/cinematesdb/PrendiUserDataDaDB.php";
    private static final String PASSURL = "http://192.168.1.9/cinematesdb/CambiaPassword.php";
    public static final String JSON_ARRAY = "dbdata";

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
        NumeroRecensioniScritte = v.findViewById(R.id.Numero_recensioni_scritte);
        NumeroListePersonalizzate = v.findViewById(R.id.Numero_liste_personalizzate);
        NumeroAmici = v.findViewById(R.id.Numero_amici);
        NomeUser = v.findViewById(R.id.Nome_user);
        CognomeUser = v.findViewById(R.id.Cognome_user);
        EmailUser = v.findViewById(R.id.Email_User);
        PasswordUser = v.findViewById(R.id.Password_user);
        DescrizioneUser = v.findViewById(R.id.Descrizione_User);
        DataNascitaUser = v.findViewById(R.id.Data_nascita_user);
        SessoUser = v.findViewById(R.id.Sesso_user);
        CambiaPass = v.findViewById(R.id.Cambia_Passwd);
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
                            String  str_liste = object.getString("Numero_Liste_Personalizzate");
                            Recensioni_Scritte = Integer.valueOf(str_rece);
                            TotaleAmici = Integer.valueOf(str_amici);
                            NumeroListe = Integer.valueOf(str_liste);
                        }
                    if(Foto_Profilo.equals(":null")){
                        Glide.with(getContext()).load(Foto_Profilo).into(ImmagineProfilo);
                    }else{
                        ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_cineblack);
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
                    if(Descrizione.equals(":null")){
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
                            startActivity(intent2);
                        }
                    });
                    CambiaPass.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            dialogBilder = new AlertDialog.Builder(getContext());
                            final View PopUpView = getLayoutInflater().inflate(R.layout.cambia_pass_pop_up, null);
                            Conferma = (AppCompatButton) PopUpView.findViewById(R.id.conferma_button);
                            Annulla = (AppCompatButton) PopUpView.findViewById(R.id.annulla_button);
                            InserisciVecchiaPass = PopUpView.findViewById(R.id.Inserisci_VecchiaPass);
                            InserisciNuovaPass =  PopUpView.findViewById(R.id.Inserisci_NuovaPass);
                            ConfermaNuovaPass =  PopUpView.findViewById(R.id.Conferma_NuovaPass);
                            VecchiaPass = PopUpView.findViewById(R.id.layout_vecchia_pass);
                            NuovaPass = PopUpView.findViewById(R.id.layout_nuova_pass);
                            ConfermaPass = PopUpView.findViewById(R.id.layout_conferma_pass);
                            dialogBilder.setView(PopUpView);
                            CambiaPassword = dialogBilder.create();
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