package com.trafficanalysics.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trafficanalysics.LocationApplication;
import com.trafficanalysics.R;
import com.trafficanalysics.modal.ArrayFavorite;
import com.trafficanalysics.modal.Favorite;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private ArrayFavorite favorites = LocationApplication.getInstance().arrFavotites;

    private int rowLayout;
    private Context mContext;


    public FavoritesAdapter(int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.mContext = context;
    }


    public void clearData() {
        if (favorites != null)
            favorites.getArrFavorites().clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Favorite favo = favorites.getArrFavorites().get(position);

        holder.address.setText(favo.getAddress());
        holder.title.setText(favo.getTitle());

        holder.btnFavorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                favorites.getArrFavorites().remove(position);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return favorites == null ? 0 : favorites.getArrFavorites().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView address;
        ImageView btnFavorite;

        public ViewHolder(View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            address = itemView.findViewById(R.id.txtAddress);
            btnFavorite = itemView.findViewById(R.id.btnFavoriteItem);
        }
    }

}
