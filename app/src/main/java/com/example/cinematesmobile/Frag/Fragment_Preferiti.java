package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.MovieListPrefAdapter;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.DBModelDataFilms;
import com.example.cinematesmobile.Search.MovieDetailActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
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
    private RecyclerView recyclerViewPreferiti;
    private static final String lingua = "it-IT";
    private List<DBModelDataFilms> preferiti;
    private String user = "mattia.golino@gmail.com";
    private MovieListPrefAdapter moviedetailAdapter;
    private static final String URL = "http://192.168.1.9/cinematesdb/PrendiDaPreferiti.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSePresente.php";
    public static final String JSON_ARRAY = "dbdata";
    private ArrayList<Integer> id_preferti = new ArrayList<>();
    private ArrayList<Integer> id_presente = new ArrayList<>();
    private boolean firstuse = true;

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
        recyclerViewPreferiti = v.findViewById(R.id.Preferiti_List);
        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        preferiti = new ArrayList<>();
        PrendiListaDaPreferiti(user);
        return v;
    }


    @Override public void onResume() {
        super.onResume();
        for (int i = 0; i<id_preferti.size(); i++){
            verificaSePresente(id_preferti.get(i));
        }
        if (id_preferti.size() != id_presente.size() || firstuse == false) {
            PrendiListaDaPreferiti(user);
        }
    }

    private void PrendiListaDaPreferiti(String user) {
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
                        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        moviedetailAdapter = new MovieListPrefAdapter(getActivity(), preferiti);
                        recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                        recyclerViewPreferiti.setLayoutAnimation(controller);
                        recyclerViewPreferiti.scheduleLayoutAnimation();
                        Toast.makeText(getContext(), "La Sua Lista Al Momento è Vuota.",Toast.LENGTH_SHORT).show();
                    }else {
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void verificaSePresente(int id) {
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
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}