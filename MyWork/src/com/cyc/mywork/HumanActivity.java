package com.cyc.mywork;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.view.GestureDetector.OnGestureListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HumanActivity extends Activity implements OnTouchListener,
		OnGestureListener
{
	private static final float FLING_MIN_DISTANCE = 100;
	private static final float FLING_MIN_VELOCITY = 200;
	private TextView tvh = null;
	private ImageView ivh = null;
	private File[] fileList = null;
	private String imagePath = null;
	private int imgIndex = 0;
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_human);

		tvh = (TextView) findViewById(R.id.tvh);
		tvh.setText("Start!");
		ivh = (ImageView) findViewById(R.id.ivh);
		ivh.setClickable(true);
		ivh.setLongClickable(true);
		ivh.setFocusable(true);
		mGestureDetector = new GestureDetector(this, this);
		ivh.setOnTouchListener(this);
		mGestureDetector.setIsLongpressEnabled(true);
		if ((imagePath = isSdcard()) != null)
		{
			imagePath += "/cycApp/image";
			File file = new File(imagePath);
			if (!file.exists())
			{
				file.mkdirs();
			} else
			{
				fileList = file.listFiles();
			}
		} else
		{
			Toast.makeText(this, "Cannot open sd card!", Toast.LENGTH_SHORT)
					.show();
		}
		ivh.setImageBitmap(BitmapFactory.decodeFile("/mnt/sdcard/cycApp/image/5.jpg"));
		Log.i("RUN", imagePath+fileList.length+Thread.currentThread().getId());
	}

	public void changeImageView()
	{
		File imgFile = fileList[imgIndex];
		if (imgFile.isFile())
		{
			String filename = imgFile.getName();
			Log.i("RUN", filename);
			int index = filename.lastIndexOf(".");
			if (index <= 0)
			{
				ivh.setImageResource(R.drawable.warning);
				Toast.makeText(this, "Wrong image file: " + filename,
						Toast.LENGTH_SHORT).show();
				return;
			}
			String fileExtention = filename.substring(index).toLowerCase(
					Locale.US);
			Log.i("RUN", fileExtention);
			if (fileExtention.equals(".jpg") || fileExtention.equals(".jpeg")
					|| fileExtention.equals(".bmp")
					|| fileExtention.equals(".png")
					|| fileExtention.equals(".gif"))
			{
				tvh.setText(filename);
				ivh.setImageBitmap(BitmapFactory.decodeFile(imagePath + '/' + filename));
			}
			Log.i("RUN", "changeImageView: " + imagePath + '/' + filename
					+ fileList.length);
		}
	}

	private String isSdcard()
	{
		File sdcardDir = null;
		boolean isSDExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (isSDExist)
		{
			// 如果存在SDcard 就找到跟目录
			sdcardDir = Environment.getExternalStorageDirectory();
			return sdcardDir.toString();
		} else
		{
			return null;
		}
	}

//	private Bitmap getDiskBitmap(String pathString)
//	{
//		Bitmap bitmap = null;
//		try
//		{
//			File file = new File(pathString);
//			if (file.exists())
//			{
//				bitmap = BitmapFactory.decodeFile(pathString);
//				Log.i("RUN","getDiskBitmap: "+pathString);
//			}
//		} catch (Exception e)
//		{
//			// TODO: handle exception
//		}
//
//		return bitmap;
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		// TODO Auto-generated method stub
		Log.i("RUN", "onTouch");
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)

	{
		// e1：第1个ACTION_DOWN MotionEvent
		// e2：最后一个ACTION_MOVE MotionEvent
		// velocityX：X轴上的移动速度，像素/秒
		// velocityY：Y轴上的移动速度，像素/秒
		// 触发条件 ：
		// X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒
		// Log.i("RUN", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY)
		{
			// left
			imgIndex--;
			Log.i("RUN", "onFling left"+imgIndex);
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY)
		{
			// right
			imgIndex++;
			Log.i("RUN", "onFling right" + imgIndex);
		}
		imgIndex = Math.abs(imgIndex % (fileList.length));
		Log.i("RUN", "onFling after" + imgIndex+Thread.currentThread().getId());
		changeImageView();
		//mHandler.sendMessage(mHandler.obtainMessage());
		return true;

	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		// TODO Auto-generated method stub

		Log.i("RUN", "onDown");
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
		// TODO Auto-generated method stub

		Log.i("RUN", "onLongPress");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		// TODO Auto-generated method stub

		Log.i("RUN", "onScroll");
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
		// TODO Auto-generated method stub
		Log.i("RUN", "onShowPress");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		// TODO Auto-generated method stub
		Log.i("RUN", "onSingleTapUp");
		return false;
	}
	
//	public Handler mHandler = new Handler()
//	{
//		public void handleMessage(Message msg)
//		{
//			super.handleMessage(msg);
//			Log.i("mHandler", "Thread id " + Thread.currentThread().getId());
//			changeImageView();
//		}
//	};
}
