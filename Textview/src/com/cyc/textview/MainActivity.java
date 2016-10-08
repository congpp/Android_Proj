package com.cyc.textview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//way 1 to add textview, not recommand.
//		TextView tv=new TextView(this);
//		tv.setText("Hello Congge!");
//		setContentView(tv);
		//way 2 to add textview is to edit xml file;
		
		//use Html to change text view color;
		TextView tv=(TextView)findViewById(R.id.tv);
//		tv.setText(Html.fromHtml("What you know you know<font color=blue> what you don't know " +
//				"</font>you don't know!"));
		
		//another way to change color
//		String str="What you know you know what you don't know you don't know" ;
//		SpannableStringBuilder style=new SpannableStringBuilder(str);
//		style.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 20,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		style.setSpan(new ForegroundColorSpan(Color.GREEN),20, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		style.setSpan(new ForegroundColorSpan(Color.GRAY), 30, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		tv.setText(style);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
