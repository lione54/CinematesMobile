package com.example.cinematesmobile.Search.VisteHolder;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinematesmobile.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchViewHolderAttori extends RecyclerView.ViewHolder {

    private CircleImageView posterImageView;
    public AppCompatTextView posterTitle;
    public AppCompatTextView ruolo;

    public SearchViewHolderAttori(@NonNull View itemView) {
        super(itemView);
        posterImageView = itemView.findViewById(R.id.image_celebrity);
        posterTitle = itemView.findViewById(R.id.nome_attore);
        ruolo = itemView.findViewById(R.id.ruolo_attore);
    }

    public void setPosterImageView(Context context, String Posterurl) {
        if(Posterurl != null){
            Picasso.with(context).load(Posterurl).into(posterImageView);
        }else{
            posterImageView.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }
    }
}
