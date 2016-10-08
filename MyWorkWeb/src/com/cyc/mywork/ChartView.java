package com.cyc.mywork;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ChartView extends View
{
	private int XPoint = 0; // 原点的X坐标
	private int YPoint = 260; // 原点的Y坐标
	private int XScale = 100; // X的刻度长度
	private int YScale = 100; // Y的刻度长度
	private int XLength = 380; // X轴的长度
	private int YLength = 240; // Y轴的长度
	private String[] XLabel; // X的刻度
	private String[] YLabel; // Y的刻度
	private String[] Data; // 数据
	private String Title; // 显示的标题
	private int screenW = 480;
	private int screenH = 800;
	private float imgWScale = 1;
	private float imgHScale = 1;
	private Bitmap bmp = null;
	private Paint paint = null;
	private String info = null;
	private float textSize = 18;

	public ChartView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.black);

		// 拉伸图片
		paint = new Paint();
	}

	/*
	 * width -> screen width, height -> screen heght
	 */
	public void setScreenPara(int width, int height, int avg, float textSize)
	{
		screenW = width;
		screenH = height;
		YPoint = height - 40;
		YLength = height - 60;
		XLength = width;
		this.textSize = textSize;
		XScale = (XLength - 20) / avg; // x轴刻度数量
		YScale = (YLength - 20) / 1; // y轴刻度数量
		int imgwidth = bmp.getWidth();
		int imgheight = bmp.getHeight();
		Log.i("RUN before-->", "imgWH:" + imgwidth + "\t" + imgheight);
		imgWScale = (float) screenW / imgwidth;
		imgHScale = (float) screenH / imgheight;
		Matrix matrix = new Matrix();
		matrix.postScale(imgWScale, imgHScale);
		bmp = Bitmap.createBitmap(bmp, 0, 0, imgwidth, imgheight, matrix, true);
		Log.i("RUN after-->", "imgWH:" + bmp.getWidth() + "\t" + bmp.getHeight());
	}

	public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, String strTitle,
			String Info)
	{
		XLabel = XLabels;
		YLabel = YLabels;
		Data = AllData;
		Title = strTitle;
		info = Info;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas); // 重写onDraw方法

		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bmp, 10, 10, null);

		// canvas.drawColor(Color.BLACK);//设置背景颜色
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);// 去锯齿
		paint.setColor(Color.WHITE);// 颜色
		// Paint paint1 = new Paint();
		// paint1.setStyle(Paint.Style.STROKE);
		// paint1.setAntiAlias(true);// 去锯齿
		// paint1.setColor(Color.DKGRAY);
		paint.setTextSize(textSize); // 设置轴文字大小
//		float xyWidth = 2;
//		float rulingWidth = 0;
		// 设置Y轴
//		paint.setStrokeWidth(xyWidth);
//
//		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint); // Y轴线
//		paint.setStrokeWidth(rulingWidth);
//		for (int i = 0; i * YScale < YLength; i++)
//		{
//			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 10, YPoint - i * YScale, paint); // 刻度
//			try
//			{
//				canvas.drawText(YLabel[i], XPoint - 28, YPoint - i * YScale + 5, paint); // 文字
//			} catch (Exception e)
//			{
//			}
//		}
//		canvas.drawLine(XPoint, YPoint - YLength, XPoint - 3, YPoint - YLength + 10, paint); // 箭头
//		canvas.drawLine(XPoint, YPoint - YLength, XPoint + 3, YPoint - YLength + 10, paint);

		// 设置X轴
		// paint.setStrokeWidth(xyWidth);
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint); // X轴线
		for (int i = 0; i * XScale < XLength; i++)
		{
			canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 10, paint); // 刻度

			paint.setColor(Color.RED);
			try
			{
				drawText(canvas, XLabel[i], XPoint + i * XScale - 10, YPoint + 20, paint, -45); // x轴文字
				// 数据值
				if (i > 0 && YCoord(Data[i - 1]) != -9999 && YCoord(Data[i]) != -9999) // 保证有效数据
				{
					// Log.i("RUN", "Line Width: " + oldLineWidth);
					// paint.setStrokeWidth(0);
					canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]), XPoint + i
							* XScale, YCoord(Data[i]), paint); // 绘制折线
					paint.setStrokeWidth(4);
					canvas.drawPoint(XPoint + i * XScale, YCoord(Data[i]), paint);// 描点
					paint.setStrokeWidth(0);
					// canvas.drawText(Data[i], XPoint + i * XScale,
					// YCoord(Data[i]), paint);// 值
					drawText(canvas, Data[i], XPoint + i * XScale, YCoord(Data[i]), paint, -45);

				}
			} catch (Exception e)
			{
			}
			paint.setColor(Color.WHITE);
		}
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint - 3, paint); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 6, YPoint + 3, paint);
		paint.setColor(Color.GREEN);
		canvas.drawText(info, XPoint + XLength - 120, YPoint - YLength + 180, paint);
		paint.setColor(Color.MAGENTA);
		paint.setTextSize(textSize + 4);
		canvas.drawText(Title, XPoint + 50, YPoint - YLength + 50, paint);
	}

	private void drawText(Canvas canvas, String text, float x, float y, Paint paint, float angle)
	{
		if (angle != 0)
		{
			canvas.rotate(angle, x, y);
		}
		canvas.drawText(text, x, y, paint);
		if (angle != 0)
		{
			canvas.rotate(-angle, x, y);
		}
	}

	private int YCoord(String y0) // 计算绘制时的Y坐标，无数据时返回-9999
	{
		int y;
		try
		{
			y = (int) (Double.parseDouble(y0)*100);//*100 保留小数
		} catch (Exception e)
		{
			return -9999; // 出错则返回-999
		}
		try
		{
			return (int) (YPoint - (y/100) * YScale / Double.parseDouble(YLabel[1])*100);
		} catch (Exception e)
		{
		}
		return y;
	}
}