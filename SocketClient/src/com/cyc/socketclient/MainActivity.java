package com.cyc.socketclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends Activity implements Runnable
{
	private EditText ed_his = null;
	private EditText ed_msg = null;
	private Button btn_send = null;
	private ImageView iv = null;
	private Button btn_img = null;
	private static final String HOST = "192.168.235.10";
	private static final int PORT = 8088;
	private Socket socket = null;

	private BufferedReader in = null;
	private InputStream ins = null;
	private PrintWriter out = null;
	private String content;
	private Bitmap bmp;
	private byte[] imgByte;
	private int imgLength;
	private static final boolean[] isPlaying = { false };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ed_msg = (EditText) findViewById(R.id.txt1);
		// btn_login = (Button) findViewById(R.id.Button01);
		btn_send = (Button) findViewById(R.id.btn2);
		btn_img = (Button) findViewById(R.id.btn1);
		ed_his = (EditText) findViewById(R.id.txtHis);
		iv = (ImageView) findViewById(R.id.iv1);

		try
		{
			socket = new Socket(HOST, PORT);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream())), true);
		} catch (IOException ex)
		{
			ex.printStackTrace();
			ShowDialog("login exception" + ex.getMessage());
		}
		btn_send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String msg = ed_msg.getText().toString();
				if (socket.isConnected())
				{
					if (!socket.isOutputShutdown())
					{
						out.println(msg);
						//ed_his.append("You: " + msg + "\n");

					}
				}
			}
		});
		btn_img.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				isPlaying[0] = true;
			}
		});
		new Thread(MainActivity.this).start();
	}

	public void ShowDialog(String msg)
	{
		new AlertDialog.Builder(this).setTitle("notification").setMessage(msg)
				.setPositiveButton("ok", new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub

					}
				}).show();
	}

	public void run()
	{
//		String tempString = null;
		Log.i("RUN", "Thread id" + Thread.currentThread().getId());
		try
		{
			boolean isRun=true;
			int time=0;
			while (isRun)
			{
				// if (socket.isConnected())
				// {
				// if (!socket.isInputShutdown())
				// {
				// if ((content = in.readLine()) != null)
				// {
				// Log.i("RUN", "Readline" + content);
				// // You cannot directory access the EditText in a new
				// // Thread
				// // like use: "ed_his.append(content)" here;
				// // So, a handler is necessary!
				// mHandler.sendMessage(mHandler.obtainMessage());
				// } else
				// {
				//
				// }
				// }
				// }

				if (socket.isConnected())
				{
					if (!socket.isOutputShutdown() && !socket.isInputShutdown())
					{
						if (isPlaying[0])
						{
							try
							{
								out.println("VIDEO\n");
								// receive file

								DataInputStream inputStream = new DataInputStream(
										new BufferedInputStream(
												socket.getInputStream()));

								String savePath = "/mnt/sdcard/cycApp/";
								int bufferSize = 8192;
								byte[] buf = new byte[bufferSize];
								int passedlen = 0;
								long len = 0;

								savePath += inputStream.readUTF();
								
								DataOutputStream fileOut = new DataOutputStream(
										new BufferedOutputStream(
												new BufferedOutputStream(
														new FileOutputStream(
																savePath))));
								new File(savePath);
								len = inputStream.readLong();
								Log.i("RUN","文件长度还有：" +  len  + "\n");
								if(len<bufferSize)	//小于，即一次可以读完
									passedlen=(int) len;
								else				//否则，需要读多几次
									passedlen=bufferSize;
								while (true) {
					                int read = 0;
					                if (inputStream != null) {
										read = inputStream.read(buf,0,passedlen);
					                }
					                len -= read;
					                if(len<bufferSize)	//小于8192，即一次可以读完
										passedlen=(int) len;
									else				//否则，需要读多几次8192
										passedlen=bufferSize;
					                
					                if (read == -1) {
					                    break;
					                }
					                
					                Log.i("RUN","文件长度还有：" +  len  + "\n");
					                fileOut.write(buf, 0, read);
					                if(passedlen<=0)
					                	break;
					            }
								Log.i("RUN","文件长度还有：" +  len  +" " +savePath +"\n");
								fileOut.close();
								bmp=BitmapFactory.decodeFile(savePath);
								mHandler.sendMessage(mHandler.obtainMessage());
								if(++time>2)
									isRun=false;
								//socket.close();
							} catch (IOException e)
							{
								e.printStackTrace();
								isRun=false;
							}
						}

					}
				}
			}
		} catch (Exception e)
		{
			Log.e("RUN", "Error" + Thread.currentThread().getId());
			e.printStackTrace();
		}
	}

	public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			Log.i("mHandler", "Thread id " + Thread.currentThread().getId());
			//ed_his.append(socket.getInetAddress() + ": " + content + "\n");
			iv.setImageBitmap(bmp);
		}
	};
}
