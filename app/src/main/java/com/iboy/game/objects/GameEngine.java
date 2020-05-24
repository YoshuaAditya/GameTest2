package com.iboy.game.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.iboy.game.R;
import com.iboy.game.gui.GameActivity;
import com.iboy.game.main.AppConstants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import io.github.controlwear.virtual.joystick.android.JoystickView;


/*
 * Stores all object references that relevant for the game display
 * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
 * */
public class GameEngine {
    /*MEMBERS*/
    static List<Enemy> enemyList;
    static List<Bullet> bulletList;
    static List<Enemy> enemyRemoveList = new ArrayList<>();
    static List<Bullet> bulletRemoveList = new ArrayList<>();
    static int damage = 0;
    static int score = 0;
    int level=1;
    static final Object _sync = new Object();
    static float _lastTouchedX = 100, _lastTouchedY = AppConstants.HALFSCREEN_HEIGHT;
    Player player = new Player();

    Paint playerPaint = new Paint();
    Paint enemyPaint = new Paint();
    GameActivity activity;

    private Handler repeatUpdateHandler = new Handler();
    boolean down = false, up = false;
    private final int REPEAT_DELAY = 50;
    public DisplayThread displayThread;
    public boolean isPaused = false;

    private int enemy_timer = 10;
    private int enemy_delay = 5;

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
        final Button buttonUp = activity.findViewById(R.id.moveUp);
        final Button buttonDown = activity.findViewById(R.id.moveDown);
        final Button buttonShoot = activity.findViewById(R.id.buttonShoot);
        buttonUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPaused) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
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
                if (!isPaused) {
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
                if (!isPaused) {
                //acquire permit means the thread arent allowed to run
                try {
                    displayThread.semaphore.acquire();
                    activity.pauseScreen.setVisibility(View.VISIBLE);
                    isPaused = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                }
            }
        });
        buttonShoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPaused) {
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
                activity.pauseScreen.setVisibility(View.INVISIBLE);
                isPaused = false;
            }
        });
        Button buttonQuit = activity.findViewById(R.id.bQuit);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayThread.semaphore.release();
                isPaused = false;
                activity.finish();
            }
        });
        Button buttonSpeed = activity.findViewById(R.id.buttonSpeed);
        buttonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.playerSpeed++;
                if(player.playerSpeed==5)player.playerSpeed=1;
            }
        });
        JoystickView joystick = activity.findViewById(R.id.joystick);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                if(!isPaused){
                    double cos = Math.cos(angle * (Math.PI / 180.0));
                    double sin = Math.sin(angle * (Math.PI / 180.0));
                    player.x += cos * strength /30 * player.playerSpeed;
                    player.y -= sin * strength /30 * player.playerSpeed;
                }
            }
        });
    }

    Paint _paint;

    /**
     * Updates all relevant objects business logic
     */
    public void Update() {
        for (int i = 0; i < bulletList.size(); i++) {
            Bullet bullet = bulletList.get(i);
            bullet.x += bullet.speed;
            bullet.checkHitbox();
        }
        bulletList.removeAll(bulletRemoveList);
        bulletRemoveList.clear();
        for (Enemy enemy : enemyList) {
            enemy.x -= enemy.speed;
            enemy.checkXPosition();
        }
        enemyList.removeAll(enemyRemoveList);
        enemyRemoveList.clear();
        //TODO hmm, enaknya gmn ya, biarin spam shot tapi dibuat susah, apa consequence dari too many shots.
        //TODO klo up down dipencet spesifik caranya, bisa nambah speed,cuma ini mau fitur apa bug
        //TODO implementasi options
        updateLife();
        updateScore();
        setButtons();
    }

    void updateScore() {
        if (score != 0) {
            int currentScore = Integer.parseInt(activity.textScore.getText().toString());
            final int updatedScore = currentScore + score;
            score = 0;

            //Win condition
            if(updatedScore>100*level){
                level++;
                enemy_delay--;
                if(enemy_delay<1){
                    enemy_delay=1;
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.textLevel.setText(String.format(Locale.ENGLISH, "%d", level));
                    }
                });
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.textScore.setText(String.format(Locale.ENGLISH, "%d", updatedScore));
                }
            });
        }
    }

    private void updateLife() {
        if (damage != 0) {
            int currentLife = Integer.parseInt(activity.textLife.getText().toString());
            final int updatedLife = currentLife-damage;
            damage = 0;

            //Defeat condition
            if(updatedLife<1){
                activity.finish();
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.textLife.setText(String.format(Locale.ENGLISH, "%d", updatedLife));
                }
            });
        }
    }


    public void setActivityContext(Context context, DisplayThread displayThread) {
        activity = (GameActivity) context;
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
            for (int i = 0; i < bulletList.size(); i++) {
                Bullet bullet = bulletList.get(i);
                Rect rect = new Rect();
                rect.set(bullet.x, bullet.y, bullet.x + bullet.bulletSize, bullet.y + bullet.bulletSize);
                int color = ContextCompat.getColor(activity, R.color.white);
                Paint bulletPaint = new Paint();
                bulletPaint.setARGB(255, 255, 255, 255);
                canvas.drawRect(rect, bulletPaint);
            }
        }
    }

    private void DrawPlayer(Canvas canvas) {
        Rect rect = new Rect();
        rect.set(player.x, player.y, player.x + player.playerSize, player.y + player.playerSize);
        int color = ContextCompat.getColor(activity, R.color.white);
//        guardAreaPaint.setARGB(128, 255, 255, 255);
        playerPaint.setColor(color);
        canvas.drawRect(rect, playerPaint);
    }

    private void DrawEnemys(Canvas canvas) {
        //check how many enemys in field, create if its not max limit)
        enemy_timer--;
        if (enemy_timer<1) {
            Random random = new Random();
            int randomY = random.nextInt(AppConstants.HALFSCREEN_HEIGHT) / 2;
            int finalRandomY = randomY * (random.nextBoolean() ? -1 : 1);
            enemyList.add(new Enemy(canvas.getWidth(), AppConstants.HALFSCREEN_HEIGHT - finalRandomY));
            enemy_timer=enemy_delay;
        }
        //draw all enemys
        synchronized (_sync) {
            for (Enemy enemy : enemyList) {
                Rect rect = new Rect();
                rect.set(enemy.x, enemy.y, enemy.x + enemy.enemySize, enemy.y + enemy.enemySize);
                int color = ContextCompat.getColor(activity, R.color.white);
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
