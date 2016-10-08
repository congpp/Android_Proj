package com.cyc.mywork;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ShareDataApplication extends Application
{
	private Bitmap shareBitmap=null;
	private String shareString=null;
	private long status=0;
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
	}
	
	public Bitmap getShareBitmap()
	{
		return shareBitmap;
	}
	
	public void setShareBitmap(byte[] bytes)
	{
		status++;
		shareBitmap=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		Log.i("RUN","shareBitmap: "+shareBitmap.getHeight());
	}
	public void setShareBitmap(Bitmap bitmap)
	{
		status++;
		shareBitmap=bitmap;
	}
	public String getString()
	{
		return shareString;
	}
	public void setString(String string)
	{
		shareString=string;
	}
	public long getStatus()
	{
		return status;
	}
}
