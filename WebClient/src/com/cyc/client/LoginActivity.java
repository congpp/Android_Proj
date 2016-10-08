package com.cyc.client;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cyc.entity.Student;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.homework.R;

public class LoginActivity extends Activity {
	TextView userid, password, log_info;
	Button login_ok;
	String ip = "192.168.235.10";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.networkPolicy();// 定制网络连接策略，至少最低版本9，低于9则不需要
		userid = (TextView) this.findViewById(R.id.login_userid);
		password = (TextView) this.findViewById(R.id.login_password);
		log_info = (TextView) this.findViewById(R.id.login_errInfo);
		login_ok = (Button) this.findViewById(R.id.login_ok);
		login_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (userid.getText().toString().trim().equals("gduf")
				// && password.getText().toString().equals("welcome")) {
				// log_info.setText("登录成功！");
				// } else {
				// log_info.setText("登录失败！");
				// }
				Student student = login(userid.getText().toString().trim());
				if (student.getStudentId().equals("fail")) {
					log_info.setText("无此用户");
				} else if (!student.getPassword().trim().equals(
						password.getText().toString().trim())) {
					log_info.setText("密码错");
				} else {
					log_info.setText("登录成功！");

				}

			}
		});
	}

	public Student login(String stid) {
		Student result = null;
		URL url = null;
		try {
			url = new URL("http://" + ip
					+ ":8080/homeworkServer/servlet/StudentLogin");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestMethod("POST");
			DataOutputStream outobj = new DataOutputStream(
					connection.getOutputStream());
			outobj.writeUTF(this.userid.getText().toString().trim());
			outobj.flush();
			outobj.close();
			ObjectInputStream ois = new ObjectInputStream(
					connection.getInputStream());
			result = (Student) ois.readObject();
			ois.close();
			connection.disconnect();
		} catch (Exception e) {
			password.setText(e.toString());
			e.printStackTrace();
		} finally {

		}
		return result;
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
