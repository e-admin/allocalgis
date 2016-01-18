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

import org.satec.sld.SVG.SVGNodeFeature;

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
	public boolean validate(SVGNodeFeature feature, String Name, Object Value) {
		if (Value==null) 
			Value = feature.getAttribute(Name);

		Domain matchedDom=getMatchedChildDomain(feature,Name,Value);
		if (matchedDom!=null)
			setLastErrorMessage(matchedDom.getLastError());
		return 	matchedDom!=null;	
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		return Domain.TREE;
	}

	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getRepresentation()
	 */
	public String getRepresentation() {
		return getName();
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
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#validateLocal(java.lang.String, java.lang.String, com.vividsolutions.jump.feature.Feature)
	 */
	public boolean validateLocal(SVGNodeFeature feature, String Name, Object Value) {
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
}