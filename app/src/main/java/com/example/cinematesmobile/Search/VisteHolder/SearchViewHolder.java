package com.example.cinematesmobile.Search.VisteHolder;

import android.content.Context;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;
import com.squareup.picasso.Picasso;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    private KenBurnsView posterImageView;
    public AppCompatTextView posterTitle;
    public AppCompatTextView Genere, Trama;
    public AppCompatTextView Voto, VotoCinemates;

    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        posterImageView = itemView.findViewById(R.id.poster_image_view);
        posterTitle = itemView.findViewById(R.id.titolo_poster);
        Voto = itemView.findViewById(R.id.film_votazione_generale);
        Genere = itemView.findViewById(R.id.film_detail_genere);
        Trama = itemView.findViewById(R.id.film_search_trama);
        VotoCinemates = itemView.findViewById(R.id.film_votazione_cinemates);
        RandomTransitionGenerator generator = new RandomTransitionGenerator(1000, new DecelerateInterpolator());
        posterImageView.setTransitionGenerator(generator);
    }

    public void setPosterImageView(Context context, String Posterurl) {
        Picasso.with(context).load(Posterurl).into(posterImageView);
    }
}
