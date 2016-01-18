/**
 * EIELCompletoTableModel.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.models;

import java.util.HashMap;

import javax.swing.table.DefaultTableModel;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;

public abstract class EIELCompletoTableModel extends DefaultTableModel{

	 /**
	 * 
	 */
	private static final long serialVersionUID = -6062337709320798104L;

    private AppContext aplicacion;
	
	 
	 
	 public String getNombreNucleo(String codentidad,String codnucleo){
		 
		 String nombreNucleo=codentidad;
		 aplicacion= (AppContext) AppContext.getApplicationContext();

		 HashMap map=(HashMap)aplicacion.getBlackboard().get("HASH_NUCLEOS_MUNICIPIO");
		 
		 String claveBusqueda=codentidad+"_"+codnucleo;
		 
		 LCGNucleoEIEL nucleoEIEL= (LCGNucleoEIEL)map.get(claveBusqueda);
		 if (nucleoEIEL!=null){
			 nombreNucleo="("+claveBusqueda+") "+nucleoEIEL.getDenominacion() ;
		 }
		 
		 return nombreNucleo;
	 }
}
