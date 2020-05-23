package com.iboy.game.gui;

import com.iboy.game.R;
import com.iboy.game.main.AppConstants;
import com.iboy.game.objects.DisplayThread;
import com.iboy.game.objects.GameEngine;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.Semaphore;

public class GameActivity extends Activity implements SurfaceHolder.Callback {

    Context context;
    SurfaceView view;
    SurfaceHolder holder;
    public TextView textScore, textLife, textLevel;
    private DisplayThread displayThread;
    public View pauseScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        context = this;

        //getting surface holder from the layout
        view = findViewById(R.id.gameView);
        textLife = findViewById(R.id.life);
        textScore = findViewById(R.id.score);
        textLevel = findViewById(R.id.level);
        pauseScreen = findViewById(R.id.pauseScreen);
        holder = view.getHolder();
        holder.addCallback(this);

        displayThread = new DisplayThread(holder, context);
        view.setFocusable(true);

        //pass GameActivity into GameEngine, mainly for buttons
        AppConstants.GetEngine().setActivityContext(context, displayThread);
    }

    //Handles touch event in game screen
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                OnActionDown(event);
                break;
            }
            case MotionEvent.ACTION_UP: {
//				OnActionUp(event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                OnActionMove(event);
                break;
            }
            default:
                break;
        }
        return false;
    }

    /*activates on touch move event*/
    private void OnActionMove(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        AppConstants.GetEngine().SetLastTouch(event.getX(), event.getY());
    }


    /*activates on touch up event*/
    private void OnActionUp(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

    }

    /*activates on touch down event*/
    private void OnActionDown(MotionEvent event) {

        AppConstants.GetEngine().SetLastTouch(event.getX(), event.getY());
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        /*DO NOTHING*/
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        //Stop the display thread
        displayThread.SetIsRunning(false);
        AppConstants.StopThread(displayThread);
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        //Condition when activity started from home button
        if (AppConstants.GetEngine().isPaused) {
            //Draw last situation of UI
            Paint backgroundPaint = new Paint();
            backgroundPaint.setARGB(255, 0, 0, 0);
            Canvas canvas = holder.lockCanvas(null);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
            AppConstants.GetEngine().Draw(canvas);
            holder.unlockCanvasAndPost(canvas);

            //Replace old thread with new thread
            displayThread = new DisplayThread(view.getHolder(), context);
            displayThread.start();
            try {
                displayThread.semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AppConstants.GetEngine().isPaused = true;
            AppConstants.GetEngine().displayThread = displayThread;
            return;
        }

        if (!displayThread.IsRunning()) {
            displayThread = new DisplayThread(view.getHolder(), context);
            displayThread.start();
        } else {
            displayThread.start();
        }
    }

    @Override
    protected void onStop() {
        AppConstants.GetEngine().displayThread._isOnRun = false;
        pauseScreen.setVisibility(View.VISIBLE);
        super.onStop();
    }

    @Override
    public void onBackPressed(){
//        findViewById(R.id.buttonPause).callOnClick();
    }
}
