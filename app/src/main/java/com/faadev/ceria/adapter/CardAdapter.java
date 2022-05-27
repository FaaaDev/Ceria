package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.faadev.ceria.databinding.HorizontalCardLayoutBinding;
import com.faadev.ceria.model.Post;
import com.faadev.ceria.screen.activity.AuthActivity;
import com.faadev.ceria.screen.activity.DetailPostActivity;
import com.faadev.ceria.utils.GlideApp;
import com.faadev.ceria.utils.Preferences;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mData;

    public CardAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final CardAdapter.ViewHolder viewHolder = new CardAdapter.ViewHolder(HorizontalCardLayoutBinding.inflate(LayoutInflater.from(mContext),
                parent, false));

        return viewHolder;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Post data = mData.get(position);

        holder.binding.card.setOnClickListener(view -> {
            if (Preferences.isLogedIn(mContext)){
                Intent intent = new Intent(mContext, DetailPostActivity.class);
                intent.putExtra("data", data);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                mContext.startActivity(new Intent(mContext, AuthActivity.class));
            }
        });

        if (position == 1) {
            holder.binding.card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
        }

        if (position == 0) {
            holder.binding.card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        }

        if (position % 2 == 0 && position > 0) {
            holder.binding.card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        }

        if (position % 3 == 0 && position > 0) {
            holder.binding.card.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 850));
        }

        holder.binding.tittle.setText(data.getTittle());

        holder.binding.writer.setText(data.getWriter());

        Glide.with(mContext)
                .load(data.getIllustration())
                .into(holder.binding.illustration);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private HorizontalCardLayoutBinding binding;

        public ViewHolder(HorizontalCardLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
