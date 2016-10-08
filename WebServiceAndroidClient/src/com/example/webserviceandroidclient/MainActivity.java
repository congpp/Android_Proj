package com.example.webserviceandroidclient;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
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
	private EditText stid;
	private TextView resultView;
	private Button queryButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		networkPolicy();
		stid = (EditText) findViewById(R.id.sid);
		resultView = (TextView) findViewById(R.id.resultView);
		queryButton = (Button) findViewById(R.id.button1);

		queryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 手机号码（段）
				String sid = stid.getText().toString().trim();
				// 简单判断用户输入的手机号码（段）是否合法
				if ("".equals(sid)) {
					// 给出错误提示
					stid.setError("您输入的单词有误！");
					stid.requestFocus();
					// 将显示查询结果的TextView清空
					resultView.setText("");
					return;
				}
				// 查询手机号码（段）信息
				findStudents(sid);
			}
		});
	}

	public void findStudents(String id) {
		// 命名空间
		String nameSpace = "http://service.gdufs.org/";
		// 调用的方法名称
		String methodName = "findStudentById";
		// EndPoint
		String endPoint = "http://192.168.1.102:8080/WebService/StudentDaoPort?wsdl";

		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		rpc.addProperty("arg0", id);
		// rpc.addProperty("userId", "");

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = false;
		// 等价于envelope.bodyOut = rpc;
		// envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		// transport.debug=true;
		try {

			// 调用WebService
			transport.call(null, envelope);
			// if (envelope.getResponse() != null) {
			//
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		int count = object.getPropertyCount();
		for (int i = 0; i < count; i++) {
			SoapObject soapObjectInner = (SoapObject) object.getProperty(i);
			String result = soapObjectInner.getProperty("studentName").toString();
			// 将WebService返回的结果显示在TextView中
			resultView.setText(result);
		}

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
