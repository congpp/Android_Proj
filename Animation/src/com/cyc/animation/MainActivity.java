package com.cyc.animation;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends Activity
{

	int displayWidth, displyHeight;
	static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		displayWidth = metric.widthPixels; // ÆÁÄ»¿í¶È£¨ÏñËØ£©
		displyHeight = metric.heightPixels - 50; // ÆÁÄ»¸ß¶È£¨ÏñËØ£©
		setContentView(new GameSurfaceView(this));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
