package com.cyc.mywork;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.os.Environment;

public class CycApplication extends Application
{
	private String path = null;
	private List<String> allTemp = null;
	private String serverIp=null;

	@Override
	public void onCreate()
	{
		String tmp = Environment.getExternalStorageDirectory().getPath();
		path = tmp + "/cycApp/";
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdir();
		}
		String tmpPath = path+"log/";
		file=new File(tmpPath);
		if (!file.exists())
		{
			file.mkdir();
		}
		tmpPath = path+"images/";
		file=new File(tmpPath);
		if (!file.exists())
		{
			file.mkdir();
		}
		allTemp = new ArrayList<String>();
		super.onCreate();
	}

	public String getPath()
	{
		return path;
	}

	public void addTemperature(String info)
	{
		allTemp.add(info);
		//Log.i("RUN", "allTemp " + allTemp.size());
	}

	public String getTemperature(int positon)
	{
		return allTemp.get(positon);
	}

	public String getLastTemperature()
	{
		return allTemp.get(allTemp.size() - 1);
	}

	public String[] getLastTemperature(int from)
	{
		int start, end;
		String[] tmp = new String[from];
		end = allTemp.size() - 1;
		start = end - from;
		//Log.i("RUN end", ""+end);
		//Log.i("RUN start", ""+start);
		if (start < 0)
			start = 0;
		for (int i = start, j = 0; i < end; i++, j++)
		{
			tmp[j] = allTemp.get(i);
			//Log.i("RUN tmp[j]", tmp[j]);
		}
		return tmp;
	}
	
	public void setIp(String ip)
	{
		serverIp=ip;
	}
	
	public String getIp()
	{
		return serverIp;
	}
}
