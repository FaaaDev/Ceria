package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ItemBankBinding;
import com.faadev.ceria.model.BankModel;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {
    private Context mContext;
    private List<BankModel> mData;
    private ItemCLickListener listener;
    private int before = -1;

    public BankAdapter(Context mContext, List<BankModel> mData, ItemCLickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(ItemBankBinding.inflate(LayoutInflater.from(mContext), parent, false));

        return  viewHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BankModel data = mData.get(position);
        if (data.isSelected()) {
            holder.binding.mainComponent.setBackgroundResource(R.drawable.bank_selected);
            holder.binding.label.setTextColor(mContext.getResources().getColor(R.color.fontColor));
        } else {
            holder.binding.mainComponent.setBackgroundResource(R.drawable.nominal_unselected);
            holder.binding.label.setTextColor(mContext.getResources().getColor(R.color.fontColorSecondary));
        }

        holder.binding.label.setText(data.getBankName());
        Glide.with(mContext)
                .load(data.getImage())
                .into(holder.binding.logo);

        holder.binding.item.setOnClickListener(v -> {
            if (before > -1) {
                if (position != before) {
                    data.setSelected(true);
                    mData.get(before).setSelected(false);
                    before = position;
                    listener.onItemClick(position);
                } else {
                    data.setSelected(false);
                    before = -1;
                    listener.onItemClick(before);
                }
                notifyDataSetChanged();
            } else {
                data.setSelected(true);
                before = position;
                notifyDataSetChanged();
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBankBinding binding;
        public ViewHolder(ItemBankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
