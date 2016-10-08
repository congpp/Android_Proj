package org.cyc.dao;

import org.cyc.entity.Student;

@javax.jws.WebService(targetNamespace = "http://dao.gdufs.org/", serviceName = "StudentDaoService", portName = "StudentDaoPort")
public class StudentDaoDelegate
{

	org.cyc.dao.StudentDao studentDao = new org.cyc.dao.StudentDao();

	public Student findStudentById(String studentId)
	{
		return studentDao.findStudentById(studentId);
	}

}