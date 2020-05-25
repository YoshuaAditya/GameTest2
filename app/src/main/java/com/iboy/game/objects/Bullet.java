package com.iboy.game.objects;

import android.os.Handler;
import android.util.Log;

import com.iboy.game.gui.GameActivity;
import com.iboy.game.main.AppConstants;
import com.iboy.game.main.Options;

import java.sql.BatchUpdateException;
import java.util.List;

public class Bullet {
    int x, y;
    int bulletSize = Options.bulletSize;
    double speed = 4;
    boolean willHit = false, isFriendlyFire = false;
    Enemy targetEnemy;
    int lastIndex = 0;
    Handler handler = new Handler();

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void checkHitbox() {
        for (int i = 0; i < GameEngine.enemyList.size(); i++) {
            Enemy enemy = GameEngine.enemyList.get(i);
            if(Math.abs(y-enemy.y)<bulletSize){
                if(Math.abs(x - enemy.x)<bulletSize){
                    GameEngine.bulletRemoveList.add(Bullet.this);
                    GameEngine.score++;
                    enemy.life--;
                    enemy.enemySize/=2;
                    if(enemy.life<1){
                        GameEngine.enemyRemoveList.add(enemy);
                    }
                    break;
                }
            }
        }

        //TODO nanti tambah opsi buat bullet friendly fire apa ndak
        if (x == AppConstants.SCREEN_WIDTH) {
            GameEngine.bulletRemoveList.add(Bullet.this);
            if (isFriendlyFire) {
                GameEngine.damage++;
            }
        }
    }
    @Override
    public String toString() {
        return " x:" + x + " y:" + y + " enemy x:" + targetEnemy.x + " enemy y:" + targetEnemy.y;
    }
}
