package com.cyc.animation;

import javax.microedition.lcdui.game.Sprite;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements Callback
{
	public static final String tag = "GameView";
	// 声明GameThread类实例
	GameThread gameThread;
	Bitmap bmp[] = new Bitmap[11];// 精灵对应的图像，
	Sprite spriteLed;
	int OnToOff[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };// 亮->灭
	int OffToOn[] = { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };// 灭->亮
	int currentOrder = 0;
	SurfaceHolder surfaceHolder = null;
	int hitNum = 0;

	public GameSurfaceView(Context context)
	{
		super(context);
		// 获取SurfaceHolder
		surfaceHolder = getHolder();
		// 添加回调对象
		surfaceHolder.addCallback(this);
		Resources res = context.getResources();
		bmp[0] = decodeResource(res, R.drawable.on_off);
		spriteLed = new Sprite(bmp[0], 99, 128);
		spriteLed.setFrameSequence(OffToOn);
		currentOrder = 0;// means off to on
		spriteLed.setPosition(MainActivity.instance.displayWidth / 2,
				MainActivity.instance.displyHeight - 300);
		gameThread = new GameThread(surfaceHolder);
	}

	private Bitmap decodeResource(Resources resources, int id)
	{
		TypedValue value = new TypedValue();
		resources.openRawResource(id, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTargetDensity = value.density;
		return BitmapFactory.decodeResource(resources, id, opts);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		gameThread.run = false;
	}

	// 子线程类，不断监听角色状态
	class GameThread extends Thread
	{
		SurfaceHolder surfaceHolder;
		boolean run = true;

		public GameThread(SurfaceHolder surfaceHolder)
		{
			this.surfaceHolder = surfaceHolder;
		}

		public void run()
		{
			int i = 0;
			while (run)
			{
				Canvas c = null;
				try
				{
					synchronized (surfaceHolder)
					{
						// 我们在屏幕上显示一个计数器，每隔1秒钟刷新一次
						c = surfaceHolder.lockCanvas();
						c.drawARGB(255, 255, 255, 255);
						// 绘制精灵
						spriteLed.paint(c);
						// 显示下一桢
						spriteLed.nextFrame();
						if (++i > 10)
						{
							if (currentOrder == 0)
								spriteLed.setFrameSequence(OffToOn);
							else
								spriteLed.setFrameSequence(OnToOff);
							i = 0;
						}
						Thread.sleep(20);
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					if (c != null)
					{
						surfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}
}
