package com.cyc.mywork;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements Runnable
{
	private Button btnConfirm;
	private EditText editIp;
	private String ip = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		editIp = (EditText) findViewById(R.id.editIp);
		btnConfirm.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ip = editIp.getText().toString();
				Log.i("RUN ",ip);
				if (ip.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}"))
				{
					((CycApplication) getApplication()).setIp(ip);
					new Thread(WelcomeActivity.this).start();
				} else
				{
					Toast.makeText(WelcomeActivity.this, "Wrong input: " + ip, Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Override
	public void run()
	{
		try
		{
			URL url = new URL("http://" + ip + "/index.html");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.connect();
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
		} catch (IOException e)
		{
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 0;
			mHandler.sendMessage(msg);
		} 
	}

	@SuppressLint("HandlerLeak")
	public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 1)
			{
				Intent mainIntent = new Intent();
				mainIntent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(mainIntent);
				WelcomeActivity.this.finish();
			} else
			{
				Toast.makeText(WelcomeActivity.this, "Cannot connect to: " + ip, Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

}
