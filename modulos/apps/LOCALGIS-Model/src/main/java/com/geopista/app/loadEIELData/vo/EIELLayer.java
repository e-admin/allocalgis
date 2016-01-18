/**
 * EIELLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * EIELLayer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.geopista.app.loadEIELData.vo;

public class EIELLayer  implements java.io.Serializable {
    private java.lang.String geometryField;

    private java.lang.String id;

    private java.lang.String idField;

    private java.lang.String lang_name;

    private java.lang.String table;
    
    private int position;
    
    private boolean visible;

    public EIELLayer() {
    }

    public EIELLayer(
           java.lang.String geometryField,
           java.lang.String id,
           java.lang.String idField,
           java.lang.String lang_name,
           java.lang.String table) {
           this.geometryField = geometryField;
           this.id = id;
           this.idField = idField;
           this.lang_name = lang_name;
           this.table = table;
    }


    /**
     * Gets the geometryField value for this EIELLayer.
     * 
     * @return geometryField
     */
    public java.lang.String getGeometryField() {
        return geometryField;
    }


    /**
     * Sets the geometryField value for this EIELLayer.
     * 
     * @param geometryField
     */
    public void setGeometryField(java.lang.String geometryField) {
        this.geometryField = geometryField;
    }


    /**
     * Gets the id value for this EIELLayer.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this EIELLayer.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the idField value for this EIELLayer.
     * 
     * @return idField
     */
    public java.lang.String getIdField() {
        return idField;
    }


    /**
     * Sets the idField value for this EIELLayer.
     * 
     * @param idField
     */
    public void setIdField(java.lang.String idField) {
        this.idField = idField;
    }


    /**
     * Gets the lang_name value for this EIELLayer.
     * 
     * @return lang_name
     */
    public java.lang.String getLang_name() {
        return lang_name;
    }


    /**
     * Sets the lang_name value for this EIELLayer.
     * 
     * @param lang_name
     */
    public void setLang_name(java.lang.String lang_name) {
        this.lang_name = lang_name;
    }


    /**
     * Gets the table value for this EIELLayer.
     * 
     * @return table
     */
    public java.lang.String getTable() {
        return table;
    }


    /**
     * Sets the table value for this EIELLayer.
     * 
     * @param table
     */
    public void setTable(java.lang.String table) {
        this.table = table;
    }
    
    
    

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EIELLayer)) return false;
        EIELLayer other = (EIELLayer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.geometryField==null && other.getGeometryField()==null) || 
             (this.geometryField!=null &&
              this.geometryField.equals(other.getGeometryField()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.idField==null && other.getIdField()==null) || 
             (this.idField!=null &&
              this.idField.equals(other.getIdField()))) &&
            ((this.lang_name==null && other.getLang_name()==null) || 
             (this.lang_name!=null &&
              this.lang_name.equals(other.getLang_name()))) &&
            ((this.table==null && other.getTable()==null) || 
             (this.table!=null &&
              this.table.equals(other.getTable())));
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
        if (getGeometryField() != null) {
            _hashCode += getGeometryField().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getIdField() != null) {
            _hashCode += getIdField().hashCode();
        }
        if (getLang_name() != null) {
            _hashCode += getLang_name().hashCode();
        }
        if (getTable() != null) {
            _hashCode += getTable().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EIELLayer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "EIELLayer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("geometryField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "geometryField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idField");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "idField"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lang_name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "lang_name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("table");
        elemField.setXmlName(new javax.xml.namespace.QName("http://vo.loadEIELData.app.geopista.com", "table"));
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

}
