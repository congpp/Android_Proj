package org.gdufs.service;

import java.util.*;
import org.gdufs.entity.Student;

@javax.jws.WebService(targetNamespace = "http://service.gdufs.org/", serviceName = "StudentDaoService", portName = "StudentDaoPort", wsdlLocation = "WEB-INF/wsdl/StudentDaoService.wsdl")
public class StudentDaoDelegate {

	org.gdufs.service.StudentDao studentDao = new org.gdufs.service.StudentDao();

	public Student findStudentById(String id) {
		return studentDao.findStudentById(id);
	}

}