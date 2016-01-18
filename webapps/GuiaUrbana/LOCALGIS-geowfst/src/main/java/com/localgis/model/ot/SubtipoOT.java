/**
 * SubtipoOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * SubtipoOT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.localgis.model.ot;

public class SubtipoOT  implements java.io.Serializable {
    private java.lang.String nombreSubtipo;

    private java.lang.String patron;

    public SubtipoOT() {
    }

    public SubtipoOT(
           java.lang.String nombreSubtipo,
           java.lang.String patron) {
           this.nombreSubtipo = nombreSubtipo;
           this.patron = patron;
    }


    /**
     * Gets the nombreSubtipo value for this SubtipoOT.
     * 
     * @return nombreSubtipo
     */
    public java.lang.String getNombreSubtipo() {
        return nombreSubtipo;
    }


    /**
     * Sets the nombreSubtipo value for this SubtipoOT.
     * 
     * @param nombreSubtipo
     */
    public void setNombreSubtipo(java.lang.String nombreSubtipo) {
        this.nombreSubtipo = nombreSubtipo;
    }


    /**
     * Gets the patron value for this SubtipoOT.
     * 
     * @return patron
     */
    public java.lang.String getPatron() {
        return patron;
    }


    /**
     * Sets the patron value for this SubtipoOT.
     * 
     * @param patron
     */
    public void setPatron(java.lang.String patron) {
        this.patron = patron;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SubtipoOT)) return false;
        SubtipoOT other = (SubtipoOT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombreSubtipo==null && other.getNombreSubtipo()==null) || 
             (this.nombreSubtipo!=null &&
              this.nombreSubtipo.equals(other.getNombreSubtipo()))) &&
            ((this.patron==null && other.getPatron()==null) || 
             (this.patron!=null &&
              this.patron.equals(other.getPatron())));
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
        if (getNombreSubtipo() != null) {
            _hashCode += getNombreSubtipo().hashCode();
        }
        if (getPatron() != null) {
            _hashCode += getPatron().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SubtipoOT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ot.model.localgis.com", "SubtipoOT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreSubtipo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ot.model.localgis.com", "nombreSubtipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("patron");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ot.model.localgis.com", "patron"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
