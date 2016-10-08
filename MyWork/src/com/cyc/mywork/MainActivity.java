package com.cyc.mywork;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity implements Runnable
{
	// private Button btn_login = null;
	private static final String HOST = "192.168.235.10";
	private static final int PORT = 8088;
	private boolean isConnected = false;
	private Socket socket = null;
	private SocketAddress socketAddress = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String recString = "";
	private String sendString = "";
	private boolean videoPlaying = false;

	private final boolean reconnect[] = { false, true };
	private static final boolean CANCEL = false;
	private static final boolean AGAIN = true;

	private static final String SMOKE_STATUS = "SMOKE";
	private static final String TEMP_STATUS = "TEMPE";
	private static final String HUMAN_STATUS = "HUMAN";
	private static final String VIDEO_STATUS = "VIDEO";
	private String currentSmoke = "No smoke detected!";
	private String currentTemp = "current temperature: ";
	private String currentHuman = "No human body detected!";
	private Thread mThread = null;

	private boolean isRun = true;

	protected static final AdapterView<ListAdapter> myListView = null;
	private static final int IS_LIST = 1;
	private static final int IS_DIALOG = 2;
	private ListView lv1 = null;
	private SimpleAdapter adapter = null;

	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		Log.i("RUN", "setContentView");
		adapter = new SimpleAdapter(this, getData(), R.layout.mylist,
				new String[] { "title", "info", "img" }, new int[] {
						R.id.title, R.id.info, R.id.img });
		setListAdapter(adapter);
		lv1 = getListView();
		lv1.setBackgroundColor(Color.TRANSPARENT);
		lv1.setOnItemClickListener(myListener);
		// Avoid the bg becomes black when scrolling
		lv1.setCacheColorHint(Color.TRANSPARENT);
		socketAddress = new InetSocketAddress(HOST, PORT);
		// char[] imgByte = new char [1024];
		// String imgString=null;
		// try
		// {
		// FileReader fileReader=new
		// FileReader("/mnt/sdcard/cycApp/image/5.jpg");
		// while(fileReader.read(imgByte)!=-1)
		// imgString+=imgByte;
		// fileReader.close();
		// } catch (FileNotFoundException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		try
		{
			((ShareDataApplication) getApplication())
					.setShareBitmap(toByteArray("/mnt/sdcard/cycApp/image/5.jpg"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((ShareDataApplication) getApplication())
				.setString("This image is from MainActivity");

		mThread = new Thread(MainActivity.this);
		mThread.start();

	}

	private OnItemClickListener myListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) lv1
					.getItemAtPosition(arg2);
			String title = (String) map.get("title");
			String content = (String) map.get("info");
			Intent myIntent = new Intent();
			sendString = title + ":" + content;

			Log.i("RUN", "Getting: " + sendString);
			if (title.equals("Temperature"))
			{
				// change text dynamically
				map.put("info", "Damn!");
				adapter.notifyDataSetChanged();
				Log.i("RUN", "setClass: TemActivity.class!");
				myIntent.setClass(MainActivity.this, TemActivity.class);
				startActivity(myIntent);
			} else if (title.equals("Video"))
			{
				Log.i("RUN", "setClass: VideoActivity.class!");
				videoPlaying = true;
				myIntent.setClass(MainActivity.this, VideoActivity.class);
				startActivity(myIntent);
			} else if (title.equals("Smoke"))
			{
				// To do

			} else if (title.equals("Human"))
			{
				myIntent.setClass(MainActivity.this, HumanActivity.class);
				startActivity(myIntent);
			} else
			{
				// To do
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("About this program:")
						.setMessage(
								"Created by CYC @GDUFS, Email:congpp@qq.com")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog,
											int which)
									{
									}
								}).show();
				return;
			}
			Toast.makeText(getApplicationContext(),
					arg2 + "\t" + title + "\t" + content, Toast.LENGTH_SHORT)
					.show();
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
		map.put("title", "Contect Me");
		map.put("info", "CYC @GDUFS Creative Lab");
		map.put("img", R.drawable.about);
		list.add(map);

		return list;
	}

	@Override
	public void run()
	{
		try
		{
			while (isRun)
			{
				// Connect if not connected
				while (!isConnected)
				{
					try
					{
						socket = new Socket();
						// Timeout will throws ioexception
						socket.connect(socketAddress, 5000);
						in = new BufferedReader(new InputStreamReader(
								socket.getInputStream()));
						out = new PrintWriter(
								new BufferedWriter(new OutputStreamWriter(
										socket.getOutputStream())), true);
						isConnected = true;
						Log.i("RUN", "Conected! " + isConnected + HOST + PORT);
					} catch (IOException ex)
					{
						ex.printStackTrace();
						Log.i("RUN", "IOException TimeOut: " + isConnected
								+ HOST + PORT);
						// Looper.prepare();
						// showDialog();
						// Looper.loop();
						Message msg = Message.obtain();
						msg.obj = 1;
						msg.what = IS_DIALOG;
						mHandler.sendMessage(msg);
						while (reconnect[1])
							;
						if (reconnect[0] == CANCEL)
						{
							isRun = false;
							break;// break while(!isConnected)...
						}
						reconnect[1] = true;
						Log.i("RUN", "reconnect");

					}
				}
				if (socket.isConnected())
				{
					if (!socket.isInputShutdown())
					{
						if ((recString = in.readLine()) != null)
						{
							Log.i("RUN", "Readline" + recString);
							// You cannot directly access the EditText in a new
							// Thread
							// like use: "ed_his.append(content)" here;
							// So, a handler is necessary!
							out.println("GETIT\n");
							Message msg = Message.obtain();
							msg.obj = 1;
							msg.what = IS_LIST;
							mHandler.sendMessage(msg);
							// analyzeListData(recString);
						}
					}

				}
			}
		} catch (Exception e)
		{
			Log.e("RUN", "Error" + Thread.currentThread().getId());
			e.printStackTrace();
		}

		Log.i("RUN", "Thread ends");
		System.exit(0);
	}

	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setTitle("Oops!");
		dialog.setMessage("Failed to connect to server:" + HOST);
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
			currentTemp = "Current Temperture: " + string.substring(5, strLen)
					+ "¡æ";
			if (Integer.parseInt(string.substring(5, strLen)) > 60)
				warning = true;
			else
				warning = false;
			updateList(1, currentTemp, warning);
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
		} else if (tmpFirstString.equals(VIDEO_STATUS))
		{
			// START TO PLAY VIDEO HERE
			// while(videoPlaying)
			// {
			// ;
			// }
		} else
		{
			return;
		}

	}

	private void updateList(int position, String data, boolean warning)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> map = (HashMap<String, Object>) lv1
				.getItemAtPosition(position);
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

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			Log.i("mHandler", "Thread id " + Thread.currentThread().getId());
			switch (msg.what)
			{
			case IS_LIST:
				analyzeListData(recString);
				break;
			case IS_DIALOG:
				showDialog();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("RUN", "onActivityResult: " + resultCode);
		if (resultCode == RESULT_OK)
		{
			videoPlaying = false;
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		isRun = false;
	}
}
