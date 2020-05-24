package com.iboy.game.gui;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;


abstract public class OptionHolder<T extends RecyclerView.ViewHolder> {

    private OptionAdapter mDataBindAdapter;

    public OptionHolder(OptionAdapter dataBindAdapter) {
        mDataBindAdapter = dataBindAdapter;
    }

    abstract public T newViewHolder(ViewGroup parent);

    abstract public void bindViewHolder(T holder, int position);

    abstract public int getItemCount();

}