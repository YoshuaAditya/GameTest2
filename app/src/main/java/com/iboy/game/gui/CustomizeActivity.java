package com.iboy.game.gui;

import android.app.Activity;
import android.content.ContentUris;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.iboy.game.R;
import com.iboy.game.main.Options;

import java.util.ArrayList;
import java.util.List;

public class CustomizeActivity extends Activity{

    final int VIEW_SEEKBAR=0;
    final int VIEW_SWITCH=1;

    SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    List<OptionModel> optionModels= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customize_activity);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        RecyclerView recyclerView = findViewById(R.id.optionList);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);

        //TODO add here. then options.update
        OptionModel opBulletSize=new OptionModel(getString(R.string.bulletSize),Options.bulletSize+"",VIEW_SEEKBAR);
        OptionModel opBulletSpeed=new OptionModel(getString(R.string.bulletSpeed),Options.bulletSpeed+"",VIEW_SEEKBAR);
        OptionModel opEnemySize=new OptionModel(getString(R.string.enemySize),Options.enemySize+"",VIEW_SEEKBAR);
        OptionModel opEnemySpeed=new OptionModel(getString(R.string.enemySpeed),Options.enemySpeed+"",VIEW_SEEKBAR);
        OptionModel opEnemyDelay=new OptionModel(getString(R.string.enemyDelay),Options.enemyDelay+"",VIEW_SEEKBAR);
        OptionModel opEnemyLife=new OptionModel(getString(R.string.enemyLife),Options.enemyLife+"",VIEW_SEEKBAR);
        OptionModel opLevel=new OptionModel(getString(R.string.level),Options.level+"",VIEW_SEEKBAR);
        OptionModel opFriendlyFire=new OptionModel(getString(R.string.friendlyFire),Options.friendlyFire+"",VIEW_SWITCH);
        optionModels.add(opBulletSize);
        optionModels.add(opBulletSpeed);
        optionModels.add(opEnemySize);
        optionModels.add(opEnemySpeed);
        optionModels.add(opEnemyDelay);
        optionModels.add(opEnemyLife);
        optionModels.add(opLevel);
        optionModels.add(opFriendlyFire);

        OptionAdapter optionAdapter=new OptionAdapter(this,optionModels);

        recyclerView.setAdapter(optionAdapter);

        Button buttonApply=findViewById(R.id.bApply);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreference();
                Options.updateOptions(CustomizeActivity.this);
                onBackPressed();
            }
        });

        Button buttonCancel=findViewById(R.id.bCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void savePreference() {
        editor = sharedPref.edit();
        for(OptionModel model:optionModels){
            editor.putString(model.optionName, model.value);
        }
        editor.apply();
    }
}
