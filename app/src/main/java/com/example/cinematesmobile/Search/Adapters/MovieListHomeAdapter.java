package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Model.MovieResponseResults;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolder;

import java.util.List;

public class MovieListHomeAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    private Activity activity;
    private List<MovieResponseResults> results;

    public MovieListHomeAdapter(Activity activity, List<MovieResponseResults> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull
    @Override public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_list_movie_home, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        MovieResponseResults responseResults = results.get(i);
        searchViewHolder.setPosterImageView(activity, responseResults.getPoster_path());
        String title = responseResults.getTitle();
        int id = responseResults.getId();
        if(title != null){
            searchViewHolder.posterTitle.setVisibility(View.VISIBLE);
            searchViewHolder.posterTitle.setText(title);
        }else{
            searchViewHolder.posterTitle.setVisibility(View.GONE);
        }
        searchViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return results.size();
    }
}