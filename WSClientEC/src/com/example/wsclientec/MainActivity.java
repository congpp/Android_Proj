package com.example.wsclientec;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private EditText word;
	private TextView resultView;
	private Button queryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		networkPolicy();
		word = (EditText) findViewById(R.id.words);
		resultView = (TextView) findViewById(R.id.resultView);
		queryButton = (Button) findViewById(R.id.button1);

		queryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//单词
				String cword = word.getText().toString().trim();
				// 简单判断用户输入的单词是否合法
				if ("".equals(cword)) {
					// 给出错误提示
					word.setError("您输入的单词有误！");
					word.requestFocus();
					// 将显示查询结果的TextView清空
					resultView.setText("");
					return;
				}
				// 查询翻译信息
				getEnglish(cword);

			}
		});
	}

	public void getEnglish(String word) {
		// 命名空间
		String nameSpace = "http://WebXml.com.cn/";
		// 调用的方法名称
		String methodName = "getEnCnTwoWayTranslator";
		// EndPoint
		String endPoint = "http://webservice.webxml.com.cn/WebServices/TranslatorWebService.asmx";
		// SOAP Action
		String soapAction = "http://WebXml.com.cn/getEnCnTwoWayTranslator";

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的参数Word
		rpc.addProperty("Word", word);


		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果

		String result = object.getProperty(0).toString();

		// 将WebService返回的结果显示在TextView中
		resultView.setText(result);
	}

	

	public void networkPolicy() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
																		// 就包括了磁盘读写和网络I/O
				.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
	}
}
