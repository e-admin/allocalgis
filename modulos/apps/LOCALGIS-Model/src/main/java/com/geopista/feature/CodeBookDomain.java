/**
 * CodeBookDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

/**
 * Dominio que restringe el valor a uno de los enumerados en sus hijos.
 * Los dominios hijo se utilizarán tomando:
 * getRepresentation() como valor mostrado en los GUI
 * pattern como codigo equivalente del valor.
 * Sólo admite dominios hijo CodedEntryDomain.
 * Sólo tiene sentido un nivel de hijos.
 * 
 * PATTERN: Para el dominio CODEBOOKDOMAIN se interpreta como un Nombre. No se utiliza.
 * 
 * @author juacas
 */
public class CodeBookDomain extends TreeDomain {
	
	/* Returns the String representation of this domain default value.
	 * @see com.geopista.feature.Domain#getRepresentation()
	 */
	public String getRepresentation() {
		return getPattern();
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		
		return Domain.CODEBOOK;
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.feature.TreeDomain#addLevelName(java.lang.String, int)
	 */
	public void addLevelName(String name, int level) {
		if (level>0) throw new IllegalArgumentException("CodeBookDomain supports only one level.");
		super.addLevelName(name, level);
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.TreeDomain#attachColumnToLevel(com.geopista.feature.Column, int)
	 */
	public void attachColumnToLevel(Column col, int level) {
		if (level>0) 
			throw new IllegalArgumentException("CodeBookDomain supports only one level.");
		super.attachColumnToLevel(col, level);
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.TreeDomain#getLevelName(int)
	 */
	public String getLevelName(int level) {
		if (this.Levels==0) return getName();
		else
		return super.getLevelName(level);
	}
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#addChild(com.geopista.feature.Domain)
	 */
	public void addChild(Domain child_domain) {
		if (child_domain instanceof CodedEntryDomain)
			super.addChild(child_domain);
		else
			throw new IllegalArgumentException("No se puede añadir más que CodedEntryDomain a un CodeBookDomain.");
	}
	/**
	 * 
	 */
	public CodeBookDomain() {
		super();
	}
	/**
	 * @param Name
	 * @param Description
	 */
	public CodeBookDomain(String Name, String Description) {
		super(Name, Description);
	}
}
