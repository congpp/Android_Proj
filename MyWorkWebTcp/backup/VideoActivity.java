package com.cyc.mywork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class VideoActivity extends Activity
{

	//private static String info = "RUN";
	private WebView ivWeb = null;
	private String url = "http://192.168.235.222:8080/myjs.html";
	private ProgressBar pb=null;
	private ImageView ivImageView=null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		//Log.i(info, "OnCreate");
		ivImageView=(ImageView)findViewById(R.id.videoIv);
		ivImageView.setVisibility(View.INVISIBLE);
		
		ivWeb = (WebView) findViewById(R.id.wv);
		WebSettings setting = ivWeb.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setLoadWithOverviewMode(true);
		setting.setSupportZoom(false);
		
		pb=(ProgressBar) findViewById(R.id.videoPb);
		ivWeb.setBackgroundColor(Color.BLACK);
		ivWeb.setWebViewClient(new WebViewClient()
		{

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				pb.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description,
					String failingUrl)
			{
				// TODO while load url error
				//super.onReceivedError(view, errorCode, description, failingUrl);
				view.stopLoading();
				view.loadUrl("file:///android_asset/blank.html");
				Message msg=handler.obtainMessage();
				msg.what=112233;
				handler.sendMessage(msg);
				ivImageView.setVisibility(View.VISIBLE);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return true;
			}
			
		});

		ivWeb.loadUrl(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuv, menu);
		return true;
	}

	private Bitmap captureScreen(Activity context)
	{
		View cv = context.getWindow().getDecorView();
		Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Config.ARGB_8888);
		// Canvas canvas = new Canvas(bmp);
		// cv.draw(canvas);
		return bmp;
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			File imageFile = new File(file, str + ".jpg");
			Bitmap bmp = captureScreen(VideoActivity.this);
			FileOutputStream fos;
			try
			{
				fos = new FileOutputStream(imageFile);
				bmp.compress(CompressFormat.JPEG, 50, fos);
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
		case R.id.Vrefresh:
			//refresh
			pb.setVisibility(View.VISIBLE);
			ivWeb.loadUrl(url);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private MyHandler handler=new MyHandler(VideoActivity.this)
	{

		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.what)
			{
			//load url error
			case 112233:
				
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
			//System.out.println(msg);
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
		ivWeb.stopLoading();
		super.onDestroy();
	}
}
