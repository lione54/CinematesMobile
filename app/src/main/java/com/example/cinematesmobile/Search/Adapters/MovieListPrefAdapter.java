package com.example.cinematesmobile.Search.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Model.DBModelDataFilms;
import com.example.cinematesmobile.Search.MovieDetailActivity;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.RandomTransitionGenerator;

import java.util.List;


public class MovieListPrefAdapter extends RecyclerView.Adapter<MovieListPrefAdapter.DataViewHolder> {


    private Context mCtx;
    private List<DBModelDataFilms> dataList;

    public MovieListPrefAdapter(Context mCtx, List<DBModelDataFilms> productList) {
        this.mCtx = mCtx;
        this.dataList = productList;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_preferiti, null);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        DBModelDataFilms data = dataList.get(position);
        Glide.with(mCtx).load(data.getImage()).into(holder.posterImageView);
        holder.posterTitle.setText(data.getTitolofilm());
        int id = data.getId_film();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mCtx, MovieDetailActivity.class);
                intent.putExtra("id", String.valueOf(id));
                mCtx.startActivity(intent);
            }
        });
    }

    @Override public int getItemCount() {
        return dataList.size();
    }

    class DataViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView posterImageView;
        public AppCompatTextView posterTitle;

        public DataViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.poster_image_view_preferiti);
            posterTitle = itemView.findViewById(R.id.titolo_poster_preferiti);
            RandomTransitionGenerator generator = new RandomTransitionGenerator(1000, new DecelerateInterpolator());
            posterImageView.setTransitionGenerator(generator);
        }
    }
}