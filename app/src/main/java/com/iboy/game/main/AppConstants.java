package com.iboy.game.main;

import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.objects.GameEngine;
import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.objects.GameEngine;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class AppConstants 
{

	static BitmapBank _bitmapsBank;
	static GameEngine _engine;
	
	public static int SCREEN_WIDTH, 
					  SCREEN_HEIGHT
			,HALFSCREEN_WIDTH
			,HALFSCREEN_HEIGHT;
	
	/**
	 * Initiates the application constants
	 * */
	public static void Initialization(Context context)  
	{
		_bitmapsBank = new BitmapBank(context.getResources());
	    SetScreenSize(context); 
	    _engine = new GameEngine(context);
	}


	/**
	 * Sets screen size constants accordingly to device screen size
	 * */
	private static void SetScreenSize(Context context) 
	{
	    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display display = wm.getDefaultDisplay();
	    DisplayMetrics metrics = new DisplayMetrics();
	    display.getMetrics(metrics);
	    int width = metrics.widthPixels;
	    int height = metrics.heightPixels;
	    
	    AppConstants.SCREEN_WIDTH = width;
	    AppConstants.SCREEN_HEIGHT = height;
		AppConstants.HALFSCREEN_WIDTH = width /2;
		AppConstants.HALFSCREEN_HEIGHT = height/2;
	}

	/**
	 * @return BitmapBank instance
	 * */
	public static BitmapBank GetBitmapsBank()
	{
		return _bitmapsBank;
	}
	
	/**
	 * @return GameEngine instance
	 * */
	public static GameEngine GetEngine() 
	{
		return _engine;
	}


	/**
	 * Stops the given thread
	 * @param thread
	 * 			thread to stop
	 * */
	public static void StopThread(Thread thread) 
	{
		boolean retry = true;
		while (retry) 
		{
		    try 
		    {
		        thread.join();
		        retry = false;
		    } 
		    catch (InterruptedException e) {}
		}
	}
	
}
