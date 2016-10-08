package org.cyc.servlet;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cyc.dao.StudentDao;
import org.cyc.entity.Student;

public class StudentLogin extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	StudentDao studentDao = new StudentDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		try
		{
			Student student = null;
			DataInputStream ios = null;
			ios = new DataInputStream(request.getInputStream());
			ObjectOutputStream oos = null;
			oos = new ObjectOutputStream(response.getOutputStream());
			String studentId = ios.readUTF();
			student = this.studentDao.findStudentById(studentId);
			if (student != null)
			{
				oos.writeObject(student);
			} else
			{
				student = new Student();
				student.setStudentId("fail");
				oos.writeObject(student);
			}
			oos.flush();
			oos.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
		}

	}
}
