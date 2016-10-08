package com.cyc.mywork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class WebImageViewActivity extends Activity implements Runnable
{
	private TextView imageInfo = null;
	private ImageView imageIv = null;
	private ImageButton imageToLeft = null, imageToRight = null;
	private ProgressBar webImagePb = null;
	// url to get image (images/*/*.jpg)
	private String url = null;
	String path = null;
	private List<String> allImgName = null;
	// url to get image name (log/*/date.txt)
	private URL aURL;
	private String urlDir = null;
	private FileOutputStream fos;
	private BufferedReader reader;
	private HttpURLConnection conn;
	private int location = 0;
	private BitmapDrawable curDrawable = null;
	private BitmapDrawable leftDrawable = null;
	private BitmapDrawable rightDrawable = null;
	public Object lock = null;
	private static final int RIGHT = 0;
	private static final int LEFT = 1;
	private static final int INIT = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		//this url is used to download images
		url="http://"+((CycApplication)getApplication()).getIp()+"/images/";
		Intent intent = getIntent();
		urlDir = intent.getStringExtra("log");
		if(urlDir.compareTo("TI/")==0)
			url=url+"T/";
		else
			url = url + urlDir;
		Log.i("RUN url", url);
		// 用于线程同步
		lock = new Object();

		imageToLeft = (ImageButton) findViewById(R.id.imageToLeft);
		imageToRight = (ImageButton) findViewById(R.id.imageToRight);
		webImagePb = (ProgressBar) findViewById(R.id.webImagePb);
		imageInfo = (TextView) findViewById(R.id.imageInfo);
		imageIv = (ImageView) findViewById(R.id.webImageView);
		imageInfo.setText("");
		imageInfo.setTextColor(Color.BLUE);

		imageToLeft.setOnClickListener(listener);
		imageToRight.setOnClickListener(listener);
		imageToLeft.setEnabled(false);
		imageToRight.setEnabled(false);

		path = ((CycApplication) getApplication()).getPath()+"images/";
		allImgName = new ArrayList<String>();
		new Thread(WebImageViewActivity.this).start();

	}

	private OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (allImgName.size() == 0)
			{
				imageInfo.setText("No image found!");
				return;
			}
			synchronized (lock)
			{
				switch (v.getId())
				{
				case R.id.imageToLeft:
					webImagePb.setVisibility(View.VISIBLE);
					imageIv.setImageDrawable(curDrawable);
					webImagePb.setVisibility(View.INVISIBLE);
					imageInfo.setText(allImgName.get(location));
					location--;
					location = (location + allImgName.size()) % allImgName.size();
					new PreLoadImageClass(LEFT).start();
					break;
				case R.id.imageToRight:
					webImagePb.setVisibility(View.VISIBLE);
					imageIv.setImageDrawable(curDrawable);
					webImagePb.setVisibility(View.INVISIBLE);
					imageInfo.setText(allImgName.get(location));
					location++;
					location = (location + allImgName.size()) % allImgName.size();
					new PreLoadImageClass(RIGHT).start();
					break;
				}
			}
		}

	};

	private String getRightPicName()
	{
		String tmp;
		int i = location + 1;
		i += allImgName.size();
		i %= allImgName.size();
		tmp = allImgName.get(i);
		return tmp;
	}

	private String getLeftPicName()
	{
		String tmp;
		int i = location - 1;
		i += allImgName.size();
		i %= allImgName.size();
		tmp = allImgName.get(i);
		return tmp;
	}

	/*
	 * get all images' name from server/log/(H/T/S/W/L)/date.txt
	 */
	@Override
	public void run()
	{
		// TODO
		SimpleDateFormat md = new SimpleDateFormat("MMdd", Locale.US);
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String date = md.format(curDate);
		try
		{
			//this url is used to download log.txt
			aURL = new URL("http://"+((CycApplication)getApplication()).getIp()+"/log/" + urlDir + date + ".txt");
			conn = (HttpURLConnection) aURL.openConnection();
			conn.connect();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String tmp;
			while ((tmp = reader.readLine()) != null)
			{
				Log.i("!!!!!", tmp);
				allImgName.add(tmp);
			}
			location = allImgName.size() - 1;
			Log.i("RUN !!!!!!!", allImgName.get(location));
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Message msg = mHandler.obtainMessage();
		if (allImgName.isEmpty())
		{
			msg.what = 1;
			mHandler.sendMessage(msg);
			return;
		} else
		{
			curDrawable = loadImageFromNetwork(url, allImgName.get(allImgName.size() - 1));
			msg.what = 0;
			mHandler.sendMessage(msg);
		}
	}

	private MyHandler mHandler = new MyHandler(this)
	{
		// TODO
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			webImagePb.setVisibility(View.INVISIBLE);
			switch (msg.what)
			{
			case 1:
				break;
			case 0:
				imageToLeft.setEnabled(true);
				imageToRight.setEnabled(true);
				imageIv.setImageDrawable(curDrawable);
				imageInfo.setText(allImgName.get(location));
				Log.i("handlr", url);
				new PreLoadImageClass(INIT).start();
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
			System.out.println(msg);
			if (mActivity.get() == null)
			{
				return;
			}
		}
	}

	class PreLoadImageClass extends Thread
	{
		int tag = 0;

		public PreLoadImageClass(int tag)
		{
			this.tag = tag;
		}

		@Override
		public void run()
		{
			synchronized (lock)
			{
				if (tag == RIGHT)// right
				{
					leftDrawable = curDrawable;
					curDrawable=rightDrawable;
					rightDrawable = loadImageFromNetwork(url, getRightPicName());
				} else if (tag == LEFT)// left
				{
					rightDrawable =curDrawable;
					curDrawable=leftDrawable;
					leftDrawable = loadImageFromNetwork(url, getLeftPicName());
				} else//init
				{
					rightDrawable = loadImageFromNetwork(url, getRightPicName());
					leftDrawable = loadImageFromNetwork(url, getLeftPicName());
				}
			}

		}

	}

	protected BitmapDrawable loadImageFromNetwork(String imageUrl, String imageName)
	{
		// TODO
		BitmapDrawable drawable = null;
		try
		{
			File file = new File(path + imageName);
			if (file.exists())
				drawable = (BitmapDrawable) BitmapDrawable.createFromPath(path + imageName);
			else
				drawable = (BitmapDrawable) BitmapDrawable.createFromStream(new URL(imageUrl
						+ imageName).openStream(), imageName);
		} catch (IOException e)
		{
			Log.d("test", e.getMessage());
		}
		return drawable;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menuh, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO
		switch (item.getItemId())
		{
		case R.id.menuHSave:
			// save image
			try
			{
				File file = new File(path + allImgName.get(location));
				fos = new FileOutputStream(file);
				Bitmap bmpBitmap;
				bmpBitmap = curDrawable.getBitmap();
				bmpBitmap.compress(CompressFormat.JPEG, 50, fos);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			Toast.makeText(WebImageViewActivity.this, "Save image: " + allImgName.get(location),
					Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
