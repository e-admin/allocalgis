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
 * Created on 02-may-2004
 * 
 */
package com.geopista.feature;

/**
 * @author juacas Descripción de una columna del modelo de base de datos
 */
public class Column {
	/**
	 *  
	 */
	private String name;
	private String description;
	private Domain domain;

	public Column ()
	{
		super();    
	}

	public Column(String name, String description, Domain domain) {
		super();
		this.name=name;
		this.description=description;
		this.domain=domain;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the domain.
	 */
	public Domain getDomain() {
		return domain;
	}
	/**
	 * @param domain The domain to set.
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String toString()
	{
		return getName();
	}


}
