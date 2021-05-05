package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Frag.Adapter.MovieListPrefAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import org.angmarch.views.NiceSpinner;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPreferiti#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class FragmentPreferiti extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AppCompatTextView preferitiText;
    private AppCompatTextView davedereText;
    private AppCompatTextView listePersonalizzateText;
    private LinearLayout ListePersonalizzateTextLayout, DavedereTextLayout, PreferitiTextLayout;
    private RecyclerView recyclerViewPreferiti;
    private static final String lingua = "it-IT";
    private List<DBModelDataFilms> preferiti;
    final ArrayList<String> listefilmutente = new ArrayList<>();
    private String UsernameProprietario;
    private MovieListPrefAdapter moviedetailAdapter;
    private String scelta = "Scelga la lista da visualizzare";
    private NiceSpinner listePresenti;
    private String ListaSelezionata = null;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private boolean inizializza = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentPreferiti() {
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
    public static FragmentPreferiti newInstance(String param1, String param2) {
        FragmentPreferiti fragment = new FragmentPreferiti();
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
        UsernameProprietario = this.getArguments().getString("Username");
        View v = inflater.inflate(R.layout.fragment__preferiti, container, false);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        listePresenti = v.findViewById(R.id.liste_presenti);
        if(inizializza == true) {
            inizializza = false;
            listefilmutente.add(scelta);
            Call<DBModelDataListeFilmResponce> dbModelListeFilmResponseCall = retrofitServiceDBInterno.getListeFilm(UsernameProprietario);
            dbModelListeFilmResponseCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull retrofit2.Response<DBModelDataListeFilmResponce> response) {
                    DBModelDataListeFilmResponce dbModelListeFilmResponse = response.body();
                    if(dbModelListeFilmResponse != null){
                        List<DBModelDataListeFilm> dataListeFilms = dbModelListeFilmResponse.getListeFilms();
                        for(int i = 0; i < dataListeFilms.size(); i++){
                            listefilmutente.add(dataListeFilms.get(i).getTitoloLista());
                        }
                        listePresenti.attachDataSource(listefilmutente);
                    }else{
                        Toast.makeText(getContext(), "Nessuna Lista Da Mostrare", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(getContext(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Call<DBModelDataListeFilmResponce> dbModelListeFilmResponseCall = retrofitServiceDBInterno.getListeFilm(UsernameProprietario);
            dbModelListeFilmResponseCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull retrofit2.Response<DBModelDataListeFilmResponce> response) {
                    DBModelDataListeFilmResponce dbModelListeFilmResponse = response.body();
                    if(dbModelListeFilmResponse != null){
                        List<DBModelDataListeFilm> dataListeFilms = dbModelListeFilmResponse.getListeFilms();
                        for(int i = 0; i < dataListeFilms.size(); i++){
                            listefilmutente.add(dataListeFilms.get(i).getTitoloLista());
                        }
                        listePresenti.attachDataSource(listefilmutente);
                    }else{
                        Toast.makeText(getContext(), "Nessuna Lista Da Mostrare.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(getContext(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        preferitiText = v.findViewById(R.id.preferiti_text);
        davedereText = v.findViewById(R.id.davedere_text);
        listePersonalizzateText = v.findViewById(R.id.liste_personalizzate_text);
        PreferitiTextLayout = v.findViewById(R.id.preferiti_text_layout);
        DavedereTextLayout = v.findViewById(R.id.davedere_text_layout);
        ListePersonalizzateTextLayout = v.findViewById(R.id.liste_personalizzate_text_layout);
        recyclerViewPreferiti = v.findViewById(R.id.Preferiti_List);
        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listePresenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    int numero = listePresenti.getSelectedIndex();
                    ListaSelezionata = String.valueOf(listefilmutente.get(numero));
                    preferiti = new ArrayList<>();
                    Call<DBModelFilmsResponce> dbModelFilmsResponceCall = retrofitServiceDBInterno.PrendiFilmDaDB(UsernameProprietario, ListaSelezionata);
                    dbModelFilmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
                        @Override public void onResponse(@NonNull Call<DBModelFilmsResponce> call,@NonNull retrofit2.Response<DBModelFilmsResponce> response) {
                            DBModelFilmsResponce dbModelFilmsResponce = response.body();
                            if(dbModelFilmsResponce != null){
                                List<DBModelDataFilms> dbModelDataFilms = dbModelFilmsResponce.getResults();
                                if(dbModelDataFilms.size() > 0){
                                    if(ListaSelezionata.equals("Preferiti")){
                                        PreferitiTextLayout.setVisibility(View.VISIBLE);
                                        DavedereTextLayout.setVisibility(View.GONE);
                                        ListePersonalizzateTextLayout.setVisibility(View.GONE);
                                        listePresenti.setSelectedIndex(0);
                                    }else if(ListaSelezionata.equals("Da Vedere")){
                                        DavedereTextLayout.setVisibility(View.VISIBLE);
                                        PreferitiTextLayout.setVisibility(View.GONE);
                                        ListePersonalizzateTextLayout.setVisibility(View.GONE);
                                        listePresenti.setSelectedIndex(0);
                                    }else{
                                        listePersonalizzateText.setText(ListaSelezionata);
                                        ListePersonalizzateTextLayout.setVisibility(View.VISIBLE);
                                        PreferitiTextLayout.setVisibility(View.GONE);
                                        DavedereTextLayout.setVisibility(View.GONE);
                                        listePresenti.setSelectedIndex(0);
                                    }
                                    recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    moviedetailAdapter = new MovieListPrefAdapter(getActivity(), dbModelDataFilms, UsernameProprietario, ListaSelezionata);
                                    recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                                    recyclerViewPreferiti.setLayoutAnimation(controller);
                                    recyclerViewPreferiti.scheduleLayoutAnimation();
                                }else{
                                    Toast.makeText(getContext(), "La sua lista al momento è vuota.",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "Impossibile Mostrare la Lista.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
                            Toast.makeText(getContext(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return v;
    }

    @Override public void onResume() {
        super.onResume();
        Call<DBModelFilmsResponce> dbModelFilmsResponceCall = retrofitServiceDBInterno.PrendiFilmDaDB(UsernameProprietario, ListaSelezionata);
        dbModelFilmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelFilmsResponce> call,@NonNull retrofit2.Response<DBModelFilmsResponce> response) {
            DBModelFilmsResponce dbModelFilmsResponce = response.body();
            if(dbModelFilmsResponce != null){
                List<DBModelDataFilms> dbModelDataFilms = dbModelFilmsResponce.getResults();
                if(dbModelDataFilms.size() > 0){
                    if(ListaSelezionata.equals("Preferiti")){
                        PreferitiTextLayout.setVisibility(View.VISIBLE);
                        DavedereTextLayout.setVisibility(View.GONE);
                        ListePersonalizzateTextLayout.setVisibility(View.GONE);
                        listePresenti.setSelectedIndex(0);
                    }else if(ListaSelezionata.equals("Da Vedere")){
                        DavedereTextLayout.setVisibility(View.VISIBLE);
                        PreferitiTextLayout.setVisibility(View.GONE);
                        ListePersonalizzateTextLayout.setVisibility(View.GONE);
                        listePresenti.setSelectedIndex(0);
                    }else{
                        listePersonalizzateText.setText(ListaSelezionata);
                        ListePersonalizzateTextLayout.setVisibility(View.VISIBLE);
                        PreferitiTextLayout.setVisibility(View.GONE);
                        DavedereTextLayout.setVisibility(View.GONE);
                        listePresenti.setSelectedIndex(0);
                    }
                    recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    moviedetailAdapter = new MovieListPrefAdapter(getActivity(), dbModelDataFilms, UsernameProprietario, ListaSelezionata);
                    recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                    recyclerViewPreferiti.setLayoutAnimation(controller);
                    recyclerViewPreferiti.scheduleLayoutAnimation();
                }else{
                    Toast.makeText(getContext(), "La sua lista al momento è vuota.",Toast.LENGTH_SHORT).show();
                }
            }else{
               Toast.makeText(getContext(), "Impossibile Mostrare la Lista.",Toast.LENGTH_SHORT).show();
            }
        }
        @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
            }
        });
    }
}