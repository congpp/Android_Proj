package com.cyc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
{

	private static String info="MainActivity";
	private Button btn1=null;
	private Button btn2=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Log class
		Log.i(info, "OnCreate");
		
		btn1=(Button)findViewById(R.id.btn1);
		btn2=(Button)findViewById(R.id.btn2);
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
		
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			Button btn=(Button)v;
			Intent intent=new Intent();
			switch (btn.getId())
			{
			case R.id.btn1:
//				Toast.makeText(MainActivity.this, 
//						"You have clicked the button 1",Toast.LENGTH_SHORT).show();
				
				intent.setClass(MainActivity.this, SecondActivity.class);
				break;
			case R.id.btn2:
				intent.setClass(MainActivity.this, ThirdActivity.class);
//				Toast.makeText(MainActivity.this, 
//						"You have clicked the button 2",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
			startActivity(intent);
		}
	};
	
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
