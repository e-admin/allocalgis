/**
 * VerPlanosPublicadosResponse.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * VerPlanosPublicadosResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.localgis.ln;

public class VerPlanosPublicadosResponse  implements java.io.Serializable {
    private com.localgis.web.core.model.LocalgisMap[] out;

    public VerPlanosPublicadosResponse() {
    }

    public VerPlanosPublicadosResponse(
           com.localgis.web.core.model.LocalgisMap[] out) {
           this.out = out;
    }


    /**
     * Gets the out value for this VerPlanosPublicadosResponse.
     * 
     * @return out
     */
    public com.localgis.web.core.model.LocalgisMap[] getOut() {
        return out;
    }


    /**
     * Sets the out value for this VerPlanosPublicadosResponse.
     * 
     * @param out
     */
    public void setOut(com.localgis.web.core.model.LocalgisMap[] out) {
        this.out = out;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof VerPlanosPublicadosResponse)) return false;
        VerPlanosPublicadosResponse other = (VerPlanosPublicadosResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.out==null && other.getOut()==null) || 
             (this.out!=null &&
              java.util.Arrays.equals(this.out, other.getOut())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOut() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOut());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOut(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(VerPlanosPublicadosResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ln.localgis.com", ">verPlanosPublicadosResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("out");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ln.localgis.com", "out"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://model.core.web.localgis.com", "LocalgisMap"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://model.core.web.localgis.com", "LocalgisMap"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
