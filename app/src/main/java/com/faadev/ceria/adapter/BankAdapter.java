package com.faadev.ceria.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.databinding.ItemBankBinding;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {
    Context mContext;

    public BankAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(ItemBankBinding.inflate(LayoutInflater.from(mContext), parent, false));

        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemBankBinding binding;
        public ViewHolder(ItemBankBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
