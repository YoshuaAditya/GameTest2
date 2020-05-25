package com.iboy.game.gui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iboy.game.R;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    private ShopActivity shopActivity;
    private List<ShopItem> shopItemList;
    final int VIEW_SEEKBAR = 0;
    final int VIEW_SWITCH = 1;
    int position = 0;

    public ShopAdapter(ShopActivity shopActivity, List<ShopItem> shopItemList) {
        this.shopActivity = shopActivity;
        this.shopItemList = shopItemList;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(shopActivity);
        final ShopItem shopItem = shopItemList.get(position++);
        View view;
        final ShopViewHolder holder;
        view = layoutInflater.inflate(R.layout.shop_item_layout, null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopActivity.pay(shopItem.price);
            }
        });

        holder = new ShopViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int i) {
        ShopItem shopItem = shopItemList.get(i);
        shopViewHolder.textViewPrice.setText(shopItem.price);
        shopViewHolder.textViewName.setText(shopItem.name);
    }

    @Override
    public int getItemCount() {
        return shopItemList.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewPrice;
        TextView textViewName;

        public ShopViewHolder(View itemView) {
            super(itemView);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewName = itemView.findViewById(R.id.textViewItemName);
            imageView = itemView.findViewById(R.id.shop_item_image);
        }
    }
}
