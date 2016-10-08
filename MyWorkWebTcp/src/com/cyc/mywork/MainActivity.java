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
import java.lang.reflect.Field;
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
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements Runnable
{
	protected String HOST = null;
	protected static final int PORT = 8888;
	protected Socket socket = null;
	protected SocketAddress socketAddress = null;
	protected BufferedReader in = null;
	protected PrintWriter out = null;
	protected String recvCmd = "";
	private TcpControlClass tcpControl = null;

	private FileOutputStream logTFos;
	private Date curDate = null;
	private String hour = null;
	private String date = null;
	private boolean hostChange = false;
	protected final boolean reconnect[] = { false, true };
	protected static final boolean CANCEL = false;
	protected static final boolean AGAIN = true;
	private static final String SMOKE_STATUS = "SMOKE";
	private static final String TEMP_STATUS = "TEMPE";
	private static final String HUMAN_STATUS = "HUMAN";
	private static final String LIGHT_STATUS = "LIGHT";
	private static final int IS_LIST = 1;
	private static final int IS_DIALOG = 2;
	private static final int IS_PROGRESSBAR = 3;
	private static final int IS_RECVCMD = 4;
	private static final int IS_EXIT = 5;
	private static final int IS_TIMER = 6;
	private static final boolean ON = true;
	private static final boolean OFF = false;
	private String currentSmoke = "No smoke detected!";
	private String currentTemp = "Current temperature: ";
	private String currentHuman = "No human body detected!";
	private String currentLight = "Light is: off";
	private Thread mThread = null;

	private TimerTask task;

	private boolean isRun = true;
	private boolean isConnectToWeb = false;
	private boolean LIGHT = OFF;
	private Timer timer;

	private ListView lv1 = null;
	private SimpleAdapter adapter = null;
	private ProgressBar pb1 = null;
	private SimpleDateFormat md = null;
	private SimpleDateFormat hms = null;

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

		md = new SimpleDateFormat("MMdd", Locale.US);
		hms = new SimpleDateFormat("HHmmss", Locale.US);

		timer = new Timer(true);

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
				// turn on the light
				if (LIGHT == ON)
				{
					if (sendCtrlCommand("LIGHT0"))
					{
						LIGHT = OFF;
						Log.i("RUN LIGHT", "" + LIGHT);
					}
				} else
				// turn off the light
				{
					if (sendCtrlCommand("LIGHT1"))
					{
						LIGHT = ON;
						Log.i("RUN LIGHT", "" + LIGHT);
					}
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
			Log.i("RUN", "" + arg2);
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
		map.put("info", currentLight);
		map.put("img", R.drawable.lightoff);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Contect Me");
		map.put("info", "CYC @GDUFS Creative Lab");
		map.put("img", R.drawable.about);
		list.add(map);

		return list;
	}

	protected boolean sendCtrlCommand(String cmd)
	{
		// TODO sendCtrlCommand
		if (out == null)
		{
			Toast.makeText(MainActivity.this, "Not in control mode!", Toast.LENGTH_SHORT).show();
		} else
		{
			if (socket.isConnected())
				if (!socket.isOutputShutdown())
				{
					out.println(cmd);
					return true;
				}
		}
		return false;
	}

	/*
	 */
	@Override
	public void run()
	{
		// TODO run
		HOST = ((CycApplication) getApplication()).getIp();
		socketAddress = new InetSocketAddress(HOST, PORT);

		Message msg;
		// prepare to get log txt
		URL aURL;
		BufferedReader reader;
		HttpURLConnection conn;
		curDate = new Date(System.currentTimeMillis());// get current time
		date = md.format(curDate);
		hour = hms.format(curDate);
		Log.i("RUN hour", hour);
		Log.i("RUN date", date);
		String savePath = ((CycApplication) getApplication()).getPath() + "log/T/";
		File file = new File(savePath);
		if (!file.exists())
		{
			file.mkdir();
		}
		while (!isConnectToWeb)
		{
			try
			{
				// 2. get today's temperature log
				aURL = new URL("http://" + HOST + "/log/T/" + date + ".txt");
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
					// Log.i("RUN add t", tmp);
				}
				logTFos.flush();
				// cancel progress bar
				msg = new Message();
				msg.obj = null;
				msg.what = IS_PROGRESSBAR;
				mHandler.sendMessage(msg);
				isConnectToWeb = true;
				isRun = true;
				//Log.i("RUN", "isConnectToWeb " + isConnectToWeb + date);
			} catch (Exception e)
			{
				e.printStackTrace();
				if (hostChange == false)
				{
					msg = new Message();
					msg.obj = "Failed to connect to Web server!\nMake sure your network is avaliable!";
					msg.what = IS_DIALOG;
					mHandler.sendMessage(msg);
				}
				while (reconnect[1])
					;
				if (reconnect[0] == CANCEL)
				{
					isRun = false;
					msg = new Message();
					msg.what = IS_EXIT;
					mHandler.sendMessage(msg);
				}
				reconnect[1] = true;
				//Log.i("RUN", "reconnect web");
			}
		}
		// Log.i("RUN", "isConnectToWeb " + isConnectToWeb + date);
		while (isRun)
		{
			// TODO while is run
			try
			{
				aURL = new URL("http://" + HOST + "/index.html");
				conn = (HttpURLConnection) aURL.openConnection();
				conn.connect();
				InputStream is = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is));
				String info = "";
				while ((info = reader.readLine()) != null)
				{
					// Log.i("RUN", "info " + info);
					msg = Message.obtain();
					msg.obj = info;
					msg.what = IS_LIST;
					mHandler.sendMessage(msg);
				}
				// get temperature every 5 second
				Thread.sleep(5000);
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			if (logTFos != null)
				logTFos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Log.i("RUN", "Thread web ends");
	}

	public void showDialog(String title, String msg)
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.setNegativeButton("Eixt", new OnClickListener()
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
		dialog.setPositiveButton("Try Again", new OnClickListener()
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
		// TODO analyzeListData
		if (string == null)
			return;
		int strLen = string.length();
		boolean warning = false;
		if (strLen < 6)
			return;
		String tmpFirstString = string.substring(0, 5);
		String tmpSecondString = string.substring(5, strLen);
		// Log.i("RUN recv", tmpFirstString);
		if (tmpFirstString.equals(TEMP_STATUS))
		{
			currentTemp = "Current Temperture: " + string.substring(5, strLen) + "℃";
			curDate = new Date(System.currentTimeMillis());// 获取当前时间
			hour = hms.format(curDate);
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

		} else if (tmpFirstString.equals(SMOKE_STATUS))
		{
			int smokeValue=Integer.parseInt(tmpSecondString);
			if (smokeValue<200)
			{
				currentSmoke = getString(R.string.smoke);
				warning = true;
			} else
			{
				currentSmoke = getString(R.string.nosmoke);
				warning = false;
			}
			updateList(0, currentSmoke, warning);
		} else if (tmpFirstString.equals(HUMAN_STATUS))
		{
			if (tmpSecondString.equals("1"))
			{
				currentHuman = getString(R.string.human);
				warning = true;
			} else
			{
				currentHuman = getString(R.string.nohuman);
				warning = false;
			}
			updateList(2, currentHuman, warning);
		} else if (tmpFirstString.equals(LIGHT_STATUS))
		{
			if (tmpSecondString.equals("1"))
			{
				currentLight = getString(R.string.lighton);
				warning = true;
			} else
			{
				currentLight = getString(R.string.lightoff);
				warning = false;
			}
			updateList(4, currentLight, warning);
		}

	}

	private void updateList(int position, String data, boolean warning)
	{
		// TODO updateList
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) lv1.getItemAtPosition(position);
		map.put("info", data);
		if (warning)
		{
			if (position != 4)
				map.put("img", R.drawable.warning);
			else
			{
				map.put("img", R.drawable.lighton);
			}
		} else
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
				break;
			case 4:
				map.put("img", R.drawable.lightoff);
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
			case IS_RECVCMD:
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
			case IS_TIMER:
				new Thread(MainActivity.this).start();
				timer.cancel();
				break;
			case IS_EXIT:
				finish();
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
			//System.out.println(msg);
			if (mActivity.get() == null)
			{
				return;
			}
		}
	}

	class TcpControlClass extends Thread
	{
		// TODO TCPControlClass
		private boolean isTcpConnected = false;

		@Override
		public void run()
		{
			Message msg;
			msg = Message.obtain();
			msg.obj = "display";
			msg.what = IS_PROGRESSBAR;
			mHandler.sendMessage(msg);
			// try connect until success or canceled by user
			while (!isTcpConnected)
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
					isTcpConnected = true;
					Log.i("RUN", "Conected! " + isTcpConnected + HOST + PORT);
				} catch (IOException ex)
				{
					ex.printStackTrace();
					Log.i("RUN", "IOException TimeOut: " + isTcpConnected + HOST + PORT);
					msg = Message.obtain();
					msg.obj = "Failed to connect to TCP server!";
					msg.what = IS_DIALOG;
					mHandler.sendMessage(msg);
					while (reconnect[1])
						;
					if (reconnect[0] == CANCEL)
					{
						Log.i("RUN break tcp", recvCmd);
						break;
					}
					reconnect[1] = true;
					Log.i("RUN", "reconnect");
				}
			}// while(isConnected)
			Log.i("RUN recvCmd", "12131321");
			// cancel progress bar
			msg = Message.obtain();
			msg.obj = null;
			msg.what = IS_PROGRESSBAR;
			mHandler.sendMessage(msg);
			// read data from tcp server
			while (isTcpConnected)
			{
				try
				{
					if (socket.isConnected())
						if (!socket.isInputShutdown())
						{
							// readLine()will be blocked until reading is
							// avaliable
							recvCmd = in.readLine();
							Log.i("RUN recvCmd", "" + recvCmd);
							// send message update listview
							if (recvCmd != null)
							{
								msg = Message.obtain();
								msg.obj = recvCmd;
								msg.what = IS_RECVCMD;
								mHandler.sendMessage(msg);
							}
						}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			Log.i("RUN", "Thread tcp ends");
		}

		public void stopRunning()
		{
			isTcpConnected = false;
			try
			{
				if (socket != null)
				{
					socket.close();
					Log.i("RUN stop running", "socket closed");
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}


	public void ShowInputDialog()
	{
		// TODO show input dialog
		// use layout inflater to find mydialog layout
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		final View textEntryView = inflater.inflate(R.layout.mydialog, null);
		TextView oldIp = (TextView) textEntryView.findViewById(R.id.tvdlg);
		oldIp.setText("Current IP: " + HOST);
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setView(textEntryView);
		dialog.setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				// find ip setting textview throud textEntryView
				try
				{
					Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(dialog, true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				EditText secondPwd = (EditText) textEntryView.findViewById(R.id.ipsetting);
				// secondPwd.setText(HOST);
				String tmp;
				tmp = secondPwd.getText().toString();
				if (tmp.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}"))
				{
					Log.i("RUN", "tmp host: " + tmp);
					if (tmp == HOST)
						return;
					HOST = tmp;
					((CycApplication) getApplication()).setIp(HOST);
					// delay a second to let the old thread exit and then
					// restart the thread
					isRun = false;
					task = new TimerTask()
						{
							public void run()
							{
								isRun=true;
								Message message = new Message();
								message.what = IS_TIMER;
								mHandler.sendMessage(message);
							}
						};
					timer = new Timer();
					timer.schedule(task, 1000, 1000);
					dialog.dismiss();
				} else
				{
					/*
					 * try { Field field = dialog.getClass().getSuperclass()
					 * .getDeclaredField("mShowing"); field.setAccessible(true);
					 * field.set(dialog, false); } catch (Exception e) {
					 * e.printStackTrace(); }
					 */
					Toast.makeText(MainActivity.this, "Wrong IP Format", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
				// MainActivity.this.finish();
			}
		});
		dialog.create();
		dialog.show();
		Log.i("RUN", "Input dialog");
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
			isRun = false;
			finish();
			break;
		case R.id.control:
			if (item.getTitle().equals(getString(R.string.control)))
			{
				tcpControl = new TcpControlClass();
				tcpControl.start();
				item.setTitle(getString(R.string.releasecontrol));
			} else
			{
				tcpControl.stopRunning();
				item.setTitle(getString(R.string.control));
			}
			break;
		case R.id.setip:
			ShowInputDialog();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
