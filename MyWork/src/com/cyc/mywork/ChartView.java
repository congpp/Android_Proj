package com.cyc.mywork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ChartView extends View
{
	private int XPoint = 80; // 原点的X坐标
	private int YPoint = 260; // 原点的Y坐标
	private int XScale = 55; // X的刻度长度
	private int YScale = 40; // Y的刻度长度
	private int XLength = 380; // X轴的长度
	private int YLength = 240; // Y轴的长度
	private String[] XLabel; // X的刻度
	private String[] YLabel; // Y的刻度
	private String[] Data; // 数据
	private String Title; // 显示的标题
	private int screenW=480;
	private int screenH=800;
	private float imgWScale=1;
	private float imgHScale=1;
	private Bitmap bmp=null;
	private Paint paint=null;

	public ChartView(Context context)
	{
		super(context);

		bmp = BitmapFactory
				.decodeResource(getResources(), R.drawable.black);

		//拉伸图片
		int imgwidth=bmp.getWidth();
		int imgheight=bmp.getHeight();
		Log.i("RUN","imgWH:"+imgwidth+"\t"+imgheight);
		imgWScale=(float)screenH/imgwidth;
		imgHScale=(float)screenW/imgheight;
		Matrix matrix=new Matrix();
		matrix.postScale(imgWScale, imgHScale);
		bmp=Bitmap.createBitmap(bmp, 0, 0, imgwidth, imgheight, matrix, true);
		

		paint = new Paint();
	}

	public void setScreenPara(int width, int height)
	{
		screenW=width;
		screenH=height;
		YPoint = height - 40;
		YLength = height - 60;
		XLength = width - 140;
	}

	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData,
			String strTitle)
	{
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);// 重写onDraw方法
		
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bmp, 10, 10, null);

		// canvas.drawColor(Color.BLACK);//设置背景颜色
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(Color.BLUE);// 颜色
//		Paint paint1 = new Paint();
//		paint1.setStyle(Paint.Style.STROKE);
//		paint1.setAntiAlias(true);// 去锯齿
//		paint1.setColor(Color.DKGRAY);
		paint.setTextSize(12); // 设置轴文字大小
		// 设置Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // 轴线
		for (int i = 0; i * YScale < YLength; i++)
		{
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 5, YPoint - i
					* YScale, paint); // 刻度
			try
			{
				canvas.drawText(YLabel[i], XPoint - 22,
						YPoint - i * YScale + 5, paint); // 文字
			} catch (Exception e)
			{
			}
		}
		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength
				+ 6, paint); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength
				+ 6, paint);
		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // 轴线
		float oldLineWidth;
		for (int i = 0; i * XScale < XLength; i++)
		{
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale,
					YPoint - 5, paint); // 刻度

			paint.setColor(Color.RED);
			try
			{
				canvas.drawText(XLabel[i], XPoint + i * XScale - 10,
						YPoint + 20, paint); // 文字
				// 数据值
				if (i > 0 && YCoord(Data[i - 1]) != -999
						&& YCoord(Data[i]) != -999) // 保证有效数据
				{
					oldLineWidth = paint.getStrokeWidth();
					Log.i("RUN", "Line Width: " + oldLineWidth);
					paint.setStrokeWidth(2);
					canvas.drawLine(XPoint + (i - 1) * XScale,
							YCoord(Data[i - 1]), XPoint + i * XScale,
							YCoord(Data[i]), paint);
					paint.setStrokeWidth(4);
					canvas.drawPoint(XPoint + i * XScale, YCoord(Data[i]),paint);
					paint.setStrokeWidth(oldLineWidth);

				}
			} catch (Exception e)
			{
			}
			paint.setColor(Color.BLUE);
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint - 3, paint); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6,
				YPoint + 3, paint);
		paint.setColor(Color.MAGENTA);
		paint.setTextSize(20);
		canvas.drawText(Title, 150, 50, paint);
	}

	private int YCoord(String y0) // 计算绘制时的Y坐标，无数据时返回-999
	{
		int y;
		try
		{
			y = Integer.parseInt(y0);
		} catch (Exception e)
		{
			return -999; // 出错则返回-999
		}
		try
		{
			return YPoint - y * YScale / Integer.parseInt(YLabel[1]);
		} catch (Exception e)
		{
		}
		return y;
	}
}