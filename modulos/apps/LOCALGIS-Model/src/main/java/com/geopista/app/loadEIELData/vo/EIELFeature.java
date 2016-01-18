/**
 * EIELFeature.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * EIELFeature.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.loadEIELData.vo;

public class EIELFeature  implements java.io.Serializable {
    private java.util.HashMap attributes;

    private java.lang.String geometry;

    public EIELFeature() {
    }

    public EIELFeature(
           java.util.HashMap attributes,
           java.lang.String geometry) {
           this.attributes = attributes;
           this.geometry = geometry;
    }


    /**
     * Gets the attributes value for this EIELFeature.
     * 
     * @return attributes
     */
    public java.util.HashMap getAttributes() {
        return attributes;
    }


    /**
     * Sets the attributes value for this EIELFeature.
     * 
     * @param attributes
     */
    public void setAttributes(java.util.HashMap attributes) {
        this.attributes = attributes;
    }


    /**
     * Gets the geometry value for this EIELFeature.
     * 
     * @return geometry
     */
    public java.lang.String getGeometry() {
        return geometry;
    }


    /**
     * Sets the geometry value for this EIELFeature.
     * 
     * @param geometry
     */
    public void setGeometry(java.lang.String geometry) {
        this.geometry = geometry;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EIELFeature)) return false;
        EIELFeature other = (EIELFeature) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              this.attributes.equals(other.getAttributes()))) &&
            ((this.geometry==null && other.getGeometry()==null) || 
             (this.geometry!=null &&
              this.geometry.equals(other.getGeometry())));
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
        if (getAttributes() != null) {
            _hashCode += getAttributes().hashCode();
        }
        if (getGeometry() != null) {
            _hashCode += getGeometry().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EIELFeature.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "EIELFeature"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("geometry");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "geometry"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
