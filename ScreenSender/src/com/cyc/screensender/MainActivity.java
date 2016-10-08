package com.cyc.screensender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Queue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity implements Runnable
{
	private Button btnConnect = null;
	private Button btnStart = null;
	protected static final String HOST = "192.168.235.2";
	protected static final int PORT = 8088;
	protected Socket socket = null;
	protected SocketAddress socketAddress = null;
	protected BufferedReader in = null;
	protected DataOutputStream out = null;
	protected boolean isTcpConnected=false;
	protected Queue<Bitmap> bmpQueue;
	protected boolean isStart=false;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnStart = (Button) findViewById(R.id.btnStart);
		btnConnect.setOnClickListener(listener);
		btnStart.setOnClickListener(listener);
		socketAddress = new InetSocketAddress(HOST, PORT);
	}

	private OnClickListener listener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btnConnect:
				if(!isTcpConnected)
				{
					btnConnect.setText("Connect");
					new Thread(MainActivity.this).start();
				}
				else
				{
					isTcpConnected=false;
					btnConnect.setText("Connect");
				}
				break;
			case R.id.btnStart:
				if(isStart)
				{
					isStart=false;
					btnStart.setText("Start");
				}
				else 
				{
					isStart=true;
					btnStart.setText("Stop");
				}
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onDestroy()
	{
		isTcpConnected = false;
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private Bitmap captureScreen(Activity context)
	{
		//View cv = context.getWindow().getDecorView();
		//Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Config.ARGB_8888);
		// Canvas canvas = new Canvas(bmp);
		// cv.draw(canvas);
		WindowManager windowManager = getWindowManager();  
		Display display = windowManager.getDefaultDisplay();  
		int w = display.getWidth();  
		int h = display.getHeight();  
		Bitmap bmp = Bitmap.createBitmap( w, h, Config.ARGB_8888 );      
		//获取屏幕  
		View decorview = this.getWindow().getDecorView();   
		decorview.setDrawingCacheEnabled(true);   
		bmp = decorview.getDrawingCache();
		return bmp;
	}
	
	@Override
	public void run()
	{
		Message msg;
		// TODO Auto-generated method stub
		while (!isTcpConnected)
		{
			try
			{
				// step one connect to server
				socket = new Socket();
				socket.connect(socketAddress, 5000);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new DataOutputStream(socket.getOutputStream());
				isTcpConnected = true;
				Log.i("RUN", "Conected! " + isTcpConnected + HOST + PORT);
			} catch (IOException ex)
			{
				ex.printStackTrace();
				Log.i("RUN", "IOException TimeOut: " + isTcpConnected + HOST + PORT);
			}
		}// while(isConnected)
		msg = Message.obtain();
		msg.obj = null;
		msg.what = 123;
		mHandler.sendMessage(msg);
		Bitmap bmp;
		while(!isStart);
		while (isTcpConnected)
		{
			try
			{
				if (socket.isConnected()&&isStart)
					if (!socket.isOutputShutdown())
					{
						//bmpQueue.add(captureScreen(MainActivity.this));
						bmp=captureScreen(MainActivity.this);
						ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		                //读取图片到ByteArrayOutputStream                      
		                bmp.compress(Bitmap.CompressFormat.PNG, 100, baos); 
		                out.writeInt(bmp.getWidth());
		                out.flush();
		                out.writeInt(bmp.getHeight());
		                out.flush();
		                byte[] bytes = baos.toByteArray();  
		                out.write(bytes); 
		                out.flush();
					}
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		Log.i("RUN", "Thread tcp ends");
	}
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
	private MyHandler mHandler = new MyHandler(this)
	{
		// TODO mHandler
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			// Log.i("mHandler", "Thread id " + Thread.currentThread().getId());
			switch (msg.what)
			{
			case 123:
				btnConnect.setText("Disconnect");
				break;
			default:
				break;
			}
		}
	};
}
