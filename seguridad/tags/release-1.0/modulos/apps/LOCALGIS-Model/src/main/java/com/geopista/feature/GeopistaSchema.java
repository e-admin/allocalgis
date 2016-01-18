/*
 * * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 * 
 * Created on 23-may-2004 by juacas
 *
 * 
 * TODO document
 * */
package com.geopista.feature;



import com.geopista.feature.Attribute;
import com.geopista.model.IGeopistaLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author juacas
 *
 * TODO document
 *  */
public class GeopistaSchema extends FeatureSchema implements GeopistaSchemaAccesor {
	/**
	 * Extended Schema. Contains domain metadata.
	 */
	
	private HashMap attributeCol=new HashMap();
	private HashMap colAttribute=new HashMap();
	private ArrayList attributeAccess = new ArrayList(); // Almacena acceso solo lectura o modificacion "R/W" "W"
	public static final String READ_ONLY="R";
	public static final String READ_WRITE="R/W";
	public static final String NO_ACCESS = "NA";
    
    private IGeopistaLayer geopistalayer;
	
	public GeopistaSchema() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setAttributeColumn(int index, Column column)
	{
		// set domain applied to attribute at index
		attributeCol.put(this.getAttributeName(index),column);
		colAttribute.put(column,this.getAttributeName(index));
	}
	/**
	 * Obtiene el atributo que está enlazado con la columna de nombre colname
	 * @param colname el nombre de la columna
	 * @return null si no hay resultado.
	 */
	public String getAttributeByColumn(String colname)
	{
	Iterator it=colAttribute.keySet().iterator();
	while (it.hasNext())
		{
		Column element = (Column) it.next();
		if (colname.equals(element.getName()))
			return getAttribute(element);
		}
	return null;
	}
	public Column getColumnByAttribute(int index)
	{
		return getColumnByAttribute(this.getAttributeName(index));
	}
	public Domain getAttributeDomain(int index)
	{
		return getAttributeDomain(getAttributeName(index));
	}

	public void addAttribute(String attributeName, AttributeType attributeType, Column column) 
	{
		addAttribute(attributeName, attributeType, column, READ_WRITE);
	}
    public void addAttribute(String attributeName, AttributeType attributeType,  String accessType) 
    {
        super.addAttribute(attributeName, attributeType);
        int attributeIndex = getAttributeIndex(attributeName);
        attributeAccess.add(attributeIndex,accessType);
    }
	/* (non-Javadoc)
	 * @see com.vividsolutions.jump.feature.FeatureSchema#addAttribute(java.lang.String, com.vividsolutions.jump.feature.AttributeType)
	 */
	public void addAttribute(String attributeName, AttributeType attributeType, Column column, boolean editable) 
	{
		String accessType = new String();
		
		if (editable){
			accessType = READ_WRITE;
		}
		else{
			accessType = READ_ONLY;
		}
		addAttribute(attributeName,attributeType,column,accessType);
	}
	
	public void addAttribute(String attributeName, AttributeType attributeType, Column column, String accessType) 
	{
		addAttribute(attributeName,attributeType,accessType);
		
		this.setAttributeColumn(this.getAttributeIndex(attributeName),column);
	
	}
	/**
	 * Gets the attribute Name mapped from Column col in this schema
	 * @param col
	 * @return
	 */
	public String getAttribute(Column col) {
		return (String) colAttribute.get(col);
	}
	/**
	 * @param attName
	 * @return
	 */
	public Domain getAttributeDomain(String attName) {
		return (Domain) ((Column)attributeCol.get(attName)).getDomain();
		
	}
	/**
	 * @param attName
	 * @return
	 */
	public Column getColumnByAttribute(String attName) {
		return (Column) attributeCol.get(attName);
	}
	/**
	 * @param root
	 * @return
	 */
	public String getAttributeByDomain(Domain domain) {
		for (Iterator cols=colAttribute.entrySet().iterator();cols.hasNext();)
		{
		Map.Entry entry= (Map.Entry) cols.next();
		Column col= (Column) entry.getKey();
		if (col.getDomain()== domain)
			return (String)entry.getValue();
		}
		return null;
	}
	
	/**
	 * sustituye el esquema de JUMP por un esquema automático de Geopista. Para una integración rápida.
	 * SOLO PARA PRUEBAS
	 * @param feature
	 */
	public static GeopistaFeature vampiriceSchema(Feature feature)
	{
		FeatureSchema fSchema= feature.getSchema();
		GeopistaSchema gSchema=new GeopistaSchema();
//		 crea el atributo 
		
		Table defTable = new Table("DatosCapa", "Tabla de la BBDD para esta capa.");
		for (int i=0;i<fSchema.getAttributeCount();i++)
		{
			//define Dominio, Columna y añade al esquema
		Domain defDomain;
		if(fSchema.getAttributeType(i)==AttributeType.DOUBLE ||
				fSchema.getAttributeType(i)==AttributeType.FLOAT)
			{
			defDomain =new NumberDomain("?[-INF:INF]","Default Double Number.");
			}
		else if(
				fSchema.getAttributeType(i)==AttributeType.INTEGER ||
				fSchema.getAttributeType(i)==AttributeType.LONG)
			{
			// Definir patron para excluir los decimales.
			defDomain = new NumberDomain("?[-INF:INF]","Default Integer Number");
			
			}
		else
			{
		defDomain= new StringDomain("?[.*]","Dominio por defecto.");
			}
		Column col= new Column(fSchema.getAttributeName(i),"Columna automática.", defTable, defDomain);
		gSchema.addAttribute(fSchema.getAttributeName(i),fSchema.getAttributeType(i),col,READ_WRITE);
		}
		GeopistaFeature gFeature=new GeopistaFeature(gSchema);
		for (int i=0;i<fSchema.getAttributeCount();i++)
		{
		gFeature.setAttribute(fSchema.getAttributeName(i), feature.getAttribute(i));
		}
		return gFeature;
	}


  /**
     * Called by Java2XML
     * @return Attributes with enough information to be saved to a project file
     */
    public List getAttributes() {
        ArrayList persistentAttributes = new ArrayList();

        for (int i = 0; i< super.getAttributeCount();i++) {
           // if(super.getAttributeType(i)==AttributeType.GEOMETRY) continue;
            Attribute attribute = new Attribute();
            attribute.setName(super.getAttributeName(i));
            attribute.setType(super.getAttributeType(i).toString());
            attribute.setColumn(getColumnByAttribute(super.getAttributeName(i)));
            attribute.setAccessType((String) attributeAccess.get(i));
            persistentAttributes.add(attribute);
        }

        return persistentAttributes;
    }

    public void addAttribute(Attribute attribute) {
        attribute.setSchema(this);
        // comprobación de clones de tablas
         GeopistaSchema schema = this;

    // busca si existe otra tabla con este nombre
    // java2xml desvincula las tablas y crea múltiples instancias
    
    for (int i=0; i<schema.getAttributeCount();i++)
    {
      if (schema.getColumnByAttribute(i).getTable().getName().equals(attribute.getColumn().getTable().getName()))
      {
        // Es un clon de la ya existente -> uso la existente en lugar del parámetro.
        attribute.getColumn().setTable(schema.getColumnByAttribute(i).getTable());     
      }
    }
    /**
     * Busca si existe un dominio con el mismo nombre de tipo TreeDomain
     * java2xml desvincula los dominios y crea instancias separadas
     */
    Domain newdomain=attribute.getColumn().getDomain();
    if (newdomain instanceof TreeDomain)
		{
		TreeDomain newTreeDomain = (TreeDomain) newdomain;
		// busca por nombre de dominio
		for (int i = 0; i < schema.getAttributeCount(); i++)
			{
			if (schema.getAttributeDomain(i)!=null && schema.getAttributeDomain(i).getName().equals(newTreeDomain.getName()))
				{
				//se ha encontrado el dominio ya definido: Usamos el actual
				Domain dom=schema.getAttributeDomain(i);
				newTreeDomain= (TreeDomain) dom;//overrides suplied domain
				
				
				}
			}
		attribute.getColumn().setDomain(newTreeDomain);
//		 enlaza la columna actual al dominio existente
		int level= newTreeDomain.getLevelNames().indexOf(attribute.getColumn().getName());
		if (level>-1)
			newTreeDomain.attachColumnToLevel(attribute.getColumn(),level);
		}
   
        addAttribute(attribute.getName(),AttributeType.toAttributeType(attribute.getType()),attribute.getColumn(), attribute.getAccessType());
    }
    

	/**
	 * @param index
	 * @return
	 */
	public String getAttributeAccess(int index) {
       
		return (String)attributeAccess.get(index);
       
        
	}
	public String getAttributeAccess(String attName) {
	return getAttributeAccess(this.getAttributeIndex(attName));
	}
	public void setAttributeAccess(String attName, String Status) {
		setAttributeAccess(this.getAttributeIndex(attName),Status);
		}
	/**
	 * @param attributeIndex
	 */
	private void setAttributeAccess(int attributeIndex,String Status) {
		this.attributeAccess.add(attributeIndex,Status);
		
	}
    /**
     * @return Returns the geopistalayer.
     */
    public IGeopistaLayer getGeopistalayer()
    {
        return geopistalayer;
    }
    /**
     * @param geopistalayer The geopistalayer to set.
     */
    public void setGeopistalayer(IGeopistaLayer geopistalayer)
    {
        this.geopistalayer = geopistalayer;
    }

}
