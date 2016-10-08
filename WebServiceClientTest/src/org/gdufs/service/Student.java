package org.gdufs.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for student complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="student">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="className" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="courseId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="photo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="school" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="studentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="teacherId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "student", propOrder = { "className", "courseId", "email",
		"ip", "latitude", "longitude", "password", "photo", "school", "sex",
		"studentId", "studentName", "teacherId", "telephone" })
public class Student {

	protected String className;
	protected String courseId;
	protected String email;
	protected String ip;
	protected Integer latitude;
	protected Integer longitude;
	protected String password;
	protected String photo;
	protected String school;
	protected String sex;
	protected String studentId;
	protected String studentName;
	protected String teacherId;
	protected String telephone;

	/**
	 * Gets the value of the className property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the value of the className property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setClassName(String value) {
		this.className = value;
	}

	/**
	 * Gets the value of the courseId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCourseId() {
		return courseId;
	}

	/**
	 * Sets the value of the courseId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCourseId(String value) {
		this.courseId = value;
	}

	/**
	 * Gets the value of the email property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the value of the email property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmail(String value) {
		this.email = value;
	}

	/**
	 * Gets the value of the ip property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the value of the ip property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIp(String value) {
		this.ip = value;
	}

	/**
	 * Gets the value of the latitude property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getLatitude() {
		return latitude;
	}

	/**
	 * Sets the value of the latitude property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setLatitude(Integer value) {
		this.latitude = value;
	}

	/**
	 * Gets the value of the longitude property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getLongitude() {
		return longitude;
	}

	/**
	 * Sets the value of the longitude property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setLongitude(Integer value) {
		this.longitude = value;
	}

	/**
	 * Gets the value of the password property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the value of the password property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPassword(String value) {
		this.password = value;
	}

	/**
	 * Gets the value of the photo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPhoto() {
		return photo;
	}

	/**
	 * Sets the value of the photo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPhoto(String value) {
		this.photo = value;
	}

	/**
	 * Gets the value of the school property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * Sets the value of the school property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSchool(String value) {
		this.school = value;
	}

	/**
	 * Gets the value of the sex property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets the value of the sex property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSex(String value) {
		this.sex = value;
	}

	/**
	 * Gets the value of the studentId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * Sets the value of the studentId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStudentId(String value) {
		this.studentId = value;
	}

	/**
	 * Gets the value of the studentName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStudentName() {
		return studentName;
	}

	/**
	 * Sets the value of the studentName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setStudentName(String value) {
		this.studentName = value;
	}

	/**
	 * Gets the value of the teacherId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTeacherId() {
		return teacherId;
	}

	/**
	 * Sets the value of the teacherId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTeacherId(String value) {
		this.teacherId = value;
	}

	/**
	 * Gets the value of the telephone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * Sets the value of the telephone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTelephone(String value) {
		this.telephone = value;
	}

}
