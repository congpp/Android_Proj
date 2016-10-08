package com.cyc.mywork;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity implements Runnable
{
	// private Button btn_login = null;

	private static final String HOST = "192.168.235.222";
	private static final int PORT = 6666;
	private Socket socket = null;
	private SocketAddress socketAddress = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String recvCmd = "";

	private FileOutputStream logTFos;
	private Date curDate = null;
	private String hour = null;
	private String date = null;
	private final boolean reconnect[] = { false, true };
	private static final boolean CANCEL = false;
	private static final boolean AGAIN = true;

	private static final String SMOKE_STATUS = "SMOKE";
	private static final String TEMP_STATUS = "TEMPE";
	private static final String HUMAN_STATUS = "HUMAN";
	private String currentSmoke = "No smoke detected!";
	private String currentTemp = "Current temperature: ";
	private String currentHuman = "No human body detected!";
	private Thread mThread = null;

	private boolean isRun = true;
	private boolean isConnectToWeb = false;

	protected static final AdapterView<ListAdapter> myListView = null;
	private static final int IS_LIST = 1;
	private static final int IS_DIALOG = 2;
	private static final int IS_PROGRESSBAR = 3;
	public static final int IS_RECVCMD = 4;
	protected static final boolean ON = true;
	protected static final boolean OFF = false;
	protected boolean LIGHT = OFF;

	private ListView lv1 = null;
	private SimpleAdapter adapter = null;
	private ProgressBar pb1 = null;

	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("RUN", "setContentView");
		adapter = new SimpleAdapter(this, getData(), R.layout.mylist, new String[] { "title",
				"info", "img" }, new int[] { R.id.title, R.id.info, R.id.img });
		setListAdapter(adapter);
		lv1 = getListView();
		lv1.setBackgroundColor(Color.TRANSPARENT);
		lv1.setOnItemClickListener(myListener);
		// Avoid the bg becomes black when scrolling
		lv1.setCacheColorHint(Color.TRANSPARENT);

		lv1.setEnabled(false);
		pb1 = (ProgressBar) findViewById(R.id.pb1);
		socketAddress = new InetSocketAddress(HOST, PORT);

		// try
		// {
		// ((CycApplication) getApplication())
		// .setShareBitmap(toByteArray("/mnt/sdcard/cycApp/10.jpg"));
		// } catch (IOException e)
		// {
		// e.printStackTrace();
		// }
		// ((CycApplication) getApplication())
		// .setString("This image is from MainActivity");
		pb1.setIndeterminate(true);
		setProgressBarIndeterminateVisibility(true);
		mThread = new Thread(MainActivity.this);
		mThread.start();

	}

	private OnItemClickListener myListener = new OnItemClickListener()
	{
		// TODO onItemClick
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			if (isConnectToWeb == false)
			{
				Toast.makeText(MainActivity.this, "Webserver not found!", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			@SuppressWarnings("unchecked")
			HashMap<String, Object> map = (HashMap<String, Object>) lv1.getItemAtPosition(arg2);
			String title = (String) map.get("title");
			Intent myIntent = new Intent();
			if (title.equals("Temperature"))
			{
				// change text dynamically
				// map.put("info", "Damn!");
				// adapter.notifyDataSetChanged();
				// Log.i("RUN", "setClass: TemActivity.class!");
				myIntent.setClass(MainActivity.this, TemActivity.class);
				startActivity(myIntent);
			} else if (title.equals("Video"))
			{
				// Log.i("RUN", "setClass: VideoActivity.class!");
				myIntent.setClass(MainActivity.this, VideoActivity.class);
				startActivity(myIntent);
			} else if (title.equals("Smoke"))
			{
				myIntent.putExtra("log", "S/");
				myIntent.setClass(MainActivity.this, WebImageViewActivity.class);
				startActivity(myIntent);

			} else if (title.equals("Human"))
			{
				myIntent.putExtra("log", "H/");
				myIntent.setClass(MainActivity.this, WebImageViewActivity.class);
				startActivity(myIntent);
			} else if (title.equals("Light"))
			{
				// turn on or off the light
				// map.put("img", R.drawable.lighton);
				// adapter.notifyDataSetChanged();
				if (LIGHT == ON)
				{
					sendCtrlCommand("LIGHT=ON");
					LIGHT = OFF;
				} else
				{
					LIGHT = ON;
					sendCtrlCommand("LIGHT=OFF");
				}
			} else if (title.equals("Contect Me"))
			{
				new AlertDialog.Builder(MainActivity.this).setTitle("About this program:")
						.setMessage("By CYC @GDUFS\nEmail:congpp@qq.com")
						.setPositiveButton("OK", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
							}
						}).show();
				return;
			}
			// Toast.makeText(getApplicationContext(), arg2 + "\t" + title +
			// "\t" + content,
			// Toast.LENGTH_SHORT).show();
		}
	};

	private List<Map<String, Object>> getData()
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Smoke");
		map.put("info", currentSmoke);
		map.put("img", R.drawable.smoke);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Temperature");
		map.put("info", currentTemp);
		map.put("img", R.drawable.tem);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Human");
		map.put("info", currentHuman);
		map.put("img", R.drawable.human);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Video");
		map.put("info", "Click to play...");
		map.put("img", R.drawable.video);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Light");
		map.put("info", "Light is: off");
		map.put("img", R.drawable.lightoff);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Contect Me");
		map.put("info", "CYC @GDUFS Creative Lab");
		map.put("img", R.drawable.about);
		list.add(map);

		return list;
	}

	protected void sendCtrlCommand(String cmd)
	{
		// TODO sendCtrlCommand
		if (out == null)
		{
			Toast.makeText(MainActivity.this, "Not in control mode!", Toast.LENGTH_SHORT).show();
		} else
		{
			if (socket.isConnected())
				if (!socket.isOutputShutdown())
					out.println(cmd);
		}
	}

	/*
	 * (cyc-Javadoc)
	 * 
	 * @cyc java.lang.Runnable#run() you need to finished 3 jobs here: 1.connect
	 * to server, 2. get log/T/[date].txt 3.
	 */
	@Override
	public void run()
	{
		// TODO run
		Message msg;
		while (!isConnectToWeb)
		{
			try
			{
				// prepare to get log txt
				URL aURL;
				BufferedReader reader;
				HttpURLConnection conn;
				SimpleDateFormat md = new SimpleDateFormat("MMdd", Locale.US);
				SimpleDateFormat hms = new SimpleDateFormat("hhmmss", Locale.US);
				curDate = new Date(System.currentTimeMillis());// 获取当前时间
				date = md.format(curDate);
				hour = hms.format(curDate);

				Log.i("RUN date", date);
				String savePath = ((CycApplication) getApplication()).getPath() + "log/T/";
				File file = new File(savePath);
				if (!file.exists())
				{
					file.mkdir();
				}
				// 2. get logT.txt
				aURL = new URL("http://192.168.235.222/log/T/" + date + ".txt");
				conn = (HttpURLConnection) aURL.openConnection();
				conn.connect();
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				File logT = new File(file, date + ".txt");

				logTFos = new FileOutputStream(logT);
				String tmp;

				while ((tmp = reader.readLine()) != null)
				{
					tmp += "\n";
					logTFos.write(tmp.getBytes());
					((CycApplication) getApplication()).addTemperature(tmp);
					Log.i("RUN add t", tmp);
				}
				logTFos.flush();
				// cancel progress bar
				msg = mHandler.obtainMessage();
				msg.obj = null;
				msg.what = IS_PROGRESSBAR;
				mHandler.sendMessage(msg);
				isConnectToWeb = true;
				Log.i("RUN", "isConnectToWeb " + isConnectToWeb + date);
			} catch (Exception e)
			{
				e.printStackTrace();
				msg = Message.obtain();
				msg.obj = "Failed to connect to Web server!\n Make sure your network is avaliable!";
				msg.what = IS_DIALOG;
				mHandler.sendMessage(msg);
				while (reconnect[1])
					;
				if (reconnect[0] == CANCEL)
				{
					System.exit(0);
				}
				reconnect[1] = true;
				Log.i("RUN", "reconnect web");
			}
		}
		// Log.i("RUN", "isConnectToWeb " + isConnectToWeb + date);
		while (isRun)
		{
			// TODO
			try
			{
				URL aURL;
				aURL = new URL("http://192.168.235.222/sensor.txt");
				HttpURLConnection conn = (HttpURLConnection) aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String info = "";
				while ((info = reader.readLine()) != null)
				{
					// Log.i("RUN", "info " + info);
					msg = Message.obtain();
					msg.obj = info;
					msg.what = IS_LIST;
					mHandler.sendMessage(msg);
				}

			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		Log.i("RUN", "Thread web ends");
		try
		{
			if (logTFos != null)
				logTFos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void showDialog(String title, String msg)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setNegativeButton("Exit", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// cancel
				reconnect[0] = CANCEL;
				reconnect[1] = false;
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton("Try again", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// confirm
				reconnect[0] = AGAIN;
				reconnect[1] = false;
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	public static byte[] toByteArray(String filename) throws IOException
	{

		File f = new File(filename);
		if (!f.exists())
		{
			throw new FileNotFoundException(filename);
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size)))
			{
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		} finally
		{
			try
			{
				in.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			bos.close();
		}
	}

	private void analyzeListData(String string)
	{
		// TODO
		int strLen = string.length();
		boolean warning = false;
		if (strLen < 6)
			return;
		String tmpFirstString = string.substring(0, 5);
		String tmpSecondString = string.substring(5, 6);
		if (tmpFirstString.equals(SMOKE_STATUS))
		{
			if (tmpSecondString.equals("1"))
			{
				currentSmoke = "Warning: Smoke Detected!";
				warning = true;
			} else
			{
				currentSmoke = "No smoke detected!";
				warning = false;
			}
			updateList(0, currentSmoke, warning);
		} else if (tmpFirstString.equals(TEMP_STATUS))
		{
			currentTemp = "Current Temperture: " + string.substring(5, strLen) + "℃";
			String tmp = hour + "=" + string.substring(5, strLen) + "\n";
			try
			{
				if (Double.parseDouble(string.substring(5, strLen)) > 60)
					warning = true;
				else
					warning = false;
				((CycApplication) getApplication()).addTemperature(tmp);
				logTFos.write(tmp.getBytes());
				logTFos.flush();
				updateList(1, currentTemp, warning);
			} catch (Exception e)
			{
				return;
			}

		} else if (tmpFirstString.equals(HUMAN_STATUS))
		{
			if (tmpSecondString.equals("1"))
			{
				currentHuman = "Warning: Human Body Detected!";
				warning = true;
			} else
			{
				currentHuman = "No Human Body Detected!";
				warning = false;
			}
			updateList(2, currentHuman, warning);
		}

	}

	private void updateList(int position, String data, boolean warning)
	{
		// TODO updateList
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) lv1.getItemAtPosition(position);
		map.put("info", data);
		if (warning)
			map.put("img", R.drawable.warning);
		else
		{
			switch (position)
			{
			case 0:
				map.put("img", R.drawable.smoke);
				break;
			case 1:
				map.put("img", R.drawable.tem);
				break;
			case 2:
				map.put("img", R.drawable.human);
			default:
				break;
			}
		}

		adapter.notifyDataSetChanged();
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
			case IS_LIST:
				analyzeListData((String) msg.obj);
				break;
			case IS_DIALOG:
				showDialog("Ooooops:", (String) msg.obj);
				break;
			case IS_PROGRESSBAR:
				if (msg.obj == null)
				{
					pb1.setVisibility(View.INVISIBLE);
					lv1.setEnabled(true);
				} else
				{
					pb1.setVisibility(View.VISIBLE);
					lv1.setEnabled(false);
				}
				// Log.i("====>>>","!!!!!!!!");
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		isRun = false;
		try
		{
			if (socket != null)
				socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
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

	class TcpControlClass extends Thread
	{
		// TODO TCPControlClass
		private boolean isConnected = false;

		@Override
		public void run()
		{
			Message msg;
			msg = Message.obtain();
			msg.obj = "display";
			msg.what = IS_PROGRESSBAR;
			mHandler.sendMessage(msg);
			while (!isConnected)
			{
				try
				{
					// step one connect to server
					socket = new Socket();
					// Timeout will throws ioexception
					socket.connect(socketAddress, 0);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream())), true);
					isConnected = true;
					Log.i("RUN", "Conected! " + isConnected + HOST + PORT);
				} catch (IOException ex)
				{
					ex.printStackTrace();
					Log.i("RUN", "IOException TimeOut: " + isConnected + HOST + PORT);
					msg = Message.obtain();
					msg.obj = "Failed to connect to TCP server!";
					msg.what = IS_DIALOG;
					mHandler.sendMessage(msg);
					while (reconnect[1])
						;
					if (reconnect[0] == CANCEL)
					{
						System.exit(0);
					}
					reconnect[1] = true;
					Log.i("RUN", "reconnect");
				}
			}// while(isConnected)
			msg = Message.obtain();
			msg.obj = null;
			msg.what = IS_PROGRESSBAR;
			mHandler.sendMessage(msg);
			while (isConnected)
			{
				try
				{
					if (socket.isConnected())
						if (socket.isInputShutdown())
						{
							recvCmd = in.readLine();
							Log.i("RUN recgCmd", recvCmd);
							if(recvCmd.equals("Exit"))
							{
								isConnected=false;
								break;
							}
							msg = Message.obtain();
							msg.obj = recvCmd;
							msg.what = IS_RECVCMD;
							mHandler.sendMessage(msg);
						}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			Log.i("RUN", "Thread tcp ends");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO onOptionsItemSelected
		switch (item.getItemId())
		{
		case R.id.mainExit:
			// show about dialog
			isRun = false;
			System.exit(0);
			break;
		case R.id.control:
			// connect tcp server
			new TcpControlClass().start();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
