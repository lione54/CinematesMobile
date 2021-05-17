package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.cinematesmobile.Frag.Adapter.NotificationAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelNotifiche;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelRichiesteAmicizia;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelSegnalazioni;
import com.example.cinematesmobile.ModelDBInterno.DBModelNotificheResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private RetrofitServiceDBInterno retrofitServiceDBInterno;


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
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Call<DBModelVerifica> verificanotificheCall = retrofitServiceDBInterno.VerificaSeCiSonoNotifiche(NomeUtente);
        verificanotificheCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NotNull Call<DBModelVerifica> call, @NotNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null){
                    List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                    if(verificaResults.get(0).getCodVerifica() == 1){
                        Call<DBModelNotificheResponce> notificheResponceCall = retrofitServiceDBInterno.PrendiNotificheDaDB(NomeUtente);
                        notificheResponceCall.enqueue(new Callback<DBModelNotificheResponce>() {
                            @Override public void onResponse(Call<DBModelNotificheResponce> call, Response<DBModelNotificheResponce> response) {
                                DBModelNotificheResponce dbModelNotificheResponce = response.body();
                                if(dbModelNotificheResponce != null){
                                    List<DBModelNotifiche> notificheList = dbModelNotificheResponce.getResults();
                                    if(!(notificheList.isEmpty())){
                                        notifiche.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                        notificationAdapter = new NotificationAdapter(getActivity(),notificheList, NomeUtente);
                                        notifiche.setAdapter(notificationAdapter);
                                    }else {
                                        Toast.makeText(getActivity(), "Recupero notifiche dal database fallito.", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getActivity(), "Impossibile prendere notifiche dal database.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override public void onFailure(Call<DBModelNotificheResponce> call, Throwable t) {
                                Toast.makeText(getActivity(), "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(getActivity(), "Nessuna nuova notifica.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override public void onFailure(@NotNull Call<DBModelVerifica> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}