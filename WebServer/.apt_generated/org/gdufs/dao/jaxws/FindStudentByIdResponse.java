
package org.gdufs.dao.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findStudentByIdResponse", namespace = "http://dao.gdufs.org/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findStudentByIdResponse", namespace = "http://dao.gdufs.org/")
public class FindStudentByIdResponse {

    @XmlElement(name = "return", namespace = "")
    private org.cyc.entity.Student _return;

    /**
     * 
     * @return
     *     returns Student
     */
    public org.cyc.entity.Student getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(org.cyc.entity.Student _return) {
        this._return = _return;
    }

}
