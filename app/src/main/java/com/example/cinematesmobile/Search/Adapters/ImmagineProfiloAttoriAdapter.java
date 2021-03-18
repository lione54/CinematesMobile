package com.example.cinematesmobile.Search.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Model.AttoriImage;
import com.example.cinematesmobile.Search.Model.AttoriImmageResult;
import com.example.cinematesmobile.Search.VisteHolder.ImmagineProfiloSerchViewHolder;
import com.example.cinematesmobile.Search.VisualizzaImmaginiActivity;

import java.util.List;

public class ImmagineProfiloAttoriAdapter extends RecyclerView.Adapter<ImmagineProfiloSerchViewHolder> {

    private Activity activity;
    private List<AttoriImmageResult> profileImageList;

    public ImmagineProfiloAttoriAdapter(Activity activity, List<AttoriImmageResult> profileImageList) {
        this.activity = activity;
        this.profileImageList = profileImageList;
    }

    @NonNull @Override public ImmagineProfiloSerchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.profile_immage_layout, viewGroup,false);
        return new ImmagineProfiloSerchViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ImmagineProfiloSerchViewHolder holder, int i) {
        AttoriImmageResult imagesProfiles = profileImageList.get(i);
        holder.setImmagineProfilo(activity, imagesProfiles.getFile_path());
        holder.ImmagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(activity, VisualizzaImmaginiActivity.class);
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.ImmagineProfilo, ViewCompat.getTransitionName(holder.ImmagineProfilo));
                visualizzaImmagineintent.putExtra("image_url", imagesProfiles.getFile_path());
                activity.startActivity(visualizzaImmagineintent, compat.toBundle());
            }
        });
    }

    @Override public int getItemCount() {
        return profileImageList.size();
    }
}
