package com.faadev.ceria.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryModel> mData;
    private int sebelum = 0;
    private ItemCLickListener itemCLickListener;
    private boolean isVertical = false;

    public CategoryAdapter(Context mContext, List<CategoryModel> mData, ItemCLickListener itemCLickListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.itemCLickListener = itemCLickListener;
    }

    public CategoryAdapter(Context mContext, List<CategoryModel> mData, ItemCLickListener itemCLickListener, boolean isVertical) {
        this.mContext = mContext;
        this.mData = mData;
        this.itemCLickListener = itemCLickListener;
        this.isVertical = isVertical;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (isVertical) {
            view = layoutInflater.inflate(R.layout.item_category_vertical, parent, false);
        } else {
            view = layoutInflater.inflate(R.layout.layout_category, parent, false);
        }

        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryModel data = mData.get(position);
        holder.categoryname.setText(data.getCategory());
        if (isVertical) {
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

                    itemCLickListener.onItemClick(position);
                }, 200);
            });
        } else {
            if (data.isSelected()){
                holder.body.setBackgroundResource(R.drawable.category_selected);
                holder.categoryname.setTextColor(Color.WHITE);
            } else {
                holder.body.setBackgroundResource(R.drawable.category_unselected);
                holder.categoryname.setTextColor(Color.argb(255, 58, 177, 164));
            }

            holder.body.setOnClickListener(v -> {
                if (position != sebelum){
                    data.setSelected(true);
                    mData.get(sebelum).setSelected(false);
                    sebelum = position;
                    notifyDataSetChanged();
                }

                if ("Semua Kategori".equals(data.getCategory())){
                    itemCLickListener.onItemClick(position);
                } else {
                    itemCLickListener.onItemClick(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout body;
        private TextView categoryname;
        private CardView item;
        private ImageView check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (isVertical){
                check = itemView.findViewById(R.id.selected_indicator);
                categoryname = itemView.findViewById(R.id.category_name);
                item = itemView.findViewById(R.id.item);
            } else {
                body = itemView.findViewById(R.id.body);
                categoryname = itemView.findViewById(R.id.categoryname);
            }
        }
    }
}
