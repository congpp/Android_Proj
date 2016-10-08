package org.gdufs.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the org.gdufs.service package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _FindStudentById_QNAME = new QName(
			"http://service.gdufs.org/", "findStudentById");
	private final static QName _FindStudentByIdResponse_QNAME = new QName(
			"http://service.gdufs.org/", "findStudentByIdResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: org.gdufs.service
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link FindStudentByIdResponse }
	 * 
	 */
	public FindStudentByIdResponse createFindStudentByIdResponse() {
		return new FindStudentByIdResponse();
	}

	/**
	 * Create an instance of {@link FindStudentById }
	 * 
	 */
	public FindStudentById createFindStudentById() {
		return new FindStudentById();
	}

	/**
	 * Create an instance of {@link Student }
	 * 
	 */
	public Student createStudent() {
		return new Student();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link FindStudentById }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.gdufs.org/", name = "findStudentById")
	public JAXBElement<FindStudentById> createFindStudentById(
			FindStudentById value) {
		return new JAXBElement<FindStudentById>(_FindStudentById_QNAME,
				FindStudentById.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link FindStudentByIdResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.gdufs.org/", name = "findStudentByIdResponse")
	public JAXBElement<FindStudentByIdResponse> createFindStudentByIdResponse(
			FindStudentByIdResponse value) {
		return new JAXBElement<FindStudentByIdResponse>(
				_FindStudentByIdResponse_QNAME, FindStudentByIdResponse.class,
				null, value);
	}

}
