package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieResponseResults;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.example.cinematesmobile.Search.VisteHolder.SearchViewHolder;

import java.util.List;

public class UpcomingSearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Activity activity;
    private List<MovieResponseResults> results;
    private String UserProprietario;

    public UpcomingSearchAdapter(Activity activity, List<MovieResponseResults> results, String userProprietario) {
        this.activity = activity;
        this.results = results;
        UserProprietario = userProprietario;
    }

    @NonNull @Override public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_ricerca_upcoming, viewGroup, false);
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
                intent.putExtra("UsernameProprietario", UserProprietario);
                activity.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return results.size();
    }
}
