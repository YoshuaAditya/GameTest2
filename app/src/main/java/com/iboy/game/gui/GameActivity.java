package com.iboy.game.gui;

import com.iboy.game.main.AppConstants;
import com.iboy.game.views.GameView;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GameActivity extends Activity {

	GameView _gameEngineView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //sets the activity view as GameView class
        SurfaceView view = new GameView(this, AppConstants.GetEngine());
        setContentView(view);

//        getActionBar().hide();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		 super.onTouchEvent(event);
		int action = event.getAction();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				OnActionDown(event);
				break;
			}
			case MotionEvent.ACTION_UP:
			{
//				OnActionUp(event);
				break;
			}
			case MotionEvent.ACTION_MOVE:
			{
				OnActionMove(event);
				break;
			}
			default:break;
		}
		return false;
	}

	/*activates on touch move event*/
	private void OnActionMove(MotionEvent event)
	{
		int x = (int)event.getX();
		int y = (int)event.getY();

		AppConstants.GetEngine().SetLastTouch(event.getX(), event.getY());
	}


	/*activates on touch up event*/
	private void OnActionUp(MotionEvent event)
	{
		int x = (int)event.getX();
		int y = (int)event.getY();

	}
	/*activates on touch down event*/
	private void OnActionDown(MotionEvent event) 
	{
		 AppConstants.GetEngine().SetLastTouch(event.getX(), event.getY());
	}

}
