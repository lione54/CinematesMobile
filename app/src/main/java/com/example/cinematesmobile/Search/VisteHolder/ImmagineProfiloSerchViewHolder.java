package com.example.cinematesmobile.Search.VisteHolder;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.squareup.picasso.Picasso;

public class ImmagineProfiloSerchViewHolder extends RecyclerView.ViewHolder {

    public AppCompatImageView ImmagineProfilo;

    public ImmagineProfiloSerchViewHolder(@NonNull View itemView) {
        super(itemView);
        ImmagineProfilo = itemView.findViewById(R.id.immagine_profilo);
    }
    public void setImmagineProfilo(Activity activity, String profilePath){
        Picasso.with(activity).load(profilePath).into(ImmagineProfilo);
    }
}
