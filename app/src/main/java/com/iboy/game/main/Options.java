package com.iboy.game.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.iboy.game.R;
import com.iboy.game.handlers.BitmapBank;
import com.iboy.game.objects.GameEngine;

public class Options
{

	//TODO rencananya klo lnsg bisa op Options gk seru, dibuat opsi beli nanti di shop secara perlahan, syarat level tertentu mungkin
	static public int level=1;

	static public int bulletSize=3;
	static public int bulletSpeed = 4;

	static public int enemySize=6;
	static public int enemySpeed=5;
	static public int enemyDelay=50;
	static public int enemyLife=4;
	static public boolean friendlyFire=false;

	static public void updateOptions(Context context){
		SharedPreferences sharedPref;
		sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		bulletSize=Integer.parseInt(sharedPref.getString(context.getString(R.string.bulletSize),"2"));
		bulletSpeed=Integer.parseInt(sharedPref.getString(context.getString(R.string.bulletSpeed),"2"));
		enemySize=Integer.parseInt(sharedPref.getString(context.getString(R.string.enemySize),"2"));
		enemySpeed=Integer.parseInt(sharedPref.getString(context.getString(R.string.enemySpeed),"2"));
		enemyDelay=Integer.parseInt(sharedPref.getString(context.getString(R.string.enemyDelay),"50"));
		enemyLife=Integer.parseInt(sharedPref.getString(context.getString(R.string.enemyLife),"4"));
		level=Integer.parseInt(sharedPref.getString(context.getString(R.string.level),"1"));
		friendlyFire=Boolean.valueOf(sharedPref.getString(context.getString(R.string.friendlyFire),"false"));
	}

}
