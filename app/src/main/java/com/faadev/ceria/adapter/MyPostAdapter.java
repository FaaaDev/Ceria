package com.faadev.ceria.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.databinding.ItemPostBinding;
import com.faadev.ceria.model.MyPost;
import com.faadev.ceria.screen.activity.DetailMyPostActivity;

import java.util.List;


public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.ViewHolder> {
    private Context mContext;
    private List<MyPost> mData;

    public MyPostAdapter(Context mContext, List<MyPost> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyPostAdapter.ViewHolder viewHolder = new ViewHolder(ItemPostBinding.inflate(LayoutInflater.from(mContext), parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyPost data = mData.get(position);

        holder.binding.tittle.setText(data.getTittle());
        holder.binding.content.setText(data.getArticle());
        holder.binding.category.setText(data.getCategory().getCategory());
        holder.binding.reward.setText(data.getTotalReward() + " Coin");
        holder.binding.totalLike.setText(String.valueOf(data.getLikes()));

        int status = data.getApprove();
        if (status == 1) {
            holder.binding.status.setBackgroundColor(Color.parseColor("#302DCCFF"));
            holder.binding.statusText.setTextColor(Color.parseColor("#2DCCFF"));
            holder.binding.statusText.setText("Dipublikasi");
        } else if (status == 0) {
            holder.binding.status.setBackgroundColor(Color.parseColor("#3004BD00"));
            holder.binding.statusText.setTextColor(Color.parseColor("#04BD00"));
            holder.binding.statusText.setText("Ditinjau");
        } else {
            holder.binding.status.setBackgroundColor(Color.parseColor("#30FF0076"));
            holder.binding.statusText.setTextColor(Color.parseColor("#FF0076"));
            holder.binding.statusText.setText("Ditolak");
        }

        if (data.isMonetized()) {
            holder.binding.monetize.setImageResource(R.drawable.ic_round_monetization_on_green);
        }

        holder.binding.item.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailMyPostActivity.class);
            intent.putExtra("data", data);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;
        public ViewHolder(ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
