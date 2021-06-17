package com.example.cinematesmobile.Frag;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import com.example.cinematesmobile.AsyncTask.NotificheAsyncTask;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Frag.Adapter.MovieListPrefAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.angmarch.views.NiceSpinner;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private AppCompatTextView preferitiText, davedereText, listePersonalizzateText, Descrizione, descText;
    private AppCompatImageButton ModDesc, Commenti;
    private LinearLayout ListePersonalizzateTextLayout, DavedereTextLayout, PreferitiTextLayout;
    private RecyclerView recyclerViewPreferiti;
    private static final String lingua = "it-IT";
    private List<DBModelDataFilms> preferiti;
    private AppCompatButton Conferma,Annulla;
    private TextInputLayout NuovoAttributoLayout;
    private TextInputEditText NuovoAttributo;
    final ArrayList<String> listefilmutente = new ArrayList<>();
    final ArrayList<String> listedescrizioniutente = new ArrayList<>();
    private String UsernameProprietario, DescrizioneTEXT;
    private MovieListPrefAdapter moviedetailAdapter;
    private String scelta = "Scelga la lista da visualizzare";
    private NiceSpinner listePresenti;
    private Bundle bundle = new Bundle();
    private String ListaSelezionata = null;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private AlertDialog.Builder dialogBilderDescrizione;
    private AlertDialog CambiaDescrizione;
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
        new NotificheAsyncTask(UsernameProprietario, getActivity()).execute();
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
                            listedescrizioniutente.add(dataListeFilms.get(i).getDescrizioneLista());
                        }
                        listePresenti.attachDataSource(listefilmutente);
                    }else{
                        Toast.makeText(getContext(), "Nessuna lista da mostrare", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
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
                            if(dataListeFilms.get(i).getDescrizioneLista() != null) {
                                listedescrizioniutente.add(dataListeFilms.get(i).getDescrizioneLista());
                            }else{
                                listedescrizioniutente.add("Descrizione non inserita dall' utente");
                            }
                        }
                        listePresenti.attachDataSource(listefilmutente);
                    }else{
                        Toast.makeText(getContext(), "Nessuna lista da mostrare", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                }
            });
        }
        preferitiText = v.findViewById(R.id.preferiti_text);
        davedereText = v.findViewById(R.id.davedere_text);
        listePersonalizzateText = v.findViewById(R.id.liste_personalizzate_text);
        PreferitiTextLayout = v.findViewById(R.id.preferiti_text_layout);
        DavedereTextLayout = v.findViewById(R.id.davedere_text_layout);
        Descrizione = v.findViewById(R.id.CorpoDescrizionePref);
        Commenti = v.findViewById(R.id.buttoncommenti);
        descText = v.findViewById(R.id.textdesc);
        ModDesc = v.findViewById(R.id.mod_desc);
        ListePersonalizzateTextLayout = v.findViewById(R.id.liste_personalizzate_text_layout);
        recyclerViewPreferiti = v.findViewById(R.id.Preferiti_List);
        recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        listePresenti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    int numero = listePresenti.getSelectedIndex();
                    DescrizioneTEXT = listedescrizioniutente.get(numero-1);
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
                                        Descrizione.setVisibility(View.GONE);
                                        Commenti.setVisibility(View.GONE);
                                        descText.setVisibility(View.GONE);
                                        ModDesc.setVisibility(View.GONE);
                                        listePresenti.setSelectedIndex(0);
                                    }else if(ListaSelezionata.equals("Da Vedere")){
                                        DavedereTextLayout.setVisibility(View.VISIBLE);
                                        PreferitiTextLayout.setVisibility(View.GONE);
                                        ListePersonalizzateTextLayout.setVisibility(View.GONE);
                                        Descrizione.setVisibility(View.GONE);
                                        Commenti.setVisibility(View.GONE);
                                        descText.setVisibility(View.GONE);
                                        ModDesc.setVisibility(View.GONE);
                                        listePresenti.setSelectedIndex(0);
                                    }else{
                                        listePersonalizzateText.setText(ListaSelezionata);
                                        ListePersonalizzateTextLayout.setVisibility(View.VISIBLE);
                                        PreferitiTextLayout.setVisibility(View.GONE);
                                        DavedereTextLayout.setVisibility(View.GONE);
                                        Descrizione.setVisibility(View.VISIBLE);
                                        Descrizione.setText(DescrizioneTEXT);
                                        Commenti.setVisibility(View.VISIBLE);
                                        descText.setVisibility(View.VISIBLE);
                                        ModDesc.setVisibility(View.VISIBLE);
                                        listePresenti.setSelectedIndex(0);
                                    }
                                    recyclerViewPreferiti.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    moviedetailAdapter = new MovieListPrefAdapter(getActivity(), dbModelDataFilms, UsernameProprietario, ListaSelezionata);
                                    recyclerViewPreferiti.setAdapter(moviedetailAdapter);
                                    LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                                    recyclerViewPreferiti.setLayoutAnimation(controller);
                                    recyclerViewPreferiti.scheduleLayoutAnimation();
                                }else{
                                    Toast.makeText(getContext(), "La sua lista al momento è vuota",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getContext(), "Impossibile mostrare la lista",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
                            Toast.makeText(getContext(), "Ops qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Commenti.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CommentiActivity.class);
                intent.putExtra("UsernameProprietario", UsernameProprietario);
                intent.putExtra("TitoloLista", ListaSelezionata);
                if(DescrizioneTEXT.equals("Descrizione non inserita dall' utente")) {
                    intent.putExtra("Descrizione", DescrizioneTEXT);
                }else{
                    intent.putExtra("Descrizione",DescrizioneTEXT);
                }
                intent.putExtra("TipoCorrente", "Lista");
                intent.putExtra("DiChiè", "Mia");
                startActivity(intent);
            }
        });
        ModDesc.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialogBilderDescrizione = new AlertDialog.Builder(getContext());
                final View PopUpViewDescrizione = getLayoutInflater().inflate(R.layout.cambia_descrizione_popup, null);
                Conferma = (AppCompatButton) PopUpViewDescrizione.findViewById(R.id.conferma_button_descrizone);
                Annulla = (AppCompatButton) PopUpViewDescrizione.findViewById(R.id.annulla_button_descrizione);
                NuovoAttributo =  PopUpViewDescrizione.findViewById(R.id.InsertNuovadescrizione);
                NuovoAttributoLayout =  PopUpViewDescrizione.findViewById(R.id.InsertNuovadescrizione_layout);
                dialogBilderDescrizione.setView(PopUpViewDescrizione);
                CambiaDescrizione = dialogBilderDescrizione.create();
                CambiaDescrizione.show();
                Conferma.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(NuovoAttributo.length() != 0){
                            Call<DBModelResponseToInsert> modificaDescrizione = retrofitServiceDBInterno.ModificaDescrizioneLista(UsernameProprietario, ListaSelezionata, NuovoAttributo.getText().toString());
                            modificaDescrizione.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")){
                                            Toast.makeText(getContext(), "Descrizione modificata correttamente.", Toast.LENGTH_SHORT).show();
                                            Fragment fragment = new FragmentPreferiti();
                                            bundle.putString("Username", UsernameProprietario);
                                            fragment.setArguments(bundle);
                                            loadFragment(fragment);
                                            CambiaDescrizione.dismiss();
                                        }else{
                                            Toast.makeText(getContext(), "Cambiamento descrizione fallito.", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(getContext(), "Impissibile cambiare descrizione.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(getContext(), "Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else{
                            NuovoAttributoLayout.setError("Inserisci descrizione");
                        }
                    }
                });
                Annulla.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        Toast.makeText(getContext(), "Cambiamento descriizone annullato.", Toast.LENGTH_SHORT).show();
                        CambiaDescrizione.dismiss();
                    }
                });
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
                    }
                }else{
                    Toast.makeText(getContext(), "Impossibile mostrare la Lista.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.my_nav_host_fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}