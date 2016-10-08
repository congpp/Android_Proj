package com.cyc.client;

import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyc.entity.Student;
import org.cyc.entity.WebFileService;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.homework.R;

public class StudentManagementActivity extends Activity
{

	private String ip = "192.168.235.10";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_management);

		// 获得Layout里面的ListView
		ListView list = (ListView) findViewById(R.id.lv);
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, getStudentsData(""),
				R.layout.mylist, new String[] { "img", "name", "id" }, new int[] { R.id.img,
						R.id.title, R.id.info });
		// 判断将远程图片数据转换为图片资源
		listItemAdapter.setViewBinder(new MyViewBinder());
		// 添加并且显示
		list.setAdapter(listItemAdapter);
		// 添加单击监听
		list.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				@SuppressWarnings("unchecked")
				Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
				setTitle(clkmap.get("name").toString());
			}
		});

	}

	private List<Map<String, Object>> getStudentsData(String str)
	{
		List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
		// mData.clear();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Student> students = getStudents(str);
		for (Student st : students)
		{
			map = new HashMap<String, Object>();
			// if (st.getSex().trim().equals("m"))
			// map.put("img", R.drawable.boy);
			// else
			// map.put("img", R.drawable.girl);
			// 从网络上获取图片
			if (st.getPhoto().contains("png"))
			{
				String address = "http://" + ip + ":8080/homeworkServer/photo/" + st.getPhoto();
				byte[] data = WebFileService.getImage(address); // 得到图片的输入流
				// 二进制数据生成位图
				Bitmap bit = BitmapFactory.decodeByteArray(data, 0, data.length);
				map.put("img", bit);

			} else if (st.getSex().trim().equals("m"))
				map.put("img", R.drawable.boy);
			else
				map.put("img", R.drawable.girl);
			//
			map.put("name", st.getStudentName());
			map.put("id", st.getStudentId());
			mData.add(map);
		}
		return mData;
	}

	// 连接网络获取学生名单
	public List<Student> getStudents(String queryStr)
	{
		List<Student> questions = new ArrayList<Student>();
		URL url = null;
		try
		{
			url = new URL("http://" + ip + ":8080/homeworkServer/servlet/getStudents");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestMethod("POST");
			DataOutputStream outobj = new DataOutputStream(connection.getOutputStream());

			outobj.writeUTF(queryStr);
			outobj.flush();
			outobj.close();
			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

			Student obj = (Student) ois.readObject();
			while (!obj.getStudentId().equals("00000"))
			{
				// System.out.println(obj.getQuestion());
				questions.add(obj);
				obj = (Student) ois.readObject();
			}
			ois.close();
			connection.disconnect();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{

		}
		return questions;
	}
}
