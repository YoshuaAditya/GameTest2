package com.iboy.game.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.iboy.game.R;

import java.util.ArrayList;
import java.util.List;


public class OptionSeekbarHolder extends OptionHolder<OptionSeekbarHolder.ViewHolder> {

    private List<String> mDataSet = new ArrayList();

    public OptionSeekbarHolder(OptionAdapter optionAdapter) {
        super(optionAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.option_seekbar_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        String title = mDataSet.get(position);
        holder.mTitleText.setText(title);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setDataSet(List<String> dataSet) {
        mDataSet.addAll(dataSet);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        SeekBar seekBar;

        public ViewHolder(View view) {
            super(view);
            mTitleText = view.findViewById(R.id.textViewOptionName);
            seekBar = view.findViewById(R.id.seekbarOption);
        }
    }
}