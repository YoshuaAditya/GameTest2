package com.iboy.game.objects;

import android.os.Handler;
import android.util.Log;

import com.iboy.game.main.AppConstants;

public class Enemy {
    int x, y;
    double speed=1;
    boolean willHit=false;

    public Enemy(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public void checkXPosition(){
        if(x==0){
            GameEngine.enemyRemoveList.add(Enemy.this);
            Log.e("Enemy bypass","yep");
        }
    }
}
