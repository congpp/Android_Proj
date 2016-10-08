package com.cyc.pro2;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

	ListView mainListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setListAdapter(new ArrayAdapter<String>(this,R.layout.mylistview,R.array.list));
		mainListView = getListView();
		mainListView.setTextFilterEnabled(false);
		mainListView.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(getApplicationContext(),((TextView)view).getText(),Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
