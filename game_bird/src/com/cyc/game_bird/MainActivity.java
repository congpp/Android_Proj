package com.cyc.game_bird;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/*
 * 小鸟飞行路径改成了S型，游戏难度增大
 */
public class MainActivity extends Activity
{
	static int displayWidth, displyHeight;
	static MainActivity instance;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		instance = this;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		displayWidth = metric.widthPixels; // 屏幕宽度（像素）
		displyHeight = metric.heightPixels - 50; // 屏幕高度（像素）
		setContentView(new GameSurfaceView(this));
	}

}
