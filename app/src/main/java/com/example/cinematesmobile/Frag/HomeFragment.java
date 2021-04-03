package com.example.cinematesmobile.Frag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.MovieListHomeAdapter;
import com.example.cinematesmobile.Search.Adapters.UpcomingSearchAdapter;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.MovieResponseResults;
import com.example.cinematesmobile.Search.Model.PopularResponse;
import com.example.cinematesmobile.Search.Model.TopRatedResponse;
import com.example.cinematesmobile.Search.Model.UpcomingResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerViewPopular;
    private RecyclerView recyclerViewUpcoming, recyclerTopRated;
    private RetrofitService retrofitService;
    private MovieListHomeAdapter popularSearchAdapter, topRatedcomingSearchAdapter;
    private UpcomingSearchAdapter upcomingSearchAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        String lingua = "it-IT";
        recyclerViewPopular = v.findViewById(R.id.recyclerView);
        recyclerViewUpcoming = v.findViewById(R.id.upcoming_recycleView);
        recyclerTopRated = v.findViewById(R.id.recyclerView2);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        Call<UpcomingResponse> upcomingResponseCall = retrofitService.getUpcomingByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
        upcomingResponseCall.enqueue(new Callback<UpcomingResponse>() {
            @Override public void onResponse(@NonNull Call<UpcomingResponse> call,@NonNull Response<UpcomingResponse> response) {
                UpcomingResponse upcomingResponse = response.body();
                List<MovieResponseResults> upcomingResponseResults = upcomingResponse.getResults();
                recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                upcomingSearchAdapter = new UpcomingSearchAdapter(getActivity(), upcomingResponseResults);
                recyclerViewUpcoming.setAdapter(upcomingSearchAdapter);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                recyclerViewUpcoming.setLayoutAnimation(controller);
                recyclerViewUpcoming.scheduleLayoutAnimation();
            }
            @Override public void onFailure(@NonNull Call<UpcomingResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
            }
        });
        Call<PopularResponse> popularResponseCall = retrofitService.getPopularByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua);
        popularResponseCall.enqueue(new Callback<PopularResponse>() {
            @Override public void onResponse(@NonNull Call<PopularResponse> call,@NonNull  Response<PopularResponse> response) {
                PopularResponse popularResponse = response.body();
                List<MovieResponseResults> popularResponseResults = popularResponse.getResults();
                recyclerViewPopular.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                popularSearchAdapter = new MovieListHomeAdapter(getActivity(), popularResponseResults);
                recyclerViewPopular.setAdapter(popularSearchAdapter);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                recyclerViewPopular.setLayoutAnimation(controller);
                recyclerViewPopular.scheduleLayoutAnimation();
            }
            @Override public void onFailure(@NonNull Call<PopularResponse> call,@NonNull  Throwable t) {
                Toast.makeText(getActivity(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
            }
        });

        Call<TopRatedResponse> topRatedResponseCall = retrofitService.getTopRatedByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
        topRatedResponseCall.enqueue(new Callback<TopRatedResponse>() {
            @Override public void onResponse(@NonNull Call<TopRatedResponse> call,@NonNull Response<TopRatedResponse> response) {
                TopRatedResponse topRatedResponse = response.body();
                List<MovieResponseResults> topRatedResponseResults = topRatedResponse.getResults();
                recyclerTopRated.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                topRatedcomingSearchAdapter = new MovieListHomeAdapter(getActivity(), topRatedResponseResults);
                recyclerTopRated.setAdapter(topRatedcomingSearchAdapter);
                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_scorri_destra);
                recyclerTopRated.setLayoutAnimation(controller);
                recyclerTopRated.scheduleLayoutAnimation();
            }
            @Override public void onFailure(@NonNull Call<TopRatedResponse> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}