package com.cyc.embeddedsystem;

import com.cyc.embededsystem.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WelcomeActivity extends Activity
{
	private Button cancelButton = null;
	private Button confirmButton = null;
	private EditText ipEditText = null;
	private String ipString = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		ipEditText = (EditText) findViewById(R.id.editIp);
		cancelButton = (Button) findViewById(R.id.btnCancel);
		confirmButton = (Button) findViewById(R.id.btnConfirm);
		cancelButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		confirmButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				ipString = ipEditText.getText().toString();
				if (ipString.matches("([0-9]{1,3}\\.){3}[0-9]{1,3}"))
				{
					Intent intent = new Intent();
					intent.setClass(WelcomeActivity.this, MainActivity.class);
					intent.putExtra("IP", ipString);
					startActivity(intent);
					finish();
				} else
				{
					Toast.makeText(WelcomeActivity.this, "¥ÌŒÛµƒ ‰»Î", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
