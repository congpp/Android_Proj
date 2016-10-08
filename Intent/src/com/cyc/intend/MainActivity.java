package com.cyc.intend;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.view.View.*;
import android.widget.Button;

public class MainActivity extends Activity
{
	private Button btn1=null;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn1=(Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.setAction(Intent.ACTION_SENDTO);
				intent.setData(Uri.parse("smsto:5554"));
				intent.putExtra("sms_body", "Hello congge!");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
/*************************************************************************
 1.����ҳ
                Uri uri = Uri.parse("http://www.google.com.hk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
2.�򿪵�ͼ
                //����ʹ��Google APIs�� 
                Uri uri = Uri.parse("geo:113.46,22.27");       
                Intent it = new Intent(Intent.ACTION_VIEW, uri);        
                startActivity(it);
3.�����绰������
                Uri uri = Uri.parse("tel:15013580650");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
4.����绰
                //�������android.permission.CALL_PHONEȨ�� 
                Uri uri = Uri.parse("tel:15013580650");
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
5.���Ͷ���
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("sms_body", "���������������");
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
6.���Ͷ��ţ������룩
                Uri uri = Uri.parse("smsto:5554");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "���������������");
                startActivity(intent);
7.���Ͳ���
                // ���Ͳ��ŵ�ͼƬ·��
                Uri uri = Uri.parse("file:///sdcard/handou.png");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra("sms_body", "����������Ϣ����");
                // ���Ÿ���
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                // �ļ�����
                intent.setType("image/png");
                startActivity(intent);
8.�����ʼ�
                String[] tos = {"doctang@163.com"};
                String[] ccs = {"tangmin@163.com"};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, tos);
                intent.putExtra(Intent.EXTRA_CC, ccs);
                intent.putExtra(Intent.EXTRA_TEXT, "�ʼ�����");
                intent.putExtra(Intent.EXTRA_SUBJECT, "�ʼ�����");
                //intent.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/handou.png");
                intent.setType("text/plain");
                startActivity(intent);
************************************************************************************/

