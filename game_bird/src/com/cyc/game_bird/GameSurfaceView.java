package com.cyc.game_bird;

import java.util.HashMap;

import javax.microedition.lcdui.game.Sprite;

import com.example.game_3.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements Callback
{
	public static final String tag = "GameView";
	// 声明GameThread类实例
	GameThread gameThread;
	Bitmap bmp[] = new Bitmap[5];// 精灵对应的图像，
	Sprite sprite0;// 精灵类，表示飞鸟
	boolean sprite0leftToRight = true, changeDirection = false;
	int toLeft[] = { 0, 1, 1, 2, 2, 3, 3, 4 };// 向左飞的动画帧播放顺序
	int toRight[] = { 5, 6, 6, 7, 7, 8, 8, 9 };// 向右飞的动画帧播放顺序
	int birdX = 0, birdY = 200, birdindex = 0;
	SurfaceHolder surfaceHolder = null;
	int hitNum = 0;
	// 播放音乐类
	private MediaPlayer player;
	private AudioManager am;
	private HashMap<Integer, Integer> soundPoolMap;// 备注1
	private SoundPool soundPool;

	public GameSurfaceView(Context context)
	{
		super(context);
		// 获取SurfaceHolder
		surfaceHolder = getHolder();
		// 添加回调对象
		surfaceHolder.addCallback(this);
		Resources res = context.getResources();
		// 注意如果直接获得图像资源，得到的是经过缩放的对象，
		bmp[0] = decodeResource(res, R.drawable.bird);
		// 通过一个图像创建精灵，77，40是一个精灵的大小，该图像分割成若干个精灵帧
		sprite0 = new Sprite(bmp[0], 77, 40);
		// 设置精灵播放顺序
		sprite0.setFrameSequence(this.toRight);
		sprite0.setPosition(birdX, birdY);
		// 创建GameThread类实例
		gameThread = new GameThread(surfaceHolder);
		initSounds(context);
		// 启动背景音乐
		this.player.start();
		float v1 = (float) 0.2;
		this.player.setVolume(v1, v1);
		this.player.setLooping(true);
	}

	// 获取原始大小
	private Bitmap decodeResource(Resources resources, int id)
	{
		TypedValue value = new TypedValue();
		resources.openRawResource(id, value);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inTargetDensity = value.density;
		return BitmapFactory.decodeResource(resources, id, opts);
	}

	@SuppressLint("UseSparseArrays")
	public void initSounds(Context context)
	{// 初始化声音的方法
		/* 初始化MediaPlayer对象 */
		this.player = MediaPlayer.create(context, R.raw.backsound);
		/* 初始化SoundPool对象 */
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(context, R.raw.baoza, 1));
		soundPoolMap.put(2, soundPool.load(context, R.raw.niaoming, 1));
	}

	/* 用SoundPoll播放声音的方法 */
	public void playSound(int sound, int loop)
	{
		am = (AudioManager) MainActivity.instance.getSystemService(Context.AUDIO_SERVICE);
		am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// 获取最大音量值（15最大! .不是100！）
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		/* 播放声音 */
		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// 启动gameThread
		gameThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub
		gameThread.run = false;
		this.player.stop();
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		if (arg0.getAction() == MotionEvent.ACTION_DOWN)
		{
			int ax = (int) arg0.getX();
			int ay = (int) arg0.getY();
			if (ax > birdX && ax < birdX + 77 && ay > birdY && ay < birdY + 40)
			{// 触摸到
				birdX = -100;
				birdY = (int) Math.floor(Math.random() * (MainActivity.displayWidth - 200));
				hitNum++;
				playSound(1, 0);
				sprite0.setPosition(birdX, birdY);
			}

		}
		return super.onTouchEvent(arg0);
	}

	// 子线程类，不断监听角色状态
	class GameThread extends Thread
	{
		SurfaceHolder surfaceHolder;
		// run()函数中控制循环的参数。
		boolean run = true;

		public GameThread(SurfaceHolder surfaceHolder)
		{
			this.surfaceHolder = surfaceHolder;
		}

		public void run()
		{
			int lasty = 0;
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
						birdX += 20;
						if (birdX > MainActivity.displayWidth)
						{
							birdX = -200;
							birdY = (int) Math.floor(Math.random()
									* (MainActivity.displayWidth - 200));
							// 播放鸟叫
							playSound(2, 0);
							sprite0.setPosition(birdX, birdY);
						}
						// 小鸟S型走动
						int y1 = (int) Math.random() * 10;
						int y = 0;
						y1 %= 2;
						if (y1 == 0)
						{
							int tmp = (int) (Math.random() * 100);
							y = tmp - lasty;
							lasty = tmp;
						} else
						{
							int tmp = (int) (Math.random() * (-100));
							y = tmp + lasty;
							lasty = tmp;
						}
						sprite0.move(40, y);
						// 绘制精灵
						sprite0.paint(c);
						// 显示下一桢
						sprite0.nextFrame();
						c.drawText("" + hitNum, 100, 100, new Paint());
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
