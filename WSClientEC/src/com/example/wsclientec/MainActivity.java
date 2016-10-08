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
				//����
				String cword = word.getText().toString().trim();
				// ���ж��û�����ĵ����Ƿ�Ϸ�
				if ("".equals(cword)) {
					// ����������ʾ
					word.setError("������ĵ�������");
					word.requestFocus();
					// ����ʾ��ѯ�����TextView���
					resultView.setText("");
					return;
				}
				// ��ѯ������Ϣ
				getEnglish(cword);

			}
		});
	}

	public void getEnglish(String word) {
		// �����ռ�
		String nameSpace = "http://WebXml.com.cn/";
		// ���õķ�������
		String methodName = "getEnCnTwoWayTranslator";
		// EndPoint
		String endPoint = "http://webservice.webxml.com.cn/WebServices/TranslatorWebService.asmx";
		// SOAP Action
		String soapAction = "http://WebXml.com.cn/getEnCnTwoWayTranslator";

		// ָ��WebService�������ռ�͵��õķ�����
		SoapObject rpc = new SoapObject(nameSpace, methodName);

		// ���������WebService�ӿ���Ҫ����Ĳ���Word
		rpc.addProperty("Word", word);


		// ���ɵ���WebService������SOAP������Ϣ,��ָ��SOAP�İ汾
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// �����Ƿ���õ���dotNet������WebService
		envelope.dotNet = true;
		// �ȼ���envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// ����WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ��ȡ���ص�����
		SoapObject object = (SoapObject) envelope.bodyIn;
		// ��ȡ���صĽ��

		String result = object.getProperty(0).toString();

		// ��WebService���صĽ����ʾ��TextView��
		resultView.setText(result);
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
