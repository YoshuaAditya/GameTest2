package com.iboy.game.objects;

import android.os.Handler;
import android.util.Log;

import com.iboy.game.main.AppConstants;

import java.sql.BatchUpdateException;
import java.util.List;

public class Bullet {
    int x, y;
    double speed=4;
    boolean willHit=false,isResolved=false;
    Enemy targetEnemy;
    int lastIndex=0;
    Handler handler=new Handler();

    public Bullet(int x, int y) {
        this.x=x;
        this.y=y;
    }
    public void checkHitbox(){
        //plan will be check a bullet into enemy list,
        // we will remember last index so we dont check again the previous, check if y same, then only check x
        if(lastIndex>GameEngine.enemyList.size()){
            lastIndex=lastIndex-(lastIndex-GameEngine.enemyList.size());
        }
        if(lastIndex<GameEngine.enemyList.size() && !willHit) {
            for (int i=lastIndex;i<GameEngine.enemyList.size();i++){
                Enemy enemy=GameEngine.enemyList.get(i);
                targetEnemy=enemy;
                boolean debug=Math.abs(y-enemy.y) < 2 && !enemy.willHit;
                Log.e("Checking","i:"+ i+" size:"+GameEngine.enemyList.size()
                        +" "+Bullet.this.toString()+" y check:"+debug);
//                Log.e("Checking",this.toString());
                if (debug) {
                    willHit = true;
                    enemy.willHit=true;
                    targetEnemy=enemy;
                    Log.e("Will Hit",Bullet.this.toString());
                    break;
                }
            }
            lastIndex=GameEngine.enemyList.size();
            //TODO still implementing collision logic
//            for (Enemy enemy : GameEngine.enemyList) {
//                if (y == enemy.y && !enemy.willHit) {
//                    willHit = true;
//                    enemy.willHit=true;
//                    targetEnemy=enemy;
//                    break;
//                }
//            }
        }
        else if(!isResolved && willHit){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("Hit",Bullet.this.toString());
                    GameEngine.bulletRemoveList.add(Bullet.this);
                    GameEngine.enemyRemoveList.add(targetEnemy);
                    Bullet.this.reset();
                }
            }, (long) ((targetEnemy.x-x)/(targetEnemy.speed+speed)*33));
            isResolved=true;
        }
        if (x== AppConstants.SCREEN_WIDTH)
            GameEngine.bulletRemoveList.add(Bullet.this);
    }

    private void reset() {
        willHit=isResolved=false;
    }

    @Override
    public String toString(){
        return " x:"+x+" y:"+y+" enemy x:"+targetEnemy.x+" enemy y:"+targetEnemy.y;
    }
}
