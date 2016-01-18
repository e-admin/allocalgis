/*The GEOPISTA project is a set of tools and applications to manage
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
 * Juan Pablo de Castro
 * Created on 08-may-2004
 */
package com.geopista.feature;

/**
 * @author juacas
 *
 * Entrada fija que tiene un valor de representación y un código asociado.
 * 
 * PATTERN: Código que represetna el Valor: P.Ej. "PRIV", "1", etc.
 * DESCRIPTION: Texto que aparece en el interfaz de usuario (Combo Box) 
 */
public class CodedEntryDomain extends StringDomain {
	/**
	 * @param pattern
	 * @param Description
	 */

  public CodedEntryDomain()
  {
    
  }
  
	public CodedEntryDomain(String pattern, String Description) {
		super(pattern, Description);
		
	}
	
	/* (non-Javadoc)
	 * @see com.geopista.feature.Domain#getType()
	 */
	public int getType() {
		
		return Domain.CODEDENTRY;
	}
	/* Destinado a ser representado en un ComboBox
	 * Por lo tanto su longitud será la de su Descripción
	 * @see com.geopista.feature.Domain#getAproxLenght()
	 */
	public int getAproxLenght() {
		return getDescription().length(); 
	}
  
}
