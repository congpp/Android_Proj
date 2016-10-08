package org.gdufs.service;

import java.util.*;

import org.gdufs.entity.Student;

public class StudentDao {
	static List<Student> students = new ArrayList<Student>();
	static {
		students.add(new Student("101", "tom"));
		students.add(new Student("102", "mary"));
		students.add(new Student("103", "simon"));
		students.add(new Student("104", "tomas"));
	}

	public Student findStudentById(String id) {
		Student st = null;
		for (Student student : students) {
			if (student.getStudentId().equals(id))
				st = student;
		}
		return st;
	}
}
