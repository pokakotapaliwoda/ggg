package com.example.piotrnikadonzaliczeniowy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedHashMap;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final String TAG = "1111";

    Context context;
    LinkedHashMap<Integer,Object[]> dataset;

    public ItemAdapter(Context context, LinkedHashMap<Integer,Object[]> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    public void updateData(LinkedHashMap<Integer,Object[]> dataset) {
        this.dataset = dataset;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Object[] itemData = dataset.get(position);
        if(itemData != null) {
            holder.itemTitle.setText(itemData[2]+"");
            holder.itemPrice.setText(itemData[3]+" zł");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemActivity.class);
                    intent.putExtra("itemId", itemData[0]+"");
                    intent.putExtra("userId", itemData[1]+"");
                    intent.putExtra("title", itemData[2]+"");
                    intent.putExtra("price", itemData[3]+"");
                    intent.putExtra("description", itemData[4]+"");
                    intent.putExtra("category", itemData[5]+"");
                    intent.putExtra("date", itemData[6]+"");
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemPrice;
        public ItemViewHolder(@NonNull View view) {
            super(view);
            itemTitle = view.findViewById(R.id.item_title);
            itemPrice = view.findViewById(R.id.item_price);
        }
    }
}
