package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.AttoriDetailActivity;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriResponseResults;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolderAttori;

import java.util.List;

public class AttoriSearchAdapter extends RecyclerView.Adapter<SearchViewHolderAttori> {

    private Activity activity;
    private List<AttoriResponseResults> results;

    public AttoriSearchAdapter(Activity activity, List<AttoriResponseResults> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull @Override public SearchViewHolderAttori onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_celebrity_search,viewGroup, false);
        return new SearchViewHolderAttori(view);
    }

    @Override public void onBindViewHolder(@NonNull SearchViewHolderAttori searchViewHolder, int i) {
        AttoriResponseResults responseResults = results.get(i);
        searchViewHolder.setPosterImageView(activity,responseResults.getProfile_path());
        String nome = responseResults.getName();
        String role = responseResults.getKnown_for_department();
        int id = responseResults.getId();
        if(nome != null){
            searchViewHolder.posterTitle.setVisibility(View.VISIBLE);
            searchViewHolder.posterTitle.setText(nome);
        }else{
            searchViewHolder.posterTitle.setVisibility(View.GONE);
        }
        if(role != null){
            searchViewHolder.ruolo.setVisibility(View.VISIBLE);
            searchViewHolder.ruolo.setText(role);
        }else{
            searchViewHolder.ruolo.setVisibility(View.GONE);
        }

        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, AttoriDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return results.size();
    }
}
