package com.cyc.listview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends ListActivity
{

	protected static final AdapterView<ListAdapter> myListView = null;
	private ListView lv1 = null;
	private SimpleAdapter adapter=null;

	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		adapter = new SimpleAdapter(this, getData(),
				R.layout.mylist, new String[] { "title", "info", "img" },
				new int[] { R.id.title, R.id.info, R.id.img });
		setListAdapter(adapter);
		lv1 = getListView();
		lv1.setBackgroundResource(R.drawable.bg);
		lv1.setOnItemClickListener(myListener);
		//Avoid the bg becomes black when scrolling
		lv1.setCacheColorHint(Color.TRANSPARENT);
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
			String str = "Someone click you! damn!";
			
			Log.i("On", title);
			if (title.equals("Smoke"))
			{
				//change text dynamically
				map.put("info", str);
				adapter.notifyDataSetChanged();
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
		map.put("info", "No smoke detected...");
		map.put("img", R.drawable.smoke);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Temperature");
		map.put("info", "Current temperature: 36 ¡æ");
		map.put("img", R.drawable.tem);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Human");
		map.put("info", "No human detected...");
		map.put("img", R.drawable.human);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("title", "Video");
		map.put("info", "Click to play...");
		map.put("img", R.drawable.video);
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Contect Me");
		map.put("info", "CYC @GDUFS Creative Lab Mar. 2014");
		map.put("img", R.drawable.about);
		list.add(map);

		return list;
	}
}