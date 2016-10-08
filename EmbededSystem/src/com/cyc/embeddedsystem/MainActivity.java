package com.cyc.embeddedsystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.cyc.embededsystem.R;

public class MainActivity extends Activity implements Runnable
{

	private String HOST = null;;
	private static final int PORT = 8888;
	private Socket socket = null;
	private boolean isRun = false;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private ImageView iv = null;
	private Button btn = null;
	private boolean isOpen = false;
	private Timer timer;
	private TimerTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.btn);
		iv = (ImageView) findViewById(R.id.img);
		btn.setEnabled(false);
		btn.setOnClickListener(listener);

		Intent intent = getIntent();
		HOST = intent.getStringExtra("IP");
		if (HOST == null)
		{
			// 获取wifi服务 灯已经将在被打开关闭时出错
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			// 判断wifi是否开启
			if (!wifiManager.isWifiEnabled())
			{
				Toast.makeText(MainActivity.this, "Wifi还没有打开啊，亲~", Toast.LENGTH_LONG).show();
			} else
			{
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				int ipAddress = wifiInfo.getIpAddress();
				HOST = intToIp(ipAddress);
				Log.i("RUN host ip", HOST);
				HOST = HOST.substring(0, HOST.lastIndexOf('.') + 1);
				HOST += "1";
				Log.i("RUN server ip", HOST);
				if (HOST != null)
				{
					Toast.makeText(MainActivity.this, "自动设置IP为：" + HOST, Toast.LENGTH_SHORT).show();
					new Thread(MainActivity.this).start();
				}
			}
		} else
		{
			new Thread(MainActivity.this).start();
		}
		task = new TimerTask()
		{

			@Override
			public void run()
			{
				Message msg = new Message();
				msg.what = 101010;
				mHandler.sendMessage(msg);
			}
		};
		timer = new Timer();
		timer.schedule(task, 1000, 1000);
	}

	public OnClickListener listener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			if (socket.isConnected())
			{
				if (!socket.isOutputShutdown())
				{
					if (isOpen)
					{
						out.println("0");
					} else
					{
						out.println("1");
					}
				}
			}
		}
	};

	private String intToIp(int i)
	{
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "."
				+ (i >> 24 & 0xFF);
	}

	View textEntryView;
	RadioButton openButton;
	RadioButton closeButton;

	public void ShowInputDialog()
	{
		// TODO show input dialog
		// use layout inflater to find mydialog layout
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		textEntryView = inflater.inflate(R.layout.mydialog, null);
		openButton = (RadioButton) textEntryView.findViewById(R.id.radio0);
		closeButton = (RadioButton) textEntryView.findViewById(R.id.radio1);
		AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
		dialog.setView(textEntryView);
		dialog.setPositiveButton("確定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
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
				String tmp;
				tmp = secondPwd.getText().toString();
				if (tmp.matches("[012]{1}[0-9]{1}:[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}"))
				{
					if (tmp.charAt(0) == '2')
						if (tmp.charAt(1) > '4')
						{
							Toast.makeText(MainActivity.this, "错误的输入", Toast.LENGTH_SHORT).show();
							return;
						}
					Log.i("RUN", "tmp host: " + tmp);
					if (openButton.isChecked())
					{
						tmp = "21" + tmp;
					} else if (closeButton.isChecked())
					{
						tmp = "20" + tmp;
					}
					if (socket.isConnected())
					{
						if (!socket.isOutputShutdown())
						{
							if (isOpen)
							{
								out.println(tmp);
							} else
							{
								out.println(tmp);
							}
						}
					}
					dialog.dismiss();
				} else
				{
					Toast.makeText(MainActivity.this, "错误的输入", Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.dismiss();
			}
		});
		dialog.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.setting:
			ShowInputDialog();
			break;
		case R.id.exit:
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void run()
	{
		// TODO run
		Message msg = new Message();
		try
		{
			socket = new Socket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
			out.println("3");
			String get = in.readLine();
			Log.i("RUN", "readline：" + get + "\n");
			msg = new Message();
			msg.obj = get;
			msg.what = 12345;
			mHandler.sendMessage(msg);
			isRun = true;
		} catch (IOException ex)
		{
			msg = new Message();
			msg.what = 23456;
			mHandler.sendMessage(msg);
			isRun = false;
			ex.printStackTrace();
		}
		while (isRun)
		{
			if (socket.isConnected())
			{
				if (!socket.isInputShutdown())
				{
					try
					{
						String get = in.readLine();
						//Log.i("RUN", "readline：" + get + "\n");
						msg = new Message();
						msg.obj = get;
						msg.what = 12345;
						mHandler.sendMessage(msg);
					} catch (IOException e)
					{
						e.printStackTrace();
						isRun = false;
					}
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 12345)
			{
				btn.setEnabled(true);
				if(msg.obj==null)
					return;
				if (((String) msg.obj).charAt(0) == '1')
				{
					isOpen = true;
					iv.setImageResource(R.drawable.on);
					btn.setBackgroundResource(R.drawable.switch_on);
				} else if (((String) msg.obj).charAt(0) == '0')
				{
					isOpen = false;
					iv.setImageResource(R.drawable.off);
					btn.setBackgroundResource(R.drawable.switch_off);
				} else if (((String) msg.obj).charAt(0) == '2')
				{
				} else
				{
					Toast.makeText(MainActivity.this, "接收到未知命令", Toast.LENGTH_SHORT).show();
				}
			} else if (msg.what == 23456)
			{
				Toast.makeText(MainActivity.this, "无法连接到服务器", Toast.LENGTH_LONG).show();
			} else if (msg.what == 101010)
			{
				if (socket!=null && socket.isConnected())
				{
					if (!socket.isOutputShutdown())
					{
						out.println("3");
					}
				}
			}
		}
	};

	@Override
	protected void onDestroy()
	{
		timer.cancel();
		isRun = false;
		super.onDestroy();
	}

}
