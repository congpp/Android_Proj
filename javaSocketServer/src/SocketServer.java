import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer
{
	private static final int PORT = 8088;
	private List<Socket> mList = new ArrayList<Socket>();
	private ServerSocket server = null;
	private ExecutorService mExecutorService = null; // thread pool

	public static void main(String[] args)
	{
		new SocketServer();
	}

	public SocketServer()
	{
		try
		{
			server = new ServerSocket(PORT);
			mExecutorService = Executors.newCachedThreadPool(); // create a
																// thread pool
			System.out.print("server start ...");
			Socket client = null;
			while (true)
			{
				client = server.accept();
				mList.add(client);
				mExecutorService.execute(new Service(client)); // start a new
																// thread to
																// handle the
																// connection
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	class Service implements Runnable
	{
		private Socket socket;
		private BufferedReader in = null;
		private String msg = "";

		public Service(Socket socket)
		{
			this.socket = socket;
			try
			{
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			try
			{
				boolean isRun=true;
				int i=1;
				while (isRun)
				{
					if ((msg = in.readLine()) != null)
					{
//						if (msg.equals("exit"))
//						{
//							System.out.println("ssssssss");
//							mList.remove(socket);
//							in.close();
//							msg = "user:" + socket.getInetAddress()
//									+ "exit total:" + mList.size();
//							socket.close();
//							this.sendmsg();
//							break;
//						} else
//						{
//							msg = socket.getInetAddress() + ":" + msg;
//							this.sendmsg();
//						}
					}
					//msg = socket.getInetAddress() + ":" + msg;
					if(i>3)
					{
						socket.close();
						break;
					}
					this.sendmsg(i);
					i++;
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		public void sendmsg(int i) throws IOException
		{
//			System.out.println(msg);
//			InputStreamReader isr = new InputStreamReader(System.in);
//	        BufferedReader br= new BufferedReader(isr);
//	        String sendMsg = br.readLine();
//			int num = mList.size();
//			for (int index = 0; index < num; index++)
//			{
//				Socket mSocket = mList.get(index);
//				PrintWriter pout = null;
//				try
//				{
//					pout = new PrintWriter(new BufferedWriter(
//							new OutputStreamWriter(mSocket.getOutputStream())),
//							true);
//					pout.println(sendMsg + '\n');
//				} catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
			//send a file
			 String filePath = "D:\\"+i+".jpg";
             File fi = new File(filePath);

             System.out.println("文件长度:" + (int) fi.length());

             // public Socket accept() throws
             // IOException侦听并接受到此套接字的连接。此方法在进行连接之前一直阻塞。

             System.out.println("Sending");
             DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             dis.readByte();

             DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
             DataOutputStream ps = new DataOutputStream(socket.getOutputStream());
             
             ps.writeUTF(fi.getName());
             ps.flush();
             ps.writeLong(fi.length());
             ps.flush();
             
             int bufferSize = 8192;
             byte[] buf = new byte[bufferSize];
             while (true) {
                 int read = 0;
                 if (fis != null) {
                     read = fis.read(buf);
                 }

                 if (read == -1) {
                     break;
                 }
                 ps.write(buf, 0, read);
             }
             System.out.println("Finished");
             ps.flush();
             fis.close();
             //socket.close();
		}
	}
}
