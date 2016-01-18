/**
 * EIELHashAttDomain.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.domains.eiel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.TreeDomain;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

public class EIELHashAttDomain {

	public static HashMap<String, ListaEstructuras> tablaAsociaciones;
	private static String datePattern = "?[*:*]dd-MM-yyyy";
	
	static {	
		tablaAsociaciones = new HashMap<String, ListaEstructuras>();
	}
	
	/**
	 * Retorna el dominio al que pertenece el atributo pasado como parámetro
	 * @param att
	 * @return
	 */
	public static Domain getEstructuraAtt(String att, String sLocale){
		//tratamiento diferente para fechas
		if(att.toLowerCase().contains("fecha")){
			DateDomain dateDomain = new DateDomain(datePattern, datePattern);
			return dateDomain;
		}
		else {
			ListaEstructuras listaTipos = getListaEstructuras(att);
			Hashtable listaDNodes = listaTipos.getLista();
			Enumeration keysIdNode = listaDNodes.keys();
			String idNode = null;
			DomainNode domainNode = null;
			Domain domain = new TreeDomain(att, att);
			while (keysIdNode.hasMoreElements()) {
				idNode = (String) keysIdNode.nextElement();
				domainNode = (DomainNode) listaDNodes.get(idNode);
				domain.addChild(new CodedEntryDomain(domainNode.getPatron(), domainNode.getTerm(sLocale)));
			}
			
			return domain;
		}
	}
	
	public static ListaEstructuras getListaEstructuras(String att){
		if(tablaAsociaciones.get(att)==null){
			Estructuras.cargarEstructura(att);
			tablaAsociaciones.put(att, Estructuras.getListaTipos());
		}
		return tablaAsociaciones.get(att);
	}
}
