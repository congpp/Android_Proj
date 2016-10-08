package com.cyc.mywork;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoActivity extends Activity implements Runnable
{

	private static String info = "RUN";
	private ImageView ivVideo = null;
	private TextView tvVideo = null;
	private boolean isRun = true;
	private long status=0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		Log.i(info, "OnCreate");

		tvVideo = (TextView) findViewById(R.id.tvVideo);
		ivVideo = (ImageView) findViewById(R.id.ivVideo);
		status = ((ShareDataApplication) getApplication()).getStatus();
		tvVideo.setText(((ShareDataApplication) getApplication()).getString());
		ivVideo.setImageBitmap(((ShareDataApplication) getApplication())
				.getShareBitmap());

		new Thread(VideoActivity.this).start();
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
		isRun = true;
		super.onRestart();
	}

	@Override
	protected void onResume()
	{
		super.onRestart();
	}

	@Override
	protected void onStart()
	{
		isRun = true;
		super.onStart();
	}

	@Override
	protected void onStop()
	{
		isRun = false;
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		isRun = false;
		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		isRun = false;
		super.onPause();
	}

	@Override
	public void run()
	{
		while (isRun)
		{
			if(isImgRefreshed())
			{
				Bitmap tmpBitmap=((ShareDataApplication) getApplication())
						.getShareBitmap();
				//¿≠…ÏÕº∆¨
				ivVideo.setImageBitmap(tmpBitmap);
			}
		}
	}

	private boolean isImgRefreshed()
	{
		if(((ShareDataApplication) getApplication()).getStatus()!=status)
			return true;
		else
			return false;
	}
}
