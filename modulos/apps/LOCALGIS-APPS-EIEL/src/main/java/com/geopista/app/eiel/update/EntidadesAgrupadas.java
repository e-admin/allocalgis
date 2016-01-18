/**
 * EntidadesAgrupadas.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.update;

import java.util.HashMap;

public class EntidadesAgrupadas {

	HashMap<String,EntidadAgrupada> entidadesAgrupadas=new HashMap<String,EntidadAgrupada>();
	
	public void addTotalInfo(String codEntidad,float totalPoblacion,float totalViviendas){
		
		EntidadAgrupada entidadAgrupada=entidadesAgrupadas.get(codEntidad);
		if (entidadAgrupada==null)
			entidadAgrupada=new EntidadAgrupada();
		
		entidadAgrupada.setTotalPoblacion(totalPoblacion);
		entidadAgrupada.setTotalViviendas(totalViviendas);
		
		entidadesAgrupadas.put(codEntidad,entidadAgrupada);
	}

	public EntidadAgrupada getEntidadAgrupada(String claveEntidadAgrupada) {
		return entidadesAgrupadas.get(claveEntidadAgrupada);
	}
	
}

class EntidadAgrupada{
	
	float totalPoblacion;
	float totalViviendas;
	public float getTotalPoblacion() {
		return totalPoblacion;
	}
	public void setTotalPoblacion(float totalPoblacion) {
		this.totalPoblacion += totalPoblacion;
	}
	public float getTotalViviendas() {
		return totalViviendas;
	}
	public void setTotalViviendas(float totalViviendas) {
		this.totalViviendas += totalViviendas;
	}
	@Override
	public String toString() {
		return "EntidadAgrupada [totalPoblacion=" + totalPoblacion
				+ ", totalViviendas=" + totalViviendas + "]";
	}
	
}
