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
        //plan will be check a bullet into enemy list,
        // we will remember last index so we dont check again the previous, check if y same, then only check x
        if (lastIndex > GameEngine.enemyList.size()) {
            lastIndex = lastIndex - (lastIndex - GameEngine.enemyList.size());
        }
        if (lastIndex < GameEngine.enemyList.size() && !willHit) {
            for (int i = lastIndex; i < GameEngine.enemyList.size(); i++) {
                Enemy enemy = GameEngine.enemyList.get(i);
                targetEnemy = enemy;
//                Log.e("Checking", "i:" + i + " size:" + GameEngine.enemyList.size()
//                        + " " + Bullet.this.toString() + " y check:" + debug);
//                Log.e("Checking",this.toString());
                if (willHitFormula2(enemy)) {
                    willHit = true;
                    targetEnemy = enemy;
                    targetEnemy.bulletIncoming++;
                    if(targetEnemy.bulletIncoming==targetEnemy.life)
                        targetEnemy.willHit=true;
//                    Log.e("Will Hit", Bullet.this.toString());
                    break;
                }
            }
            lastIndex = GameEngine.enemyList.size();
        } else if (willHit) {
            if (x + bulletSize> targetEnemy.x) {
//                Log.e("Hit", Bullet.this.toString());&& x - targetEnemy.x < bulletSize * speed + targetEnemy.enemySize * targetEnemy.speed
//                damageFormula1();
                damageFormula2();
                GameEngine.bulletRemoveList.add(Bullet.this);
                GameEngine.score++;
                Bullet.this.reset();
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

    private void damageFormula2() {
        targetEnemy.life--;
        targetEnemy.enemySize--;
        if(targetEnemy.life<1) {
            GameEngine.enemyRemoveList.add(targetEnemy);
            targetEnemy.willHit = false;
        }
    }

    private void damageFormula1() {
        GameEngine.enemyRemoveList.add(targetEnemy);
    }

    private void reset() {
        willHit = false;
    }

    public boolean willHitFormula1(Enemy enemy){
        return Math.abs(y - enemy.y) < bulletSize + enemy.enemySize && !enemy.willHit;
    }

    public boolean willHitFormula2(Enemy enemy){
        return Math.abs(y - enemy.y) < bulletSize && !enemy.willHit && enemy.x>x;
    }

    @Override
    public String toString() {
        return " x:" + x + " y:" + y + " enemy x:" + targetEnemy.x + " enemy y:" + targetEnemy.y;
    }
}
