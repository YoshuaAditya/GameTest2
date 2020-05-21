package com.iboy.game.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iboy.game.R;
import com.iboy.game.main.AppConstants;

public class MainMenu extends Activity implements View.OnClickListener {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        //set all buttons onclick event
        ViewGroup group = findViewById(R.id.mainMenu);
        View v;
        for(int i = 0; i < group.getChildCount(); i++) {
            v = group.getChildAt(i);
            if(v instanceof Button) v.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bStartGame: startActivity(new Intent(this,GameActivity.class));
        }
    }
}
