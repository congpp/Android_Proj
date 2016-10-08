package com.cyc.button;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.*;

public class MainActivity extends Activity
{
	private Button btn1=null;
	private Button btn2=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn1=(Button)findViewById(R.id.btn1);
		btn2=(Button)findViewById(R.id.btn2);
		//btn1.setOnClickListener(new ButtonClick());
//		btn1.setOnClickListener(new OnClickListener()
//		{
//			
//			@Override
//			public void onClick(View v)
//			{
//				// TODO Auto-generated method stub
//				Toast.makeText(MainActivity.this, 
//						"You have clicked the button",Toast.LENGTH_SHORT).show();
//			}
//		});
		
		btn1.setOnClickListener(listener);
		btn2.setOnClickListener(listener);
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			Button btn=(Button)v;
			switch (btn.getId())
			{
			case R.id.btn1:
				Toast.makeText(MainActivity.this, 
						"You have clicked the button 1",Toast.LENGTH_SHORT).show();
				break;
			case R.id.btn2:
				Toast.makeText(MainActivity.this, 
						"You have clicked the button 2",Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
//
//class ButtonClick implements OnClickListener
//{
//	@Override
//	public void onClick(View v)
//	{
//		// TODO Auto-generated method stub
//		System.out.print("You have clicked the button");
//	}
//}
