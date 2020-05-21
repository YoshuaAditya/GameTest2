package com.iboy.game.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.iboy.game.R;
import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.main.AppConstants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/*
 * Stores all object references that relevant for the game display
 * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
 * */
public class GameEngine {
    /*MEMBERS*/
    static List<Enemy> enemyList;
    static List<Bullet> bulletList;
    static List<Enemy> enemyRemoveList=new ArrayList<>();
    static List<Bullet> bulletRemoveList=new ArrayList<>();
    static final Object _sync = new Object();
    static float _lastTouchedX = 100, _lastTouchedY = AppConstants.HALFSCREEN_HEIGHT;
    Player player = new Player();

    private int maxEnemys = 100;
    Handler removeEnemy = new Handler();

    Paint playerPaint = new Paint();
    Paint enemyPaint = new Paint();
    Context activityContext;
    Activity activity;

    private Handler repeatUpdateHandler = new Handler();
    boolean down = false, up = false;
    private final int REPEAT_DELAY = 50;

    DisplayThread displayThread;
    View pauseScreen;
    boolean isPaused=false;

    public GameEngine(Context context) {
        enemyList = new LinkedList<Enemy>();
        bulletList = new LinkedList<Bullet>();
        _paint = new Paint();
    }

    class RepetitiveUpdater implements Runnable {

        @Override
        public void run() {
            if (down) {
                player.y -= player.y_speed;
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
            } else if (up) {
                player.y += player.y_speed;
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setButtons() {
        pauseScreen = activity.findViewById(R.id.pauseScreen);
        final Button buttonUp = activity.findViewById(R.id.moveUp);
        final Button buttonDown = activity.findViewById(R.id.moveDown);
        final Button buttonShoot = activity.findViewById(R.id.buttonShoot);
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isPaused){
                    switch (event.getAction() & MotionEvent.ACTION_MASK ) {
                        case MotionEvent.ACTION_DOWN:
                            down = true;
                            repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                            break;
                        case MotionEvent.ACTION_UP:
                            down = false;
                            break;
                    }
                }
                return false;
            }
        });
        buttonDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isPaused) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            up = true;
                            repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                            break;
                        case MotionEvent.ACTION_UP:
                            up = false;
                            break;
                    }
                }
                return false;
            }
        });
        Button buttonPause = activity.findViewById(R.id.buttonPause);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //acquire permit means the thread arent allowed to run
                try {
                    displayThread.semaphore.acquire();
                    pauseScreen.setVisibility(View.VISIBLE);
                    isPaused=true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPaused) {
                    bulletList.add(new Bullet(player.x, player.y));
                }
            }
        });
        Button buttonResume = activity.findViewById(R.id.bResume);
        buttonResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if there are stopped threads, make them continue
                displayThread.semaphore.release();
                pauseScreen.setVisibility(View.INVISIBLE);
                isPaused=false;
            }
        });
    }

    Paint _paint;
    //TODO kok masih aneh ya, shot kedua kyke salah dari log e, gk nampil dari 0-100, cuma 0 sing ditampilno

    /**
     * Updates all relevant objects business logic
     */
    public void Update() {
        for (Bullet bullet : bulletList) {
            bullet.checkHitbox();
        }
        bulletList.removeAll(bulletRemoveList);
        bulletRemoveList=new ArrayList<>();
        for (Enemy enemy : enemyList) {
            enemy.checkXPosition();
        }
        enemyList.removeAll(enemyRemoveList);
        enemyRemoveList=new ArrayList<>();
    }

    public void setActivityContext(Context context, DisplayThread displayThread) {
        activityContext = context;
        activity = (Activity) context;
        this.displayThread = displayThread;

        enemyList.clear();
        bulletList.clear();
        player.reset();

        setButtons();
    }

    /**
     * Draws all objects according to their parameters
     *
     * @param canvas canvas on which will be drawn the objects
     */
    public void Draw(Canvas canvas) {
        DrawPlayer(canvas);
        DrawEnemys(canvas);
        DrawBullets(canvas);
    }

    private void DrawBullets(Canvas canvas) {
        synchronized (_sync) {
            for (Bullet bullet : bulletList) {
                Rect rect = new Rect();
                bullet.x += bullet.speed;
                rect.set(bullet.x, bullet.y, bullet.x + 4, bullet.y + 4);
                int color = ContextCompat.getColor(activityContext, R.color.white);
                Paint bulletPaint = new Paint();
                bulletPaint.setARGB(255, 255, 255, 255);
                canvas.drawRect(rect, bulletPaint);
            }
        }
    }

    private void DrawPlayer(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(player.x, player.y, player.x + 4, player.y + 4);
        int color = ContextCompat.getColor(activityContext, R.color.white);
//        guardAreaPaint.setARGB(128, 255, 255, 255);
        playerPaint.setColor(color);
        canvas.drawRect(rect, playerPaint);
    }

    private void DrawEnemys(Canvas canvas) {
        //check how many enemys in field, create if its not max limit)
        if (enemyList.size() < maxEnemys) {
            Random random = new Random();
            int randomY = random.nextInt(AppConstants.HALFSCREEN_HEIGHT) / 2;
            int finalRandomY = randomY * (random.nextBoolean() ? -1 : 1);
            enemyList.add(new Enemy(canvas.getWidth(), AppConstants.HALFSCREEN_HEIGHT - finalRandomY));
        }
        //draw all enemys
        synchronized (_sync) {
            //TODO Awas kmrn ada error concurrentmodificationexception disini, gk tahu kenapa itu
            for (Enemy enemy : enemyList) {
                Rect rect = new Rect();
                enemy.x -= enemy.speed;
                rect.set(enemy.x, enemy.y, enemy.x + 4, enemy.y + 4);
                int color = ContextCompat.getColor(activityContext, R.color.white);
                enemyPaint.setARGB(128, 255, 255, 255);
                canvas.drawRect(rect, enemyPaint);
            }
        }
    }

    /**
     * Sets previous touch coordinates
     *
     * @param x current touch x coordinate
     * @param y current touch y coordinate
     */
    public void SetLastTouch(float x, float y) {
        _lastTouchedX = x;
        _lastTouchedY = y;
    }
}
