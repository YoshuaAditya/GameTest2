package com.iboy.game.gui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.iboy.game.R;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends Activity {

    final int VIEW_SEEKBAR = 0;
    final int VIEW_SWITCH = 1;

    SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    int funds = 0;

    TextView textViewFunds;

    List<ShopItem> shopItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        RecyclerView recyclerView = findViewById(R.id.shopList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(layoutManager);

        //TODO add here. then shops.update
        ShopItem item1 = new ShopItem("Gun", "Gun", "10000");
        ShopItem item2 = new ShopItem("Gun", "Option", "100000");
        ShopItem item3 = new ShopItem("Gun", "Life", "20000");
        shopItems.add(item1);
        shopItems.add(item2);
        shopItems.add(item3);

        ShopAdapter shopAdapter = new ShopAdapter(this, shopItems);

        recyclerView.setAdapter(shopAdapter);

        Button buttonApply = findViewById(R.id.bApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button buttonCancel = findViewById(R.id.bCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button buttonAds = findViewById(R.id.bAds);
        buttonAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO google ads
                funds += 10000;
                updateFunds(String.valueOf(funds));
            }
        });

        textViewFunds = findViewById(R.id.textPlayerFunds);
        updateFunds(String.valueOf(funds));
    }

    private void updateFunds(String funds) {
        textViewFunds.setText(funds);
    }

    void pay(String price){
        int value= Integer.parseInt(price);
        if(funds-value>0) {
            funds -= value;
            updateFunds(String.valueOf(funds));
        }
        else {
            Log.e("Shop","Not enough funds");
            //TODO funds disimpen di sharedpref, trus mulai coba ennemy kill gives funds
        }
    }

    private void savePreference() {
        editor = sharedPref.edit();
        editor.apply();
    }
}
