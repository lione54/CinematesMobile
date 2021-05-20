package com.example.cinematesmobile.Frag.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.AttoriDetailActivity;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriResponseResults;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttoriPopularAdapter extends RecyclerView.Adapter<AttoriPopularAdapter.SearchViewHolderAttoriHome> {

    private Activity activity;
    private List<AttoriResponseResults> results;

    public AttoriPopularAdapter(Activity activity, List<AttoriResponseResults> results) {
        this.activity = activity;
        this.results = results;
    }

    @NonNull @Override public SearchViewHolderAttoriHome onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_card_celebrity_home, viewGroup, false);
        return new SearchViewHolderAttoriHome(view);
    }

    @Override public void onBindViewHolder(@NonNull SearchViewHolderAttoriHome searchViewHolder, int i) {
        AttoriResponseResults responseResults = results.get(i);
        searchViewHolder.setPosterImageView(activity, responseResults.getProfile_path());
        String nome = responseResults.getName();
        int id = responseResults.getId();
        if (nome != null) {
            searchViewHolder.posterTitle.setVisibility(View.VISIBLE);
            searchViewHolder.posterTitle.setText(nome);
        } else {
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

    public class SearchViewHolderAttoriHome extends RecyclerView.ViewHolder {

        private CircleImageView posterImageView;
        public AppCompatTextView posterTitle;

        public SearchViewHolderAttoriHome(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.image_celebrity_home);
            posterTitle = itemView.findViewById(R.id.nome_attore_home);
        }

        public void setPosterImageView(Context context, String Posterurl) {
            if(Posterurl != null) {
                Picasso.with(context).load(Posterurl).into(posterImageView);
            }else{
                posterImageView.setImageResource(R.drawable.ic_director_chair);
            }
        }
    }

}