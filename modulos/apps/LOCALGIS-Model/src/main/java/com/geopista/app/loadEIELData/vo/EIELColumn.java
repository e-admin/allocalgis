/**
 * EIELColumn.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * EIELColumn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.loadEIELData.vo;

public class EIELColumn  implements java.io.Serializable {
    private boolean isNotNull;

    private int length;

    private java.lang.String name;

    private int position;

    private java.lang.String type;

    public EIELColumn() {
    }

    public EIELColumn(
           boolean isNotNull,
           int length,
           java.lang.String name,
           int position,
           java.lang.String type) {
           this.isNotNull = isNotNull;
           this.length = length;
           this.name = name;
           this.position = position;
           this.type = type;
    }


    /**
     * Gets the isNotNull value for this EIELColumn.
     * 
     * @return isNotNull
     */
    public boolean isIsNotNull() {
        return isNotNull;
    }


    /**
     * Sets the isNotNull value for this EIELColumn.
     * 
     * @param isNotNull
     */
    public void setIsNotNull(boolean isNotNull) {
        this.isNotNull = isNotNull;
    }


    /**
     * Gets the length value for this EIELColumn.
     * 
     * @return length
     */
    public int getLength() {
        return length;
    }


    /**
     * Sets the length value for this EIELColumn.
     * 
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }


    /**
     * Gets the name value for this EIELColumn.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this EIELColumn.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the position value for this EIELColumn.
     * 
     * @return position
     */
    public int getPosition() {
        return position;
    }


    /**
     * Sets the position value for this EIELColumn.
     * 
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }


    /**
     * Gets the type value for this EIELColumn.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this EIELColumn.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EIELColumn)) return false;
        EIELColumn other = (EIELColumn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.isNotNull == other.isIsNotNull() &&
            this.length == other.getLength() &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.position == other.getPosition() &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
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
        _hashCode += (isIsNotNull() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getLength();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += getPosition();
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EIELColumn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "EIELColumn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isNotNull");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "isNotNull"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("length");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("position");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "position"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "type"));
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
