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
 * Created on 04-may-2004
 *
 */
package com.geopista.feature;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.feature.AttributeType;
import com.vividsolutions.jump.feature.Feature;

/**
 * Un TreeDomain establece relaciones entre otros dominios y columnas del modelo.
 * El TreeDomain es la raiz de un árbol de dominios.
 * 
 * <pre>
 * TreeDomain
 * 	|
 *  --Dominio Alternativa1
 * 	|	|-Dominio alternativa1.1
 * 	|	|-Dominio alternativa1.2
 * 	|	|-Dominio alternativa1.3
 * 	|	|-Dominio alternativa1.4
 * 	--Dominio Alternativa2
 * 	|	|-Subalternativa2.1
 * 	|	|-Subalternativa2.2
 * 	--Dominio Alternativa3
 * 	 	|-Subalternativa3.1
 * 		|-Subalternativa3.2
 * </pre>
 * 
 * Cada nivel en las jerarquías representa un dominio para una columna del modelo.
 * Por defecto los niveles se nombran como las columnas a las que se asigna.
 * En la validación se tienen en cuenta las dependencias arriba-abajo en el
 * árbol de dominios.
 * 
 * NOTA: En un TreeDomain no se incluye en ningún nivel ningún nodo CODEBOOKDOMAIN sino 
 * directamente los códigos CODEDENTRYDOMAIN.
 * 
 * @author juacas
 *
 */
public class TreeDomain extends Domain {
	private static final String	TREE_DOMAIN_ERROR	= "TreeDomain.TreeSomainConstraintError";
	int Levels=0;
	Vector levelToColumnMap = new Vector();
	Vector levelToName=new Vector();
	
	
	public void addLevelName(String name, int level)
	{
	levelToName.ensureCapacity(level+1);
	    if(levelToName.size()<(level+1))
	      {
	        levelToName.setSize(level+1);
	      }
	
	levelToName.set(level,name);
	}
	public String getLevelName(int level)
	{
		return (String) levelToName.get(level);
	}
	/**
	 * For Java2XML
	 * @return a list of strings with columns' names 
	 */
	public List getLevelNames()
	{
	return levelToName;
	}
	/**
	 * For Java2XML
	 * Sets the mapping between levels and columns.
	 * Don´t make adjustments to the DOM
	 * @param levels
	 */
	public void addLevelName(String levelName)
	{
	levelToName.add(levelName);
	}
	public void attachColumnToLevel(Column col, int level)
	{
	levelToColumnMap.ensureCapacity(level);
	    if(levelToColumnMap.size()<(level+1))
	      {
	        levelToColumnMap.setSize(level+1);
	      }
	
		levelToColumnMap.set(level,col);
		addLevelName(col.getName(),level);
		Levels=Math.max(level+1,Levels);
	}
	public Column getColumnByLevel(int level)
	{
		return (Column) levelToColumnMap.get(level);
	}
	
	
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validate(java.lang.String, java.lang.String, com.vividsolutions.jump.feature.Feature)
	 */
	public boolean validate(Feature feature, String Name, Object Value) {
		if (Value==null) 
			Value = feature.getAttribute(Name);
		Column col=((GeopistaSchema)feature.getSchema()).getColumnByAttribute(Name);
		Domain dom = this.getKeyDomainByColumn(col, feature);
		
	
		if (dom==null)
			{
			StringBuffer msg=new StringBuffer();
			msg.append(I18N.get(TreeDomain.TREE_DOMAIN_ERROR));
			msg.append(" [");
			Object[] levels=(Object[]) levelToName.toArray();
			for (int i=0;i<getLevelByColumn(col);i++)
				{
				String element = (String) levels[i];
				msg.append("\"");
				msg.append(element);
				msg.append("\"");
				if (i<getLevelByColumn(col)-1)
					msg.append(" , ");
				}
			msg.append("]");
			setLastErrorMessage(msg.toString());
			return false;
			}
		
		//Si el usuario no ha introducido nada y el dominio
		//permite nulo lo dejamos pasar.
		try {
			if (((Value==null) || Value.equals("")) && dom.isNullable())
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
		Domain matchedDom=dom.getMatchedChildDomain(feature,Name,Value);
		if (matchedDom!=null)
			setLastErrorMessage(matchedDom.getLastError());
		return 	matchedDom!=null;	
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		// TODO Auto-generated method stub
		return Domain.TREE;
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getRepresentation()
	 */
	public String getRepresentation() {
		// TODO Auto-generated method stub
		return getName();
	}

	

	/*
	 * returns Domain that aplies to the attribute diving to depth level
	 * and matching upper levels
	 */
	public Domain getMatchedChildDomainDeep( Feature feature, String attName,String value)
	{
		Domain leveldomain=this;
		GeopistaSchema schema = (GeopistaSchema)feature.getSchema();
		int currLevel=0;
		Column column= schema.getColumnByAttribute(attName);
		int targetLevel=this.getLevelByColumn(column);
		if (value==null) value=feature.getString(attName);
		
		while (currLevel<targetLevel)
		{
			//get the attribute ruled by current TreeDomain
			Column col = this.getColumnByLevel(currLevel);
			String feature_attribute= schema.getAttribute(col);
			// get value
			Object atribute=feature.getAttribute(feature_attribute);
			if (atribute==null) // si el valor no está especificado no se puede seguir comprobando.
				return null;
			String feature_value = atribute.toString();
			
			Domain match=null;
			// Busca todos los hijos
			match=this.getMatchedChildDomain(feature,attName,feature_value);
//			for (Iterator i=leveldomain.getChildren().iterator();i.hasNext();)
//			{
//				Domain child=(Domain)i.next();
//				if (child.validateLocal(feature,attName,feature_value)==true)
//					match=child;
//			}
			if (match==null) return null; //search truncated
			leveldomain = match;
			// keep traversing tree
			currLevel++;
			if (currLevel == targetLevel)
			return leveldomain;
		}
		
		
		return leveldomain;
	}

	/**
	 * @param column
	 * @return
	 */
	public int getLevelByColumn(Column column) {
		for (int i=0;i<levelToColumnMap.size();i++)
		{
			if ((Column)levelToColumnMap.get(i)==column) return i;
		}
		return -1;// TODO: refactor to throw Exception ??? [JP]
	}
	/**
	 * returns el dominio efectivo al que se refiere este nivel del TreeDomain
	 * sin contrastar valores
	 * @param column
	 */
	public Domain getKeyDomainByColumn(Column column) {
		Domain effectivedomain=this;
		for (int i=0;i<levelToColumnMap.size();i++)
		{
			effectivedomain= (Domain) effectivedomain.getChildren().get(0);
			if (levelToColumnMap.get(i)== column)
			{
				return effectivedomain;
			}	
		}
		return null;
	}
	/**
	 * Obtiene el dominio efectivo que corresponde a los valores de los atributos
	 * de todos los niveles menos el último
	 * @param column
	 * @param feature
	 * @return
	 */
	public Domain getKeyDomainByColumn(Column column, Feature feature) {
	return getKeyDomainByColumn(column, feature, null);
	}
	public Domain getKeyDomainByColumn(Column column, Feature feature,String value)
	{
		GeopistaSchema schema=(GeopistaSchema)feature.getSchema();
		return (Domain) getMatchedChildDomainDeep(feature,schema.getAttribute(column),value);		
	
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(java.lang.String, java.lang.String, com.vividsolutions.jump.feature.Feature)
	 */
	public boolean validateLocal(Feature feature, String Name, Object Value) {
		// Not used as all validation is made on children
		return false;
	}
	/**
	 * Descripcion del nivel del arbol correspondiente al attributo
	 * @param attName Nombre del atributo
	 * @return Nombre del nivel del árbol
	 */
	public String getDescription(Column col) {
		
		return getLevelName(getLevelByColumn(col));
	}
	/**
	 * @return Depth of the TreeDomain
	 */
	public int getTreeDepth() {
		
		return this.Levels;
	}

	
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getAproxLenght()
	 */
	public int getAproxLenght() {
		// Max lenght of childs
		int length=0;
		for (Iterator i=getChildren().iterator();i.hasNext();)
		{
			Domain child=(Domain)i.next();
			length=Math.max(length,child.getAproxLenght());
		}
		return length;
	}
	/**
	 * 
	 */
	public TreeDomain() {
	}
	/**
	 * @param Name
	 * @param Description
	 */
	public TreeDomain(String Name, String Description) {
		super(Name, Description);
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getAttributeType()
	 */
	public AttributeType getAttributeType()
	{
		// No se puede proponer ningún tipo ya que este dominio gestiona varios atributos
		//potencialmente distintos.
		return null;
	}
}