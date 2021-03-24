package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.MovieListPrefAdapter;
import com.example.cinematesmobile.Search.Model.DBModelDataFilms;

import org.angmarch.views.NiceSpinner;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Preferiti#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class Fragment_Preferiti extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppCompatTextView preferitiText;
    private AppCompatTextView davedereText;
    private AppCompatTextView listePersonalizzateText;
    private RecyclerView recyclerViewPreferiti;
    private static final String lingua = "it-IT";
    private List<DBModelDataFilms> preferiti;
    final ArrayList<String> listefilmutente = new ArrayList<>();
    private String user = "mattia.golino@gmail.com";
    private MovieListPrefAdapter moviedetailAdapter;
    private String scelta = "Scelga Quale Lista Vuole Viusalizzare.";
    private NiceSpinner listePresenti;
    private String ListaSelezionata = null;
    private static final String URL = "http://192.168.1.9/cinematesdb/PrendiDaLista.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSePresente.php";
    private static final String LISURL = "http://192.168.1.9/cinematesdb/TrovaListeDaVisualizzare.php";
    public static final String JSON_ARRAY = "dbdata";
    private ArrayList<Integer> id_preferti = new ArrayList<>();
    private ArrayList<Integer> id_presente = new ArrayList<>();
    private boolean firstuse = true;
    private boolean inizializza = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Preferiti() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Preferiti.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Preferiti newInstance(String param1, String param2) {
        Fragment_Preferiti fragment = new Fragment_Preferiti();
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

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__preferiti, container, false);
        listePresenti = v.findViewById(R.id.liste_presenti);
        if(inizializza == true) {
            inizializza = false;
            listefilmutente.add(scelta);
            ListePresenti(user);
        }else{
            ListePresenti(user);
        }
        preferitiText = v.findViewById(R.id.preferiti_text);
        davedereText = v.findViewById(R.id.davedere_text);
        listePersonalizzateText = v.findViewById(R.id.liste_personalizzate_text);
        recyclerViewPreferiti = v.findViewById(R.id.Preferiti_List);
        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listePresenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    int numero = listePresenti.getSelectedIndex();
                    ListaSelezionata = String.valueOf(listefilmutente.get(numero));
                    preferiti = new ArrayList<>();
                    PrendiDaListe(user, ListaSelezionata);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }

    @Override public void onResume() {
        super.onResume();
        for (int i = 0; i<id_preferti.size(); i++){
            verificaSePresente(id_preferti.get(i), user, ListaSelezionata);
        }
        if (id_preferti.size() != id_presente.size() || firstuse == false) {
            PrendiDaListe(user, ListaSelezionata);
        }
    }

    private void PrendiDaListe(String user, String TipoLista) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    if(firstuse == true){
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id_film = object.getString("id_film_inserito");
                            String str_titolo = object.getString("Titolo_Film");
                            String str_url = object.getString("Url_Immagine");
                            Integer id_film = Integer.parseInt(str_id_film);
                            String strt_titoloMod = str_titolo.replaceAll("/", "'");
                            DBModelDataFilms dbModelDataFilms = new DBModelDataFilms(id_film, strt_titoloMod, str_url);
                            id_preferti.add(id_film);
                            preferiti.add(dbModelDataFilms);
                            firstuse = false;
                        }
                    }else{
                        id_preferti.clear();
                        preferiti.clear();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id_film = object.getString("id_film_inserito");
                            String str_titolo = object.getString("Titolo_Film");
                            String str_url = object.getString("Url_Immagine");
                            Integer id_film = Integer.parseInt(str_id_film);
                            String strt_titoloMod = str_titolo.replaceAll("/", "'");
                            DBModelDataFilms dbModelDataFilms = new DBModelDataFilms(id_film, strt_titoloMod, str_url);
                            id_preferti.add(id_film);
                            preferiti.add(dbModelDataFilms);
                        }
                    }
                    if(preferiti.isEmpty()){
                        if(TipoLista.equals("Preferiti")){
                            preferitiText.setVisibility(View.VISIBLE);
                            davedereText.setVisibility(View.GONE);
                            listePersonalizzateText.setVisibility(View.GONE);
                        }else if(TipoLista.equals("Da Vedere")){
                            davedereText.setVisibility(View.VISIBLE);
                            preferitiText.setVisibility(View.GONE);
                            listePersonalizzateText.setVisibility(View.GONE);
                        }else{
                            listePersonalizzateText.setText(TipoLista);
                            listePersonalizzateText.setVisibility(View.VISIBLE);
                            preferitiText.setVisibility(View.GONE);
                            davedereText.setVisibility(View.GONE);
                        }
                        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        moviedetailAdapter = new MovieListPrefAdapter(getActivity(), preferiti);
                        recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                        recyclerViewPreferiti.setLayoutAnimation(controller);
                        recyclerViewPreferiti.scheduleLayoutAnimation();
                        Toast.makeText(getContext(), "La Sua Lista Al Momento è Vuota.",Toast.LENGTH_SHORT).show();
                    }else {
                        if(TipoLista.equals("Preferiti")){
                            preferitiText.setVisibility(View.VISIBLE);
                            davedereText.setVisibility(View.GONE);
                            listePersonalizzateText.setVisibility(View.GONE);
                        }else if(TipoLista.equals("Da Vedere")){
                            davedereText.setVisibility(View.VISIBLE);
                            preferitiText.setVisibility(View.GONE);
                            listePersonalizzateText.setVisibility(View.GONE);
                        }else{
                            listePersonalizzateText.setText(TipoLista);
                            listePersonalizzateText.setVisibility(View.VISIBLE);
                            preferitiText.setVisibility(View.GONE);
                            davedereText.setVisibility(View.GONE);
                        }
                        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        moviedetailAdapter = new MovieListPrefAdapter(getActivity(), preferiti);
                        recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                        recyclerViewPreferiti.setLayoutAnimation(controller);
                        recyclerViewPreferiti.scheduleLayoutAnimation();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", user);
                params.put("Tipo_Lista",TipoLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void verificaSePresente(int id, String utente, String tipoLista) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("id_film_inserito");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 1) {
                        id_presente.add(id);
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id));
                params.put("User_Proprietario", utente);
                params.put("Tipo_Lista",tipoLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void ListePresenti(String utente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LISURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Tipo_Lista");
                        listefilmutente.add(respo);
                    }
                    listePresenti.attachDataSource(listefilmutente);
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
                params.put("User_Proprietario", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}