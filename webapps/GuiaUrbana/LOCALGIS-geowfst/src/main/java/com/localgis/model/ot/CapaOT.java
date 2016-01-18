/**
 * CapaOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * CapaOT.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.localgis.model.ot;

public class CapaOT  implements java.io.Serializable {
    private java.lang.String nombreCapa;

    private java.lang.String patron;

    private com.localgis.model.ot.SubtipoOT[] subtipos;

    public CapaOT() {
    }

    public CapaOT(
           java.lang.String nombreCapa,
           java.lang.String patron,
           com.localgis.model.ot.SubtipoOT[] subtipos) {
           this.nombreCapa = nombreCapa;
           this.patron = patron;
           this.subtipos = subtipos;
    }


    /**
     * Gets the nombreCapa value for this CapaOT.
     * 
     * @return nombreCapa
     */
    public java.lang.String getNombreCapa() {
        return nombreCapa;
    }


    /**
     * Sets the nombreCapa value for this CapaOT.
     * 
     * @param nombreCapa
     */
    public void setNombreCapa(java.lang.String nombreCapa) {
        this.nombreCapa = nombreCapa;
    }


    /**
     * Gets the patron value for this CapaOT.
     * 
     * @return patron
     */
    public java.lang.String getPatron() {
        return patron;
    }


    /**
     * Sets the patron value for this CapaOT.
     * 
     * @param patron
     */
    public void setPatron(java.lang.String patron) {
        this.patron = patron;
    }


    /**
     * Gets the subtipos value for this CapaOT.
     * 
     * @return subtipos
     */
    public com.localgis.model.ot.SubtipoOT[] getSubtipos() {
        return subtipos;
    }


    /**
     * Sets the subtipos value for this CapaOT.
     * 
     * @param subtipos
     */
    public void setSubtipos(com.localgis.model.ot.SubtipoOT[] subtipos) {
        this.subtipos = subtipos;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CapaOT)) return false;
        CapaOT other = (CapaOT) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nombreCapa==null && other.getNombreCapa()==null) || 
             (this.nombreCapa!=null &&
              this.nombreCapa.equals(other.getNombreCapa()))) &&
            ((this.patron==null && other.getPatron()==null) || 
             (this.patron!=null &&
              this.patron.equals(other.getPatron()))) &&
            ((this.subtipos==null && other.getSubtipos()==null) || 
             (this.subtipos!=null &&
              java.util.Arrays.equals(this.subtipos, other.getSubtipos())));
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
        if (getNombreCapa() != null) {
            _hashCode += getNombreCapa().hashCode();
        }
        if (getPatron() != null) {
            _hashCode += getPatron().hashCode();
        }
        if (getSubtipos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSubtipos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSubtipos(), i);
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
        new org.apache.axis.description.TypeDesc(CapaOT.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://ot.model.localgis.com", "CapaOT"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCapa");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ot.model.localgis.com", "nombreCapa"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subtipos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://ot.model.localgis.com", "subtipos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://ot.model.localgis.com", "SubtipoOT"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://ot.model.localgis.com", "SubtipoOT"));
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
