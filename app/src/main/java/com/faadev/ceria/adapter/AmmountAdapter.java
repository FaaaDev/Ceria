package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ItemNominalBinding;
import com.faadev.ceria.model.NominalModel;

import java.util.List;

public class AmmountAdapter extends RecyclerView.Adapter<AmmountAdapter.ViewHolder> {

    private Context mContext;
    private List<NominalModel> mData;
    private int sebelum = -1;
    private ItemCLickListener listener;

    public AmmountAdapter(Context mContext, List<NominalModel> mData, ItemCLickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(ItemNominalBinding.inflate(LayoutInflater.from(mContext), parent, false));
        return viewHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NominalModel data = mData.get(position);
        if (data.isSelected()) {
            holder.binding.item.setBackgroundResource(R.drawable.nominal_selected);
            holder.binding.ammount.setTextColor(Color.WHITE);
        } else {
            holder.binding.item.setBackgroundResource(R.drawable.nominal_unselected);
            holder.binding.ammount.setTextColor(mContext.getResources().getColor(R.color.fontColor));
        }

        holder.binding.ammount.setText(data.getNominal()/1000+ " rb");

        holder.binding.item.setOnClickListener(v -> {
            if (sebelum > -1) {
                if (position != sebelum) {
                    data.setSelected(true);
                    mData.get(sebelum).setSelected(false);
                    sebelum = position;
                    listener.onItemClick(position);
                } else {
                    data.setSelected(false);
                    sebelum = -1;
                    listener.onItemClick(sebelum);
                }
                notifyDataSetChanged();
            } else {
                data.setSelected(true);
                sebelum = position;
                notifyDataSetChanged();
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ItemNominalBinding binding;

        public ViewHolder(ItemNominalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
