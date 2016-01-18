/**
 * ElementoPuntualSaneamientoEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class ElementoPuntualSaneamientoEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public ElementoPuntualSaneamientoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		/*relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_c_saneam_pr",""));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_pr","eiel_c_saneam_pr",""));*/
		relacionFields.add(new LCGCampoCapaTablaEIEL("id","id","eiel_c_saneam_pr",""));
	}	

	public Hashtable getIdentifyFields() {
		return null;
	}
	
	
}
