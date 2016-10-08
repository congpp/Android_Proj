package com.cyc.mywork;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TemActivity extends Activity implements Runnable
{

	private static String info = "TemActivity";
	private ChartView myView = null;
	private boolean isRun = true;
	private String date[];
	private String value[];
	int avg = 0; // x轴刻度数

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Log class
		Log.i(info, "OnCreate");
		setContentView(R.layout.activity_tem);
		myView = (ChartView) findViewById(R.id.myCv);
		// get the screen size
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Log.i("RUN", "Screen size: " + dm.widthPixels + "\t" + dm.heightPixels);
		float textSize = 0;
		if (dm.widthPixels > 900)
		{
			avg = 16;
			textSize = 20;
		} else if (dm.heightPixels > 600)
		{
			avg = 12;
			textSize = 16;
		} else
		{
			avg = 8;
			textSize = 12;
		}
		myView.setScreenPara(dm.widthPixels, dm.heightPixels, avg, textSize);
		
		new Thread(TemActivity.this).start();
	}

	@Override
	public void run()
	{
		// TODO run
		while (isRun)
		{
			try
			{
				mHandler.sendMessage(Message.obtain());
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private MyHandler mHandler = new MyHandler(this)
	{
		// TODO
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			//Log.i("RUN avg", "" + avg);
			String temp[];
			temp = ((CycApplication) getApplication()).getLastTemperature(avg + 1);
			date = new String[avg];
			value = new String[avg];
			int max = -99999;
			int min = 99999;
			// cut date and value from temp
			int j,i;
			for (j = 0, i = 0; j < avg && temp[j] != null; j++, i++)
			{
				// Log.i("RUN tempActivity", temp[j]);
				// the string format is: 201405=35.5
				date[i] = temp[j].substring(0, 6);
				value[i] = temp[j].substring(7, temp[j].length() - 1);
				double tmp = 0;
				try
				{
					tmp = Double.parseDouble(value[i]);
				} catch (NumberFormatException e)
				{
					Toast.makeText(TemActivity.this, "Wrong data from the server", Toast.LENGTH_SHORT).show();
					return;
				}
				// take max and min, tmp*100 -> 2532
				if (tmp * 100 > max)
					max = (int) (tmp * 100);
				else if (tmp * 100 < min)
					min = (int) (tmp * 100);
				// Log.i("RUN", date[i] + " " + value[i]);
			}
			// max -> 3532 
			max += 1000;
			// min -> 1532
			min -= 1000;
			Log.i("RUN", max+ " " + min);
			if (date == null || value == null)
				return;
			// refresh myView
			myView.SetInfo(date, // X轴刻度，时间刻度
					new String[] { min + "", max + "" }, // Y轴刻度
					value, // 数据
					"Temperature History View", "Current:" + value[j-1]);
			myView.invalidate();
		}
	};

	// Handle "This Handler class should be static or leaks might occur."
	private static class MyHandler extends Handler
	{
		private final WeakReference<Activity> mActivity;

		public MyHandler(Activity activity)
		{
			mActivity = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			if (mActivity.get() == null)
			{
				return;
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		isRun = false;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menut, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO
		switch (item.getItemId())
		{
		case R.id.menuTImages:
			Intent intent = new Intent();
			intent.setClass(TemActivity.this, WebImageViewActivity.class);
			intent.putExtra("log", "TI/");
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
