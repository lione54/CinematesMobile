package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.Frag.Adapter.NotificationAdapter;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelRichiesteAmicizia;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelSegnalazioni;
import com.example.cinematesmobile.R;

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
 * Use the {@link FragmentNotifiche#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentNotifiche extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String NomeUtente;
    private List<DBNotificheModelRichiesteAmicizia> richiesteAmiciziaList = new ArrayList<>();
    private List<DBNotificheModelSegnalazioni> segnalazioniList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private RecyclerView notifiche;
    private static final String NOTURL = "http://192.168.1.9/cinematesdb/PrendiNotificheDaDB.php";
    public static final String JSON_ARRAY = "dbdata";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNotifiche() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNotifiche.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNotifiche newInstance(String param1, String param2) {
        FragmentNotifiche fragment = new FragmentNotifiche();
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
        // Inflate the layout for this fragment
        NomeUtente = this.getArguments().getString("Username");
        View v = inflater.inflate(R.layout.fragment_notifiche, container, false);
        notifiche = v.findViewById(R.id.Notifiche_List);
        notifiche.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CercaNotifiche(NomeUtente);
        return v;
    }

    private void CercaNotifiche(String nomeUtente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String str_id = object.getString("Divisore");
                        if(str_id.equals("1")) {
                            String str_id_segn = object.getString("Id_Segnalazione");
                            String UserSegnalato = object.getString("UserSegnalato");
                            String Moti = object.getString("Motivazione");
                            String Titolo_Film_Segn_Accettata = object.getString("Titolo_Film");
                            String Stato = "Accettata";
                            String Motivazione = Moti.replaceAll("-", " ");
                            Integer IdSegnalazione = Integer.valueOf(str_id_segn);
                            DBNotificheModelSegnalazioni dbNotificheModelSegnalazioni = new DBNotificheModelSegnalazioni(IdSegnalazione, UserSegnalato, nomeUtente, Motivazione, Titolo_Film_Segn_Accettata, Stato);
                            segnalazioniList.add(dbNotificheModelSegnalazioni);
                        }else if(str_id.equals("2")){
                            String str_id_segn = object.getString("Id_Segnalazione");
                            String UserSegnalato = object.getString("UserSegnalato");
                            String Moti = object.getString("Motivazione");
                            String Titolo_Film_Segn_Declinata = object.getString("Titolo_Film");
                            String Stato = "Declinata";
                            String Motivazione = Moti.replaceAll("-", " ");
                            Integer IdSegnalazione = Integer.valueOf(str_id_segn);
                            DBNotificheModelSegnalazioni dbNotificheModelSegnalazioni = new DBNotificheModelSegnalazioni(IdSegnalazione, UserSegnalato, nomeUtente, Motivazione, Titolo_Film_Segn_Declinata, Stato);
                            segnalazioniList.add(dbNotificheModelSegnalazioni);
                        }else if(str_id.equals("3")){
                            String UserQuasiAmico = object.getString("E_Amico_Di");
                            String str_foto = object.getString("Foto_Profilo");
                            String Foto = "http://192.168.1.9/cinematesdb/"+ str_foto;
                            String Stato = "Inviata";
                            DBNotificheModelRichiesteAmicizia dbNotificheModelRichiesteAmicizia = new DBNotificheModelRichiesteAmicizia(nomeUtente, UserQuasiAmico,Foto, Stato);
                            richiesteAmiciziaList.add(dbNotificheModelRichiesteAmicizia);
                        }else if(str_id.equals("4")){
                            String UserAmico = object.getString("E_Amico_Di");
                            String str_foto = object.getString("Foto_Profilo");
                            String Foto = "http://192.168.1.9/cinematesdb/"+ str_foto;
                            String Stato = "Accettata";
                            DBNotificheModelRichiesteAmicizia dbNotificheModelRichiesteAmicizia = new DBNotificheModelRichiesteAmicizia(nomeUtente, UserAmico,Foto, Stato);
                            richiesteAmiciziaList.add(dbNotificheModelRichiesteAmicizia);
                        }
                    }
                    if(!(richiesteAmiciziaList.isEmpty()) && !(segnalazioniList.isEmpty())){
                        notifiche.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        notificationAdapter = new NotificationAdapter(getActivity(), richiesteAmiciziaList, segnalazioniList, nomeUtente);
                        notifiche.setAdapter(notificationAdapter);
                    }else if(!(richiesteAmiciziaList.isEmpty()) && segnalazioniList.isEmpty()) {
                        notifiche.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        notificationAdapter = new NotificationAdapter(getActivity(), richiesteAmiciziaList, segnalazioniList, nomeUtente);
                        notifiche.setAdapter(notificationAdapter);
                    }else if(richiesteAmiciziaList.isEmpty() && !(segnalazioniList.isEmpty())) {
                        notifiche.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        notificationAdapter = new NotificationAdapter(getActivity(), richiesteAmiciziaList, segnalazioniList, nomeUtente);
                        notifiche.setAdapter(notificationAdapter);
                    }else{
                        Toast.makeText(getActivity(), "Nessuna nuova notifica", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext() , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("UserProprietario", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}