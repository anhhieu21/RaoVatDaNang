package com.dev.assignment.adapter;

import android.content.Context;
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
import com.dev.assignment.fragment.PetDetailFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    ArrayList<DataProduct> dataPets;
    Context context;

    public PetAdapter(ArrayList<DataProduct> dataPets, Context context) {
        this.dataPets = dataPets;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_pet, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = dataPets.get(position).getProductId();
        String name = dataPets.get(position).getName();
        Integer price = dataPets.get(position).getPrice();
        String image1 = dataPets.get(position).getImage_1();
        String detail = dataPets.get(position).getDetail();
        int lockPr = dataPets.get(position).getLockPr();
        int idUser = dataPets.get(position).getLockPr();
        holder.tvName.setText(dataPets.get(position).getName());
//        holder.tvPrice.setText(dataPets.get(position).getPrice()+" đ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(dataPets.get(position).getPrice()) + "đ");
//        holder.imvPet.setImageResource(dataPets.get(position).getImage());

        Picasso.get().load(dataPets.get(position).getImage_1())
                .placeholder(R.drawable.ic_load_24)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imvPet);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetDetailFragment petDetailFragment = new PetDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("productId", id);
                bundle.putString("name", name);
                bundle.putInt("price", price);
                bundle.putString("image", image1);
                bundle.putString("detail", detail);
                petDetailFragment.setArguments(bundle);
                Constants.PR_ID = String.valueOf(id);
                Constants.ID_USER_K = String.valueOf(idUser);
                Constants.PR_LOCK = String.valueOf(lockPr);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.flyMain, petDetailFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataPets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

