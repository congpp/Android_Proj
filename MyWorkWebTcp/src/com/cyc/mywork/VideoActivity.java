package com.cyc.mywork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends Activity implements Runnable
{

	private String STREAM_URL = null;
	private static final int BMP_CHANGE = 0x123;
	private static final int PB_CHANGE = 0x234;
	// private static String info = "RUN";
	// private String url = "http://192.168.235.222:8080/myjs.html";
	private ProgressBar pb = null;
	private ImageView ivImageView = null;
	private TextView ivInfo = null;
	protected boolean isRun = true;
	private String infoString = null;
	protected Bitmap bmp;
	// private int screenW = 480;
	private int screenH = 800;
	private float imgWScale = 1;
	private float imgHScale = 1;
	public Object lock = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);

		STREAM_URL = "http://" + ((CycApplication) getApplication()).getIp()
				+ ":8080/?action=snapshot";

		ivImageView = (ImageView) findViewById(R.id.videoIv);
		// ivImageView.setVisibility(View.INVISIBLE);
		ivInfo = (TextView) findViewById(R.id.videoTV);
		ivInfo.setTextColor(Color.BLUE);
		pb = (ProgressBar) findViewById(R.id.videoPb);
		
		// get the screen size
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenH = dm.heightPixels;
		// screenW = dm.widthPixels;
		lock = new Object();
		new Thread(VideoActivity.this).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuv, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO
		switch (item.getItemId())
		{
		case R.id.Vsave:
			// save image
			String savePath = ((CycApplication) getApplication()).getPath() + "videoCaptures/";
			File file = new File(savePath);
			if (!file.exists())
			{
				file.mkdir();
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			File imageFile = new File(file, str + ".jpg");
			FileOutputStream fos;

			try
			{
				fos = new FileOutputStream(imageFile);
				synchronized (lock)
				{
					bmp.compress(CompressFormat.JPEG, 50, fos);
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			Toast.makeText(VideoActivity.this, "Save img: " + str + ".jpg", Toast.LENGTH_SHORT)
					.show();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private MyHandler handler = new MyHandler(VideoActivity.this)
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			// load url error
			case BMP_CHANGE:
				ivImageView.setImageBitmap(bmp);
				ivInfo.setText(infoString);
				Log.i("RUN", "BMP change");
				break;
			case PB_CHANGE:
				pb.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
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
			// System.out.println(msg);
			if (mActivity.get() == null)
			{
				return;
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO
		// ivWeb.stopLoading();
		isRun = false;
		super.onDestroy();
	}

	@Override
	public void run()
	{
		// TODO run decode bmp from stream
		try
		{
			// hide the processbar
			Message msg = new Message();
			msg.obj = null;
			msg.what = PB_CHANGE;
			handler.sendMessage(msg);
			URL streamUrl = new URL(STREAM_URL);
			while (isRun)
			{
				InputStream is = streamUrl.openStream();
				synchronized (lock)
				{
					bmp = BitmapFactory.decodeStream(is);
				}
				SimpleDateFormat hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
				long preTime, curTime;
				if (bmp != null)
				{
					// get current time
					preTime = System.currentTimeMillis();
					
					//scale the image
					int imgwidth = bmp.getWidth();
					int imgheight = bmp.getHeight();
					//Log.i("RUN before-->", "imgWH:" + imgwidth + "\t" + imgheight);
					imgHScale = (float) screenH / imgheight;
					// imgWScale = (float) screenW / imgwidth;
					imgWScale = imgHScale;
					Matrix matrix = new Matrix();
					matrix.postScale(imgWScale, imgHScale);
					synchronized (lock)
					{
						bmp = Bitmap.createBitmap(bmp, 0, 0, imgwidth, imgheight, matrix, true);
					}
					
					// 1000/(20+20)=25 frames per second
					Thread.sleep(20);
					curTime = System.currentTimeMillis();
					infoString = "" + hms.format(new Date(System.currentTimeMillis()))
							+ "  Delay: " + (curTime - preTime) + " ms";
					msg = new Message();
					msg.obj = bmp;
					msg.what = BMP_CHANGE;
					handler.sendMessage(msg);
				} else
				{
					Log.e("RUN", "Cannot decode bmp");
				}
			}
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}
}
