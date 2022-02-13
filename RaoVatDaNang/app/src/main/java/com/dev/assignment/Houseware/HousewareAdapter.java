package com.dev.assignment.Houseware;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.assignment.R;

import java.util.List;

public class HousewareAdapter extends RecyclerView.Adapter<HousewareAdapter.HousewareViewHolder> {
    private List<Houseware> mHouseware;

    public void setData(List<Houseware> mHouseware){
        notifyDataSetChanged();
    }

    public HousewareAdapter(List<Houseware> mHouseware) {
        this.mHouseware = mHouseware;
    }

    @NonNull
    @Override
    public HousewareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_houseware, parent, false);
        return new HousewareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HousewareViewHolder holder, int position) {
        Houseware houseware = mHouseware.get(position);

        if(houseware == null){
            return;
        }
        holder.imgHouseware.setImageResource(houseware.getResourceId());
        holder.tvtitle.setText(houseware.getTitle());
    }

    @Override
    public int getItemCount() {
        if(mHouseware!= null){
            return mHouseware.size();

        }
        return 0;
    }

    public class HousewareViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgHouseware;
        private TextView tvtitle;

        public HousewareViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHouseware = itemView.findViewById(R.id.img_houseware);
            tvtitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
