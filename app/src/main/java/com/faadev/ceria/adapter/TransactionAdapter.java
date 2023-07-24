package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ItemTransactionBinding;
import com.faadev.ceria.http.response.PurchaseData;
import com.faadev.ceria.model.TransactionModel;
import com.faadev.ceria.screen.activity.DetailTransactionActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private Context mContext;
    private List<TransactionModel> mData;

    public TransactionAdapter(Context mContext, List<TransactionModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(mContext), parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
        TransactionModel data = mData.get(position);

        if (data.getType().equals("b")) {
            holder.binding.type.setText("Isi Ulang");
            holder.binding.status.setImageResource(R.drawable.ic_add_color);
        } else if (data.getType().equals("s")) {
            holder.binding.type.setText("Penarikan");
            holder.binding.status.setImageResource(R.drawable.ic_minus);
            holder.binding.coinAffected.setTextColor(Color.parseColor("#ff0000"));
        } else if (data.getType().equals("r") && data.getCoinAffected().contains("-")) {
            holder.binding.type.setText("Reward");
            holder.binding.status.setImageResource(R.drawable.ic_minus);
            holder.binding.coinAffected.setTextColor(Color.parseColor("#ff0000"));
        } else if (data.getType().equals("r") && data.getCoinAffected().contains("+")) {
            holder.binding.type.setText("Reward");
            holder.binding.status.setImageResource(R.drawable.ic_add_color);
        }

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            input.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = input.parse(data.getDate()+"Z");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.binding.date.setText(outputFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (data.getStatus() == 0) {
            holder.binding.statusBg.setBackgroundColor(Color.parseColor("#30FFB302"));
            holder.binding.status.setImageResource(R.drawable.ic_waiting);
        } else if (data.getStatus() == 1) {
            holder.binding.statusBg.setBackgroundColor(Color.parseColor("#3004BD00"));
            holder.binding.status.setImageResource(R.drawable.ic_confirm);
        } else if (data.getStatus() == 2) {
            holder.binding.statusBg.setBackgroundColor(Color.parseColor("#302DCCFF"));
        } else if (data.getStatus() == 3) {
            holder.binding.statusBg.setBackgroundColor(Color.parseColor("#30FF0076"));
            holder.binding.status.setImageResource(R.drawable.ic_reject);
        } else {
            holder.binding.statusBg.setBackgroundColor(Color.parseColor("#309EA7AD"));
            holder.binding.status.setImageResource(R.drawable.ic_cancel);
        }

        holder.binding.coinAffected.setText(data.getCoinAffected()+" Coin");

        holder.binding.item.setOnClickListener(v -> {
            if (!data.getType().equals("r")) {
                Intent intent = new Intent(mContext, DetailTransactionActivity.class);
                intent.putExtra("data", data);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemTransactionBinding binding;

        public ViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    
}
