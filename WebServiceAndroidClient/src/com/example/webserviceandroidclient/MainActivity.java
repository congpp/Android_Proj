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
				// �ֻ����루�Σ�
				String sid = stid.getText().toString().trim();
				// ���ж��û�������ֻ����루�Σ��Ƿ�Ϸ�
				if ("".equals(sid)) {
					// ����������ʾ
					stid.setError("������ĵ�������");
					stid.requestFocus();
					// ����ʾ��ѯ�����TextView���
					resultView.setText("");
					return;
				}
				// ��ѯ�ֻ����루�Σ���Ϣ
				findStudents(sid);
			}
		});
	}

	public void findStudents(String id) {
		// �����ռ�
		String nameSpace = "http://service.gdufs.org/";
		// ���õķ�������
		String methodName = "findStudentById";
		// EndPoint
		String endPoint = "http://192.168.1.102:8080/WebService/StudentDaoPort?wsdl";

		// ָ��WebService�������ռ�͵��õķ�����
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// ���������WebService�ӿ���Ҫ�������������mobileCode��userId
		rpc.addProperty("arg0", id);
		// rpc.addProperty("userId", "");

		// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// �����Ƿ���õ���dotNet������WebService
		envelope.dotNet = false;
		// �ȼ���envelope.bodyOut = rpc;
		// envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		// transport.debug=true;
		try {

			// ����WebService
			transport.call(null, envelope);
			// if (envelope.getResponse() != null) {
			//
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��
		int count = object.getPropertyCount();
		for (int i = 0; i < count; i++) {
			SoapObject soapObjectInner = (SoapObject) object.getProperty(i);
			String result = soapObjectInner.getProperty("studentName").toString();
			// ��WebService���صĽ����ʾ��TextView��
			resultView.setText(result);
		}

	}

	public void networkPolicy() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // ��������滻ΪdetectAll()
																		// �Ͱ����˴��̶�д������I/O
				.penaltyLog() // ��ӡlogcat����ȻҲ���Զ�λ��dropbox��ͨ���ļ�������Ӧ��log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
				.penaltyLog() // ��ӡlogcat
				.penaltyDeath().build());
	}
}
