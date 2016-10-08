package com.example.ppp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.*;
import android.view.View;

public class MainActivity extends Activity {
	
	private Button confirmBtn;
	private EditText nameEditText, ageEditText;
	private TextView infoTextView;
	private String name;
	private int age;
	private String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		confirmBtn = (Button)findViewById(R.id.confirmBtn);
		nameEditText = (EditText)findViewById(R.id.nameEditText);
		ageEditText = (EditText)findViewById(R.id.ageEditText);
		infoTextView = (TextView)findViewById(R.id.infoTextView);
		
		nameEditText.setText("Enter you name here");
		ageEditText.setText("Enter you age here");
		infoTextView.setText("Nothing");
		
		nameEditText.setOnClickListener(nameEditTextOnClick);
		ageEditText.setOnClickListener(ageEditTextOnClick);
		confirmBtn.setOnClickListener(confirmBtnOnClick);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Button.OnClickListener confirmBtnOnClick = new Button.OnClickListener(){
		public void onClick(View v){
			name = nameEditText.getText().toString();
			age = Integer.parseInt(ageEditText.getText().toString());
			result = name + ' ' + age;
			infoTextView.setText(result);
		}
	};
	
	private EditText.OnClickListener nameEditTextOnClick = new EditText.OnClickListener(){
		public void onClick(View v){
			nameEditText.setText("");
		}
	};
	
	private EditText.OnClickListener ageEditTextOnClick = new EditText.OnClickListener(){
		public void onClick(View v){
			ageEditText.setText("");
		}
	};
	
	
	

}
