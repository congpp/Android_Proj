package com.cyc.mywork;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;

public class TemActivity extends Activity
{

	private static String info="Second Activity";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Log class
		Log.i(info, "OnCreate");
		ChartView myView=new ChartView(this);
		setContentView(myView);
		//获取屏幕大小
		DisplayMetrics dm = new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.i("RUN", "Screen size: "+dm.widthPixels+"\t"+dm.heightPixels);
		myView.setScreenPara(dm.widthPixels, dm.heightPixels);
		myView.SetInfo(new String[] { "7-11", "7-12", "7-13", "7-14", "7-15",
				"7-16", "7-17" }, // X轴刻度
				new String[] { "", "50", "100", "150", "200", "250" }, // Y轴刻度
				new String[] { "105", "123", "110", "236", "145", "140", "122" }, // 数据
				"Temperature History View");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		Log.i(info, "Main onRestart()");
	}
	
	@Override
	protected void onResume()
	{
		super.onRestart();
		Log.i(info, "Main onResume()");
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Log.i(info,"Main onStart()");
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		Log.i(info, "Main onStop()");
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		Log.i(info, "Main onDestroy()");
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		Log.i(info, "Main onPause()");
	}
}
