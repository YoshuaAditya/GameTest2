package com.iboy.game.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iboy.game.R;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {
    private Context mCtx;
    private List<OptionModel> optionModelList;
    final int VIEW_SEEKBAR = 0;
    final int VIEW_SWITCH = 1;
    int position = 0;

    public OptionAdapter(Context mCtx, List<OptionModel> optionModelList) {
        this.mCtx = mCtx;
        this.optionModelList = optionModelList;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        final OptionModel optionModel = optionModelList.get(position++);
        View view;
        final OptionViewHolder holder;

        switch (optionModel.viewType) {
            case VIEW_SEEKBAR:
                view = layoutInflater.inflate(R.layout.option_seekbar_layout, null);
                holder = new OptionViewHolder(view);
                holder.seekBar = view.findViewById(R.id.seekbarOption);
                holder.seekBar.setProgress(Integer.parseInt(optionModel.value));
                holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        String value = progress + "";
                        optionModel.value = value;
                        holder.textViewValue.setText(value);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;
            default:
                view = layoutInflater.inflate(R.layout.option_switch_layout, null);
                holder = new OptionViewHolder(view);
                holder.aSwitch = view.findViewById(R.id.switchOption);
                if (optionModel.value.equals("true"))
                    holder.aSwitch.setChecked(true);
                holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String value = isChecked + "";
                        optionModel.value = value;
                        holder.textViewValue.setText(value);
                    }
                });
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder optionViewHolder, int i) {
        OptionModel optionModel = optionModelList.get(i);
        optionViewHolder.textViewOption.setText(optionModel.optionName);
        optionViewHolder.textViewValue.setText(optionModel.value);
    }

    @Override
    public int getItemCount() {
        return optionModelList.size();
    }

    class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOption;
        TextView textViewValue;
        SeekBar seekBar;
        Switch aSwitch;

        public OptionViewHolder(View itemView) {
            super(itemView);
            textViewOption = itemView.findViewById(R.id.textViewOptionName);
            textViewValue = itemView.findViewById(R.id.optionValue);
        }
    }
}
