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
	// ����GameThread��ʵ��
	GameThread gameThread;
	Bitmap bmp[] = new Bitmap[5];// �����Ӧ��ͼ��
	Sprite sprite0;// �����࣬��ʾ����
	boolean sprite0leftToRight = true, changeDirection = false;
	int toLeft[] = { 0, 1, 1, 2, 2, 3, 3, 4 };// ����ɵĶ���֡����˳��
	int toRight[] = { 5, 6, 6, 7, 7, 8, 8, 9 };// ���ҷɵĶ���֡����˳��
	int birdX = 0, birdY = 200, birdindex = 0;
	SurfaceHolder surfaceHolder = null;
	int hitNum = 0;
	// ����������
	private MediaPlayer player;
	private AudioManager am;
	private HashMap<Integer, Integer> soundPoolMap;// ��ע1
	private SoundPool soundPool;

	public GameSurfaceView(Context context)
	{
		super(context);
		// ��ȡSurfaceHolder
		surfaceHolder = getHolder();
		// ��ӻص�����
		surfaceHolder.addCallback(this);
		Resources res = context.getResources();
		// ע�����ֱ�ӻ��ͼ����Դ���õ����Ǿ������ŵĶ���
		bmp[0] = decodeResource(res, R.drawable.bird);
		// ͨ��һ��ͼ�񴴽����飬77��40��һ������Ĵ�С����ͼ��ָ�����ɸ�����֡
		sprite0 = new Sprite(bmp[0], 77, 40);
		// ���þ��鲥��˳��
		sprite0.setFrameSequence(this.toRight);
		sprite0.setPosition(birdX, birdY);
		// ����GameThread��ʵ��
		gameThread = new GameThread(surfaceHolder);
		initSounds(context);
		// ������������
		this.player.start();
		float v1 = (float) 0.2;
		this.player.setVolume(v1, v1);
		this.player.setLooping(true);
	}

	// ��ȡԭʼ��С
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
	{// ��ʼ�������ķ���
		/* ��ʼ��MediaPlayer���� */
		this.player = MediaPlayer.create(context, R.raw.backsound);
		/* ��ʼ��SoundPool���� */
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		soundPoolMap.put(1, soundPool.load(context, R.raw.baoza, 1));
		soundPoolMap.put(2, soundPool.load(context, R.raw.niaoming, 1));
	}

	/* ��SoundPoll���������ķ��� */
	public void playSound(int sound, int loop)
	{
		am = (AudioManager) MainActivity.instance.getSystemService(Context.AUDIO_SERVICE);
		am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		// ��ȡ�������ֵ��15���! .����100����
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		/* �������� */
		soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// ����gameThread
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
			{// ������
				birdX = -100;
				birdY = (int) Math.floor(Math.random() * (MainActivity.displayWidth - 200));
				hitNum++;
				playSound(1, 0);
				sprite0.setPosition(birdX, birdY);
			}

		}
		return super.onTouchEvent(arg0);
	}

	// ���߳��࣬���ϼ�����ɫ״̬
	class GameThread extends Thread
	{
		SurfaceHolder surfaceHolder;
		// run()�����п���ѭ���Ĳ�����
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
						// ��������Ļ����ʾһ����������ÿ��1����ˢ��һ��
						c = surfaceHolder.lockCanvas();
						c.drawARGB(255, 255, 255, 255);
						birdX += 20;
						if (birdX > MainActivity.displayWidth)
						{
							birdX = -200;
							birdY = (int) Math.floor(Math.random()
									* (MainActivity.displayWidth - 200));
							// �������
							playSound(2, 0);
							sprite0.setPosition(birdX, birdY);
						}
						// С��S���߶�
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
						// ���ƾ���
						sprite0.paint(c);
						// ��ʾ��һ��
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
