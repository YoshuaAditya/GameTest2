package com.iboy.game.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iboy.game.R;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder>{
    private Context mCtx;
    private List<OptionModel> optionModelList;
    final int VIEW_SEEKBAR=0;
    final int VIEW_SWITCH=1;

    public OptionAdapter(Context mCtx, List<OptionModel> optionModelList) {
        this.mCtx = mCtx;
        this.optionModelList = optionModelList;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        OptionModel optionModel = optionModelList.get(i);
        View view = layoutInflater.inflate(R.layout.option_seekbar_layout, null);
        final OptionViewHolder holder = new OptionViewHolder(view);
        switch (optionModel.viewType){
            case VIEW_SEEKBAR:break;
            case VIEW_SWITCH:break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder optionViewHolder, int i) {
        OptionModel optionModel = optionModelList.get(i);
        optionViewHolder.textViewOption.setText(optionModel.optionName);
    }

    @Override
    public int getItemCount() {
        return optionModelList.size();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder{
        TextView textViewOption;
        public OptionViewHolder(View itemView) {
            super(itemView);
            textViewOption = itemView.findViewById(R.id.textViewOptionName);

        }
    }
}
