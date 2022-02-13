package com.dev.assignment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Filter;
import android.widget.Filterable;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    ArrayList<DataProduct> dataProducts;
    ArrayList<DataProduct> dataProducts1;
    Context context;

    public ProductAdapter(ArrayList<DataProduct> dataProducts, Context context) {
        this.dataProducts = dataProducts;
        this.dataProducts1 = dataProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int productId = dataProducts.get(position).getProductId();
        String name = dataProducts.get(position).getName();
        Integer price = dataProducts.get(position).getPrice();
        String image1 = dataProducts.get(position).getImage_1();
        String detail = dataProducts.get(position).getDetail();
        int lockPr = dataProducts.get(position).getLockPr();
        int idUser = dataProducts.get(position).getIdUser();

        holder.tvName.setText(dataProducts.get(position).getName());
//        holder.tvPrice.setText(dataPets.get(position).getPrice()+" đ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText(decimalFormat.format(dataProducts.get(position).getPrice()) + "đ");
//        holder.imvPet.setImageResource(dataPets.get(position).getImage());

        Picasso.get().load(dataProducts.get(position).getImage_1())
                .placeholder(R.drawable.ic_load_24)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imvProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetDetailFragment petDetailFragment = new PetDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("productId", productId);
                bundle.putString("name", name);
                bundle.putInt("price", price);
                bundle.putString("image", image1);
                bundle.putString("detail", detail);
                petDetailFragment.setArguments(bundle);
                Constants.PR_LOCK = String.valueOf(lockPr);
                Constants.ID_USER_K = String.valueOf(idUser);
                Constants.PR_ID = String.valueOf(productId);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.flyMain, petDetailFragment).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataProducts.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString().trim();
                if (search.isEmpty()) {
                    dataProducts = dataProducts1;
                } else {
                    ArrayList<DataProduct> list = new ArrayList<>();
                    for (DataProduct dataProduct : dataProducts1) {
                        if (dataProduct.getName().toLowerCase().contains(search.toLowerCase())) {
                            list.add(dataProduct);
                        }
                    }
                    dataProducts = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataProducts;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataProducts = (ArrayList<DataProduct>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imvProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imvProduct = itemView.findViewById(R.id.imvProduct);
        }
    }


}

