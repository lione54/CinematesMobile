package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.AttoriSearchAdapter;
import com.example.cinematesmobile.Search.Adapters.MovieSearchAdapter;
import com.example.cinematesmobile.Search.Adapters.RicercaUtenteAdapter;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.AttoriResponse;
import com.example.cinematesmobile.Search.Model.AttoriResponseResults;
import com.example.cinematesmobile.Search.Model.DBModelDataUser;
import com.example.cinematesmobile.Search.Model.MovieResponse;
import com.example.cinematesmobile.Search.Model.MovieResponseResults;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String UsernameProprietario;
    private AppCompatEditText queryEditText;
    private AppCompatButton querySearchButton;
    private RecyclerView recyclerViewRicerca;
    private RetrofitService retrofitService;
    private MovieSearchAdapter movieSearchAdapter;
    private AttoriSearchAdapter attoriSearchAdapter;
    private RicercaUtenteAdapter ricercaUtenteAdapter;
    private List<DBModelDataUser> UtentiCercati;
    public static final String JSON_ARRAY = "dbdata";
    private static final String URL = "http://192.168.1.9/cinematesdb/RicercaUtente.php";
    private RadioGroup CampiRicerca;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UsernameProprietario = this.getArguments().getString("Username");
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        queryEditText = v.findViewById(R.id.query_edit_text);
        querySearchButton = v.findViewById(R.id.query_search_button);
        recyclerViewRicerca = v.findViewById(R.id.results_recycle_view);
        CampiRicerca = v.findViewById(R.id.gruppo_ricerca);
        recyclerViewRicerca.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        querySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int camposelezionato = CampiRicerca.getCheckedRadioButtonId();
                if (camposelezionato == -1){
                    Toast.makeText(getContext(), "Seleziona Un Campo Di Ricerca.", Toast.LENGTH_SHORT).show();
                }else{
                    UtentiCercati = new ArrayList<>();
                    Ricerca(camposelezionato);
                }
            }
        });
        return v;
    }

    private void Ricerca(int camposelezionato){
        switch (camposelezionato){
            case R.id.radio_ricerca_film:
                if (queryEditText.getText() != null) {
                    String query = queryEditText.getText().toString();
                    String lingua = "it-IT";
                    if (query.equals("") || query.equals(" ")) {
                        Toast.makeText(getContext(), "Scrivi Qualcosa", Toast.LENGTH_SHORT).show();
                    } else {
                        queryEditText.setText("");
                        String finalQuery = query.replaceAll(" ", "+");
                        Call<MovieResponse> movieResponseCall = retrofitService.getMoviesByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua, finalQuery);
                        movieResponseCall.enqueue(new Callback<MovieResponse>() {
                            @Override public void onResponse(@NonNull Call<MovieResponse> call,@NonNull Response<MovieResponse> response) {
                                MovieResponse movieResponse = response.body();
                                if (movieResponse != null) {
                                    List<MovieResponseResults> movieResponseResults = movieResponse.getResults();
                                    recyclerViewRicerca.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    movieSearchAdapter = new MovieSearchAdapter(getActivity(), movieResponseResults);
                                    recyclerViewRicerca.setAdapter(movieSearchAdapter);
                                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                                    recyclerViewRicerca.setLayoutAnimation(controller);
                                    recyclerViewRicerca.scheduleLayoutAnimation();
                                } else {
                                    Toast.makeText(getContext(), "Nessuna Voce Corrisponde Ai Criteri Di Ricerca.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override public void onFailure(@NonNull Call<MovieResponse> call,@NonNull Throwable t) {
                                Toast.makeText(getContext(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            break;
            case R.id.radio_ricerca_attori:
                if (queryEditText.getText() != null) {
                    String query = queryEditText.getText().toString();
                    String lingua = "it-IT";
                    if (query.equals("") || query.equals(" ")) {
                        Toast.makeText(getContext(), "Scrivi Qualcosa", Toast.LENGTH_SHORT).show();
                    } else {
                        queryEditText.setText("");
                        String finalQuery = query.replaceAll(" ", "+");
                        Call<AttoriResponse> attoriResponseCall = retrofitService.getPersonByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua, finalQuery);
                        attoriResponseCall.enqueue(new Callback<AttoriResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<AttoriResponse> call, @NonNull Response<AttoriResponse> response) {
                                AttoriResponse attoriResponse = response.body();
                                if (attoriResponse != null) {
                                    List<AttoriResponseResults> attoriResponseResults = attoriResponse.getResults();
                                    recyclerViewRicerca.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    attoriSearchAdapter = new AttoriSearchAdapter(getActivity(), attoriResponseResults);
                                    recyclerViewRicerca.setAdapter(attoriSearchAdapter);
                                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                                    recyclerViewRicerca.setLayoutAnimation(controller);
                                    recyclerViewRicerca.scheduleLayoutAnimation();
                                } else {
                                    Toast.makeText(getContext(), "Nessuna Voce Corrisponde Ai Criteri Di Ricerca.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AttoriResponse> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            break;
            case R.id.radio_ricerca_amici:
                if (queryEditText.getText() != null) {
                    String query = queryEditText.getText().toString();
                    if (query.equals("") || query.equals(" ")) {
                        Toast.makeText(getContext(), "Scrivi Qualcosa", Toast.LENGTH_SHORT).show();
                    } else {
                        if(queryEditText.getText().toString().equals(UsernameProprietario)){
                            Toast.makeText(getContext(), "Non Puoi Cercare Te Stesso", Toast.LENGTH_SHORT).show();
                        }else {
                            RicercaUtente(query);
                        }
                    }
                }
            break;
        }
    }

    private void RicercaUtente(String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for(int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id_utente = object.getString("Id_Utente");
                            String str_username = object.getString("UserName");
                            String str_foto_profilo = object.getString("Foto_Profilo");
                            String Foto = "http://192.168.1.9/cinematesdb/"+ str_foto_profilo;
                            String str_amicizia = object.getString("Amicizia");
                            String str_amici_in_com = object.getString("Amici_In_Comune");
                            Integer id_utente = Integer.parseInt(str_id_utente);
                            Integer EsisteAmicizia = Integer.parseInt(str_amicizia);
                            Integer AmiciInComune = Integer.parseInt(str_amici_in_com);
                            String strt_userMod = str_username.replaceAll("/", "'");
                            DBModelDataUser dbModelDataUser = new DBModelDataUser(UsernameProprietario,strt_userMod, Foto, id_utente, EsisteAmicizia, AmiciInComune);
                            UtentiCercati.add(dbModelDataUser);
                        }
                    if(UtentiCercati.isEmpty()){
                        recyclerViewRicerca.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        ricercaUtenteAdapter = new RicercaUtenteAdapter(getActivity(), UtentiCercati);
                        recyclerViewRicerca.setAdapter(ricercaUtenteAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                        recyclerViewRicerca.setLayoutAnimation(controller);
                        recyclerViewRicerca.scheduleLayoutAnimation();
                        Toast.makeText(getContext(), "Nessun Utente Corrisponde Ai Criteri.",Toast.LENGTH_SHORT).show();
                    }else {
                        recyclerViewRicerca.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        ricercaUtenteAdapter = new RicercaUtenteAdapter(getActivity(), UtentiCercati);
                        recyclerViewRicerca.setAdapter(ricercaUtenteAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                        recyclerViewRicerca.setLayoutAnimation(controller);
                        recyclerViewRicerca.scheduleLayoutAnimation();
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
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserNameDaCercare", query);
                params.put("User_Proprietario",UsernameProprietario);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}