package org.cyc.dao;

import java.sql.*;

import org.cyc.entity.Student;
import org.cyc.util.DBHelper;

public class StudentDao
{

	String sql1 = "select * from student where studentId=?";

	public Student findStudentById(String studentId)
	{
		Student student = null;
		Connection con = null;
		PreparedStatement stat = null;
		con = DBHelper.connect();
		try
		{
			stat = con.prepareStatement(sql1);
			stat.setString(1, studentId);
			ResultSet rs = stat.executeQuery();
			if (rs.next())
			{
				student = new Student();
				student.setStudentId(studentId);
				student.setStudentName(rs.getString("studentName"));
				student.setPassword(rs.getString("password"));
				student.setClassName(rs.getString("className"));
				student.setEmail(rs.getString("email"));
				student.setTeacherId(rs.getString("teacherId"));
				student.setPhoto(rs.getString("photo"));
				student.setTelephone(rs.getString("telephone"));
				student.setSex(rs.getString("sex"));
				student.setSchool(rs.getString("school"));
				student.setCourseId(rs.getString("courseId"));

			}
		} catch (SQLException ex)
		{
			System.out.println("dao“Ï≥£" + ex.toString());
		} finally
		{
			DBHelper.closePreparedStatement(stat);
			DBHelper.closeConneciton(con);
		}
		return student;
	}

}
