package com.iboy.game.objects;

import com.iboy.game.R;
import com.iboy.game.main.AppConstants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Responsible for screen painting.
 */
public class DisplayThread extends Thread {
    SurfaceHolder _surfaceHolder;
    Paint _backgroundPaint;
    public Semaphore semaphore;
    public ReentrantLock lock;
    Activity activity;


    //Delay amount between screen refreshes
    final long FPS = 60;
    final long LAG_PERIOD = 8;
    final long DELAY = 4;

    public boolean _isOnRun;

    public DisplayThread(SurfaceHolder surfaceHolder, Context context) {
        _surfaceHolder = surfaceHolder;
        semaphore=new Semaphore(1);
        lock=new ReentrantLock();
        activity=(Activity)context;

        //black painter below to clear the screen before the game is rendered
        _backgroundPaint = new Paint();
        _backgroundPaint.setARGB(255, 0, 0, 0);
        _isOnRun = true;
    }


    /**
     * This is the main nucleus of our program.
     * From here will be called all the method that are associated with the display in GameEngine object
     */
    @Override
    public void run() {

        long ticksPerSecond=1000/FPS;
        long startTime;
        long sleepTime;

        //Looping until the boolean is false
        while (_isOnRun) {
            //semaphore.acquire used to mark the beginning part of code needs to be stopped
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Updates the game objects buisiness logic
                AppConstants.GetEngine().Update();
            //locking the canvas
            Canvas canvas = _surfaceHolder.lockCanvas(null);
            startTime = System.currentTimeMillis();
            if (canvas != null) {
                //Clears the screen with black paint and draws object on the canvas
                synchronized (_surfaceHolder) {
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), _backgroundPaint);
                    AppConstants.GetEngine().Draw(canvas);
                }

                //unlocking the Canvas
                _surfaceHolder.unlockCanvasAndPost(canvas);
            }
            sleepTime = ticksPerSecond-(System.currentTimeMillis() - startTime);
            //delay time
            try {
                if (sleepTime > 0)
                    Thread.sleep(sleepTime);
                else
                    Thread.sleep(LAG_PERIOD);
            } catch (InterruptedException ex) {
                //TODO: Log
            }
            //semaphore.release used to mark end part of code needs to be stopped
            semaphore.release();
        }
    }

    /**
     * @return whether the thread is running
     */
    public boolean IsRunning() {
        return _isOnRun;
    }

    /**
     * Sets the thread state, false = stoped, true = running
     **/
    public void SetIsRunning(boolean state) {
        _isOnRun = state;
    }
}