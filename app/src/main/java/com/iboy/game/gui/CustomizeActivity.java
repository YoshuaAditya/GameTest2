package com.iboy.game.gui;

import android.app.Activity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.iboy.game.R;
import com.iboy.game.main.Options;

import java.util.ArrayList;
import java.util.List;

public class CustomizeActivity extends Activity{

    final int VIEW_SEEKBAR=0;
    final int VIEW_SWITCH=1;

    List<OptionModel> optionModels= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_activity);
        RecyclerView recyclerView = findViewById(R.id.optionList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.COLUMN);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        recyclerView.setLayoutManager(layoutManager);

        //TODO setidake recycler view e wis iso, cuma perlu dicek lagi
        OptionModel opBulletSize=new OptionModel("Bullet Size",Options.bulletSize+"",VIEW_SEEKBAR);
        OptionModel opFriendlyFire=new OptionModel("Friendly Fire",Options.friendlyFire+"",VIEW_SWITCH);
        optionModels.add(opBulletSize);
        optionModels.add(opFriendlyFire);

        OptionAdapter optionAdapter=new OptionAdapter(this,optionModels);

        recyclerView.setAdapter(optionAdapter);
    }
}
