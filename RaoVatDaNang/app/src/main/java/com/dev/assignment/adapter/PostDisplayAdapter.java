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
import com.dev.assignment.fragment.PostDisplayDetailFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PostDisplayAdapter extends RecyclerView.Adapter<PostDisplayAdapter.ViewHolder> {

    ArrayList<DataProduct> dataPostDisplay;
    Context context;

    public PostDisplayAdapter(ArrayList<DataProduct> dataPostDisplay, Context context) {
        this.dataPostDisplay = dataPostDisplay;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_post_display, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int id = dataPostDisplay.get(position).getProductId();
        String type1 = dataPostDisplay.get(position).getType1();
        String type2 = dataPostDisplay.get(position).getType2();
        String name = dataPostDisplay.get(position).getName();
        Integer price = dataPostDisplay.get(position).getPrice();
        String image_1 = dataPostDisplay.get(position).getImage_1();
        String detail = dataPostDisplay.get(position).getDetail();
        int idU = dataPostDisplay.get(position).getIdUser();
        holder.tvName.setText(dataPostDisplay.get(position).getName());
//        holder.tvPrice.setText(dataPets.get(position).getPrice()+" đ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(dataPostDisplay.get(position).getPrice()) + "đ");
//        holder.imvPet.setImageResource(dataPets.get(position).getImage());

        Picasso.get().load(dataPostDisplay.get(position).getImage_1())
                .placeholder(R.drawable.ic_load_24)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDisplayDetailFragment postDisplayDetailFragment = new PostDisplayDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type1", type1);
                bundle.putString("type2", type2);
                bundle.putString("name", name);
                bundle.putInt("price", price);
                bundle.putString("image_1", image_1);
                bundle.putString("detail", detail);
                postDisplayDetailFragment.setArguments(bundle);
                Constants.URL_IG = image_1;
                Constants.PR_ID_YOUR = String.valueOf(id);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.flyMain, postDisplayDetailFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataPostDisplay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imv = itemView.findViewById(R.id.imv);
        }
    }
}

