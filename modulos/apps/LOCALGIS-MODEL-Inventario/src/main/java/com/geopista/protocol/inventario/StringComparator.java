/**
 * StringComparator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.text.Collator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import com.geopista.app.AppContext;
import com.geopista.app.UserPreferenceConstants;

/**
 * Clase necesaria para realizar una ordenación por fecha
 * 
 * @date: 21-12-2010
 * @author: angeles
 * 
 */

public class StringComparator {
	// Para añadir la búsqueda especial por numero de inventario
	static AppContext app =(AppContext) AppContext.getApplicationContext();
    private static String locale = app.getString(UserPreferenceConstants.DEFAULT_LOCALE_KEY,"es_ES");
    
    
	
    
	public static final Comparator STRING_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {

			//Para permitir que la busqueda no sea tan sensible
			 Collator myCollator=Collator.getInstance(new Locale(locale));
             myCollator.setStrength(Collator.PRIMARY);
             return myCollator.compare(o1, o2);
             
			//return ((String)o1).compareTo((String)o2);

		}
	};

	private String valor;

	public StringComparator(String valor) {
		this.valor = valor;
	}

	public String toString() {
		return valor;
	}

	public static void main(String[] arg) {
		/*
		 * NumInventario str1= new NumInventario("1.1.2.10"); NumInventario
		 * str2= new NumInventario("1.1.10");
		 * System.out.println("Comparando: "+NUM_INVENTARIO_COMPARATOR
		 * .compare(str1, str2));
		 */

	}

}
