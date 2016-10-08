package com.cyc.game_bird;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

/*
 * С�����·���ĳ���S�ͣ���Ϸ�Ѷ�����
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
		displayWidth = metric.widthPixels; // ��Ļ��ȣ����أ�
		displyHeight = metric.heightPixels - 50; // ��Ļ�߶ȣ����أ�
		setContentView(new GameSurfaceView(this));
	}

}
