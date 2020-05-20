package com.iboy.game.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.main.AppConstants;

import android.os.Handler;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/*
 * Stores all object references that relevant for the game display
 * Calls objects business logic methods, and draw them to the given Canvas from DisplayThread
 * */
public class GameEngine {
    /*MEMBERS*/
    static List<Laser> laserList;
    static final Object _sync = new Object();
    static float _lastTouchedX, _lastTouchedY;

    private float halfscreenX = AppConstants.SCREEN_WIDTH / 2;
    private float halfscreenY = AppConstants.SCREEN_HEIGHT / 2;
    private int maxLasers = 1;
    Handler removeLaser = new Handler();

    public GameEngine() {
        laserList = new LinkedList<Laser>();
        _paint = new Paint();
    }

    Paint _paint;
    //TODO mulai buat logic game yang diinginkan: kalau terkena x bola player kalah(kasih logic nabrak + sfx)

    /**
     * Updates all relevant objects business logic
     */
    public void Update() {

    }


    /**
     * Draws all objects according to their parameters
     *
     * @param canvas canvas on which will be drawn the objects
     */
    public void Draw(Canvas canvas) {
        DrawShield(canvas);
        DrawLasers(canvas);
    }

    private void DrawLasers(Canvas canvas) {
        //check how many lasers in field, create if its not max limit)
        if (laserList.size() < maxLasers) {
            int random = new Random().nextInt(4);
            long durationMiliseconds = 2000;
            laserList.add(new Laser(4, random));
            removeLaser.postDelayed(new Runnable() {
                @Override
                public void run() {
                    laserList.remove(0);
                }
            }, durationMiliseconds);
        }
        //draw all lasers
        synchronized (_sync) {
            for (Laser laser : laserList) {
                Bitmap bitmap = AppConstants.GetBitmapsBank().GetRedBubble();
                canvas.drawBitmap(bitmap, (float) laser._x, (float) laser._y, _paint);
            }
        }
    }

    private void DrawShield(Canvas canvas) {
        Paint guardAreaPaint = new Paint();
        guardAreaPaint.setARGB(128, 255, 255, 255);
        float left = _lastTouchedX < halfscreenX ? 0 : halfscreenX;
        float top = _lastTouchedY < halfscreenY ? 0 : halfscreenY;
        canvas.drawRect(left, top, left + halfscreenX, top + halfscreenY, guardAreaPaint);
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
