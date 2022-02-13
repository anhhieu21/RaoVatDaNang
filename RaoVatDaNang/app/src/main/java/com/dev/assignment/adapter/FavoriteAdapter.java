package com.dev.assignment.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.assignment.Constants;
import com.dev.assignment.Model.DataProduct;
import com.dev.assignment.R;
import com.dev.assignment.activity.FavoriteDetailActivity;
import com.dev.assignment.fragment.FavoriteDetailFragment;
import com.dev.assignment.fragment.PetDetailFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    ArrayList<DataProduct> dataPets;
    Context context;

    public FavoriteAdapter(ArrayList<DataProduct> dataPets, Context context) {
        this.dataPets = dataPets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_favorite,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = dataPets.get(position).getProductId();
        String name = dataPets.get(position).getName();
        Integer price = dataPets.get(position).getPrice();
        String image1 = dataPets.get(position).getImage_1();
        holder.tvName.setText(dataPets.get(position).getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(dataPets.get(position).getPrice()) + "đ");
        Picasso.get().load(dataPets.get(position).getImage_1())
                .placeholder(R.drawable.ic_load_24)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imvPet);
        //
        holder.itemView.setOnClickListener(view -> {
            Constants.PRICE_F= String.valueOf(price);
            Constants.PR_ID = String.valueOf(id);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Intent intent = new Intent(view.getContext(), FavoriteDetailActivity.class);
           view.getContext().startActivity(intent);
           ((AppCompatActivity) view.getContext()).finish();

        });
    }

    @Override
    public int getItemCount() {
        return dataPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvPrice;
        ImageView imvPet;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imvPet = itemView.findViewById(R.id.imv_pet);
        }
    }
}
