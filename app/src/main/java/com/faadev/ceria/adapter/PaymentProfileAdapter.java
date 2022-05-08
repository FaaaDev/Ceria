package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.model.CategoryModel;
import com.faadev.ceria.model.PaymentProfileModel;

import java.util.List;

public class PaymentProfileAdapter extends RecyclerView.Adapter<PaymentProfileAdapter.ViewHolder> {

    private Context mContext;
    private List<PaymentProfileModel> mData;
    private int sebelum = 0;
    private ItemCLickListener itemCLickListener;



    public PaymentProfileAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public PaymentProfileAdapter(Context mContext, List<PaymentProfileModel> mData, ItemCLickListener itemCLickListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.itemCLickListener = itemCLickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.item_payment_profile, parent, false);

        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PaymentProfileModel data = mData.get(position);
        holder.profile_bank.setText(data.getBank().getBankName());
        holder.profile_detail.setText(data.getBank_number()+" a.n "+data.getBank_alias());

        if (!data.isSelected()){
            holder.check.setImageResource(R.drawable.ic_round_uncheck_circle_24);
        } else {
            holder.check.setImageResource(R.drawable.ic_round_check_circle_24);
            sebelum = position;
        }


        holder.item.setOnClickListener(v -> {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (position != sebelum){
                    data.setSelected(true);
                    mData.get(sebelum).setSelected(false);
                    sebelum = position;
                    notifyDataSetChanged();
                } else {
                    data.setSelected(true);
                    sebelum = position;
                    notifyDataSetChanged();
                }

                itemCLickListener.onItemClick(data.getId());
            }, 200);
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView profile_bank, profile_detail;
        private CardView item;
        private ImageView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            check = itemView.findViewById(R.id.selected_indicator);
            profile_bank = itemView.findViewById(R.id.profile_bank);
            profile_detail = itemView.findViewById(R.id.profile_detail);
            item = itemView.findViewById(R.id.item);
        }
    }
}
