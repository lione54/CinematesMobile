package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.AttoriDetailActivity;
import com.example.cinematesmobile.Search.Model.AttoriResponseResults;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolder;

import java.util.List;

public class AttoriSearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Activity activity;
    private List<AttoriResponseResults> results;

    public AttoriSearchAdapter(Activity activity, List<AttoriResponseResults> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull @Override public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_ricerca,viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        AttoriResponseResults responseResults = results.get(i);
        searchViewHolder.setPosterImageView(activity,responseResults.getProfile_path());
        String nome = responseResults.getName();
        int id = responseResults.getId();
        if(nome != null){
            searchViewHolder.posterTitle.setVisibility(View.VISIBLE);
            searchViewHolder.posterTitle.setText(nome);
        }else{
            searchViewHolder.posterTitle.setVisibility(View.GONE);
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
