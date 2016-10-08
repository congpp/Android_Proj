import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ScreenReceiver extends JDialog implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private static final int PORT = 8088;
	private List<Socket> mList = new ArrayList<Socket>();
	private ServerSocket server = null;
	private ExecutorService mExecutorService = null; // thread pool

	private DataInputStream in = null;
	protected boolean isRun = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			ScreenReceiver dialog = new ScreenReceiver();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ScreenReceiver()
	{
		setBounds(100, 100, 365, 565);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblImage = new JLabel("");
			lblImage.setIcon(new ImageIcon("E:\\AndroidPro\\SceenReceiver\\Image\\win8.jpg"));
			contentPanel.add(lblImage);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Start");
				okButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						new Thread(ScreenReceiver.this).start();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						isRun = false;
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void run()
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
		public Service(Socket socket)
		{
			try
			{
				in = new DataInputStream(socket.getInputStream());
				System.out.println("client comes");
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			int width = 0;
			int height = 0;
			try
			{
				width = in.readInt();
				height = in.readInt();
				System.out.println(width);
				System.out.println(height);
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int tmp = 0, len = 0;
			byte img[] = new byte[width * height];
			byte buf[] = new byte[512];
			while (isRun)
			{
				try
				{
					while ((tmp = in.read(buf)) != -1)
					{
						CopyByte(buf, img, len, tmp);
						if (width * height < len)
							break;
					}
					Toolkit toolkit = Toolkit.getDefaultToolkit();
					Image image = toolkit.createImage(img);
					System.out.println(len);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void CopyByte(byte[] from, byte[] to, int start, int len)
	{
		for (int i = start, j = 0; j < len; i++, j++)
		{
			to[i] = from[j];
		}
	}

}
