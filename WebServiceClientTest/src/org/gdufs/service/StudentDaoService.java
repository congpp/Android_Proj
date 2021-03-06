package org.gdufs.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * StudentDaoService service = new StudentDaoService();
 * StudentDaoDelegate portType = service.getStudentDaoPort();
 * portType.findStudentById(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "StudentDaoService", targetNamespace = "http://service.gdufs.org/", wsdlLocation = "http://localhost:8080/WebService/StudentDaoPort?wsdl")
public class StudentDaoService extends Service {

	private final static URL STUDENTDAOSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(org.gdufs.service.StudentDaoService.class.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = org.gdufs.service.StudentDaoService.class
					.getResource(".");
			url = new URL(baseUrl,
					"http://localhost:8080/WebService/StudentDaoPort?wsdl");
		} catch (MalformedURLException e) {
			logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:8080/WebService/StudentDaoPort?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		STUDENTDAOSERVICE_WSDL_LOCATION = url;
	}

	public StudentDaoService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public StudentDaoService() {
		super(STUDENTDAOSERVICE_WSDL_LOCATION, new QName(
				"http://service.gdufs.org/", "StudentDaoService"));
	}

	/**
	 * 
	 * @return returns StudentDaoDelegate
	 */
	@WebEndpoint(name = "StudentDaoPort")
	public StudentDaoDelegate getStudentDaoPort() {
		return super.getPort(new QName("http://service.gdufs.org/",
				"StudentDaoPort"), StudentDaoDelegate.class);
	}

}
