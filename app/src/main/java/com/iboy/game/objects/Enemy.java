package com.iboy.game.objects;

import android.graphics.Path;
import android.os.Handler;
import android.util.Log;

import com.iboy.game.gui.GameActivity;
import com.iboy.game.main.AppConstants;
import com.iboy.game.main.Options;

public class Enemy {
    int x, y;
    int enemySize= Options.enemySize;
    double speed=Options.enemySpeed;
    boolean willHit=false;
    int life= Options.enemyLife;
    int bulletIncoming=0;

    public Enemy(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void checkLife(){
        life--;
        if (life<1)
            willHit=true;
    }

    public void checkXPosition(){
        if(x<0){
            GameEngine.enemyRemoveList.add(Enemy.this);
            GameEngine.damage++;
//            Log.e("Enemy bypass","yep");
        }
    }
}
