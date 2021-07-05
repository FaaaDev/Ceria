package com.faadev.ceria.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faadev.ceria.R;
import com.faadev.ceria.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryModel> mData;
    private int sebelum = 0;
    private ItemCLickListener itemCLickListener;

    public CategoryAdapter(Context mContext, List<CategoryModel> mData, ItemCLickListener itemCLickListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.itemCLickListener = itemCLickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.layout_category, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.categoryname.setText(mData.get(position).getCategory());
        if (mData.get(position).isSelected()){
            holder.body.setBackgroundResource(R.drawable.category_selected);
            holder.categoryname.setTextColor(Color.WHITE);
        } else {
            holder.body.setBackgroundResource(R.drawable.category_unselected);
            holder.categoryname.setTextColor(Color.argb(255, 58, 177, 164));
        }

        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != sebelum){
                    mData.get(position).setSelected(true);
                    mData.get(sebelum).setSelected(false);
                    sebelum = position;
                    notifyDataSetChanged();
                }

                if ("Semua Kategori".equals(mData.get(position).getCategory())){
                    itemCLickListener.onItemClick("");
                } else {
                    itemCLickListener.onItemClick(mData.get(position).getCategory());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout body;
        private TextView categoryname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            body = itemView.findViewById(R.id.body);
            categoryname = itemView.findViewById(R.id.categoryname);
        }
    }
}
