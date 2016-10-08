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
 1.打开网页
                Uri uri = Uri.parse("http://www.google.com.hk");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
2.打开地图
                //必须使用Google APIs包 
                Uri uri = Uri.parse("geo:113.46,22.27");       
                Intent it = new Intent(Intent.ACTION_VIEW, uri);        
                startActivity(it);
3.呼出电话拨号器
                Uri uri = Uri.parse("tel:15013580650");
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
4.拨打电话
                //必须加上android.permission.CALL_PHONE权限 
                Uri uri = Uri.parse("tel:15013580650");
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                startActivity(intent);
5.发送短信
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.putExtra("sms_body", "这里输入短信内容");
                intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
6.发送短信（带号码）
                Uri uri = Uri.parse("smsto:5554");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "这里输入短信内容");
                startActivity(intent);
7.发送彩信
                // 发送彩信的图片路径
                Uri uri = Uri.parse("file:///sdcard/handou.png");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra("sms_body", "这里输入信息内容");
                // 彩信附件
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                // 文件类型
                intent.setType("image/png");
                startActivity(intent);
8.发送邮件
                String[] tos = {"doctang@163.com"};
                String[] ccs = {"tangmin@163.com"};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, tos);
                intent.putExtra(Intent.EXTRA_CC, ccs);
                intent.putExtra(Intent.EXTRA_TEXT, "邮件正文");
                intent.putExtra(Intent.EXTRA_SUBJECT, "邮件主题");
                //intent.putExtra(Intent.EXTRA_STREAM, "file:///sdcard/handou.png");
                intent.setType("text/plain");
                startActivity(intent);
************************************************************************************/

