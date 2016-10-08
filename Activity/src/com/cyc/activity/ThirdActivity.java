package com.cyc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class ThirdActivity extends Activity
{

	private static String info="Third Activity";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		//Log class
		Log.i(info, "OnCreate");
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
