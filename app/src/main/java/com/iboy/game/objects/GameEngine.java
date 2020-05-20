package com.iboy.game.objects;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.handlers.RotationHandler;
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
    static final int BUBBLE_SPEED = 4;
    static Cannon _cannon;
    static List<Bubble> _bubles;
    static List<Laser> laserList;
    static final Object _sync = new Object();
    static float _lastTouchedX, _lastTouchedY;

    private float halfscreenX = AppConstants.SCREEN_WIDTH / 2;
    private float halfscreenY = AppConstants.SCREEN_HEIGHT / 2;
    private int maxLasers = 1;
    Handler removeLaser = new Handler();

    public GameEngine() {
        _bubles = new LinkedList<Bubble>();
        laserList = new LinkedList<Laser>();
        _paint = new Paint();
        _cannon = new Cannon();
    }

    Paint _paint;
    //TODO mulai buat logic game yang diinginkan: kalau terkena x bola player kalah(kasih logic nabrak + sfx)

    /**
     * Updates all relevant objects business logic
     */
    public void Update() {
        AdvanceBubbles();
    }

    /**
     * Iterates through the Bubble list and advances them
     */
    private void AdvanceBubbles() {
        synchronized (_sync) {
            for (Bubble b : _bubles) {
                b.Advance(BUBBLE_SPEED);
            }
        }
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
     * Draws Aim bitmap
     *
     * @param canvas canvas on which will be drawn the bitmap
     **/
    private void DrawAim(Canvas canvas) {
        //Doesn't draws on touch ACTION_UP event, only on ACTION_DOWN or ACTION_MOVE
        if (_lastTouchedX != FingerAim.DO_NOT_DRAW_X
                && _lastTouchedY != FingerAim.DO_NOT_DRAW_Y) {
            Bitmap bitmap = AppConstants.GetBitmapsBank().GetFingerAim();
            canvas.drawBitmap(bitmap, _lastTouchedX, _lastTouchedY, _paint);
        }
    }

    /**
     * Draws bubble bitmaps
     *
     * @param canvas canvas on which will be drawn the bitmap
     */
    private void DrawBubles(Canvas canvas) {
        synchronized (_sync) {
            for (Bubble buble : _bubles) {
                //Drawing bubble
                Bitmap bitmap = AppConstants.GetBitmapsBank().GetRedBubble();
                canvas.drawBitmap(bitmap, (float) buble.GetX(), (float) buble.GetY(), _paint);
            }
        }
    }

    /**
     * Draws cannon bitmap
     *
     * @param canvas canvas on which will be drawn the bitmap
     */
    private void DrawCanon(Canvas canvas) {
        Bitmap cannon = BitmapBank.RotateBitmap(AppConstants.GetBitmapsBank().GetAndroid(),
                _cannon.GetRotation());

        Rect rect = _cannon.GetRect(AppConstants.SCREEN_WIDTH, AppConstants.SCREEN_HEIGHT, cannon);
        canvas.drawBitmap(cannon, null, rect, _paint);
    }

    /**
     * Sets cannon bitmap rotation accordingly to touch event
     *
     * @param touch_x x coordinate of the touch event
     * @param touch_y y coordinate of the touch event
     */
    public void SetCannonRotaion(int touch_x, int touch_y) {
        float cannonRotation = RotationHandler
                .CannonRotationByTouch(touch_x, touch_y, _cannon);

        _cannon.SetRotation(cannonRotation);
    }

    /**
     * Crates a new bubble on touch event
     *
     * @param touchX x coordinates of touch event
     * @param touchY y coordinates of touch event
     */
    public void CreateNewBubble(int touchX, int touchY) {
        synchronized (_sync) {
            _bubles.add
                    (
                            new Bubble
                                    (
                                            _cannon.GetX(),
                                            _cannon.GetY(),
                                            _cannon.GetRotation(),
                                            touchX,
                                            touchY
                                    )
                    );
        }
    }

    /**
     * @return cannon object
     */
    public Cannon getCannon() {
        return _cannon;
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
