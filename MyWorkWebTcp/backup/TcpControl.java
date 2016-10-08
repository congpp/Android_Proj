package com.cyc.mywork;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.util.Log;

public class TcpControl extends Thread
{
	private boolean isTcpConnected = false;
	private static final String HOST = "192.168.235.10";
	private static final int PORT = 8888;
	private Socket socket = null;
	private SocketAddress socketAddress = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private String recvCmd = "";
	private final boolean reconnect[] = { false, true };
	protected static final boolean CANCEL = false;
	protected static final boolean AGAIN = true;

	public TcpControl()
	{
		socketAddress = new InetSocketAddress(HOST, PORT);
	}

	@Override
	public void run()
	{
		// try connect until success or canceled by user
		while (!isTcpConnected)
		{
			try
			{
				socket = new Socket();
				// Timeout will throws ioexception
				socket.connect(socketAddress, 10);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
						socket.getOutputStream())), true);
				isTcpConnected = true;
				Log.i("RUN", "Conected! " + isTcpConnected + HOST + PORT);
			} catch (IOException ex)
			{
				ex.printStackTrace();
				Log.i("RUN", "IOException TimeOut: " + isTcpConnected + HOST + PORT);
			}
		}// while(isConnected)
		Log.e("RUN recvCmd", recvCmd);

		// read data from tcp server
		while (isTcpConnected)
		{
			try
			{
				if (socket.isConnected())
					if (!socket.isInputShutdown())
					{
						// readLine()will be blocked until reading is avaliable
						recvCmd = in.readLine();
						Log.i("RUN recvCmd", recvCmd);
					}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			if (socket != null)
				socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Log.i("RUN", "Thread tcp ends");
	}

	public boolean sendMessage(String msg)
	{
		if (socket.isConnected())
			if (!socket.isOutputShutdown())
			{
				out.write(msg);
				Log.i("RUN writecmd", msg);
				return true;
			}
		return false;
	}

	public String getRecvCmd()
	{
		return recvCmd;
	}
	
	public void stopRunning()
	{
		isTcpConnected = false;
	}
}
