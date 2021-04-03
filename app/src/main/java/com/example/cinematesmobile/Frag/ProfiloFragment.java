package com.example.cinematesmobile.Frag;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;

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

    private String UsernameProprietario;
    private String EmailProprietario;
    private String Username, Nome, Cognome, Email, Passwd, Foto_Profilo, Descrizione, DataNascita, Sesso;
    private Integer  Recensioni_Scritte, TotaleAmici, NumeroListe;
    public CircleImageView ImmagineProfilo;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroListePersonalizzate, NumeroAmici;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, PasswordUser, DescrizioneUser, DataNascitaUser, SessoUser;
    private static final String PROFURL = "http://192.168.1.9/cinematesdb/PrendiUserDataDaDB.php";
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
        CaricaProfilo(UsernameProprietario, EmailProprietario);
        return v;
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
                    PasswordUser.setText(Passwd);
                    if(Descrizione.equals(":null")){
                        DescrizioneUser.setText(Descrizione);
                    }else{
                        DescrizioneUser.setText("Descrizione Non Inserita Dall'User.");
                    }
                    DataNascitaUser.setText(DataNascita);
                    SessoUser.setText(Sesso);
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
}