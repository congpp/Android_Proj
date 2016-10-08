package test;

import org.gdufs.service.Student;
import org.gdufs.service.StudentDaoDelegate;
import org.gdufs.service.StudentDaoService;

public class TestWs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StudentDaoService sds=new StudentDaoService();
		StudentDaoDelegate sdd=sds.getStudentDaoPort();
		Student st=(Student)sdd.findStudentById("101");
         System.out.println(st.getStudentName());
	}

}
