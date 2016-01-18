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
 * 
 */
package com.geopista.feature;



public class ValidationError
{
	public Domain domain; // Dominio que a informado de este error
	public String attName; // Atributo que contiene el error
	public String message; // Descripción del error
	/**
	 * @param attname2
	 * @param description2
	 * @param domain2
	 */
	public ValidationError(String attname2, String description2, Domain domain2) {
		domain=domain2;
		attName=attname2;
		message=description2;
	}
	public String toString()
	{
	return attName+": "+message;
	}
}
