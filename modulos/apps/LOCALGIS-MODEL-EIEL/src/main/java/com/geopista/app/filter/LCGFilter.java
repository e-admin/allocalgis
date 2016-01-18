/**
 * LCGFilter.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

public class LCGFilter {

	public static String getValue(Object valuesObj,String clave){
		
		HashMap<String, String> values=(HashMap<String, String>)valuesObj;
		//return "("+values.get(clave)+")";	
		if ((clave==null) || clave.trim().equals(""))
			//return "VACIO";
			return "";
		
		if ((clave.endsWith("_null")))
			//return "VACIO";
			return "";
		if(values.get(clave) == null)
			return "NO EXISTE EL PATRON O EL DOMINIO";
		else
			return values.get(clave);		
	}
	
	
	/**
	 * Carga una estructura de dominios con informacion de la Base de Datos
	 * @param c
	 */
	public static void loadEstructuraNucleos(Collection<Object> c) {
		// Generamos dominios para que se puedan presentar en el desplegable al
		// igual que los dominio
		ArrayList listaDominios = new ArrayList();
		
		//***********************************
		//VALOR POR DEFECTO PARA TODOS LOS NUCLEOS
		//***********************************
		String idNode = "000" + "_" + "000";
		String texto="000" + "_"+ "000"+"_"+"Todos";
		DomainNode domainNode = new DomainNode(idNode, null,
				com.geopista.feature.Domain.CODEDENTRY, null,
				String.valueOf(AppContext.getIdMunicipio()),
				idNode, idNode);
		domainNode.setTerm(ConstantesLocalGISEIEL.Locale,texto);
		listaDominios.add(domainNode);

		
		Iterator<Object> it = c.iterator();
		while (it.hasNext()) {
			LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) it.next();
			// String idNode, String idDes, int type, String idParent, String
			// idMuni, String idDomain,
			idNode = nucleoEIEL.getCodentidad() + "_"+ nucleoEIEL.getCodpoblamiento();
			
			texto=nucleoEIEL.getCodentidad() + "_"+ nucleoEIEL.getCodpoblamiento()+"_"+nucleoEIEL.getDenominacion();
			domainNode = new DomainNode(idNode, null,
					com.geopista.feature.Domain.CODEDENTRY, null,
					String.valueOf(AppContext.getIdMunicipio()),
					idNode, idNode);
			domainNode.setTerm(ConstantesLocalGISEIEL.Locale,texto);
			listaDominios.add(domainNode);
		}
		ListaEstructuras lstTipos = new ListaEstructuras(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS);
		lstTipos.loadDB(listaDominios);

		Estructuras.setEstructura(ConstantesLocalGISEIEL.ESTRUCTURA_NUCLEOS,lstTipos);
	}
	
	/**
	 * Carga una estructura de dominios con informacion de la Base de Datos
	 * @param c
	 */
	public static void loadEstructuraEntidadesNucleo(Collection<Object> c) {
		// Generamos dominios para que se puedan presentar en el desplegable al
		// igual que los dominio
		ArrayList listaDominios = new ArrayList();
		
		//***********************************
		//VALOR POR DEFECTO PARA TODOS LOS NUCLEOS
		//***********************************
		String idNode=null;
		String texto=null;
		DomainNode domainNode;
		
		Iterator<Object> it = c.iterator();
		while (it.hasNext()) {
			LCGNucleoEIEL nucleoEIEL = (LCGNucleoEIEL) it.next();
			// String idNode, String idDes, int type, String idParent, String
			// idMuni, String idDomain,
			idNode = nucleoEIEL.getCodentidad();
			
			texto=nucleoEIEL.getCodentidad() +"_"+nucleoEIEL.getDenominacion();
			domainNode = new DomainNode(idNode, null,
					com.geopista.feature.Domain.CODEDENTRY, null,
					String.valueOf(AppContext.getIdMunicipio()),
					idNode, idNode);
			domainNode.setTerm(ConstantesLocalGISEIEL.Locale,texto);
			listaDominios.add(domainNode);
		}
		ListaEstructuras lstTipos = new ListaEstructuras(ConstantesLocalGISEIEL.ESTRUCTURA_ENTIDADES_NUCLEOS);
		lstTipos.loadDB(listaDominios);

		Estructuras.setEstructura(ConstantesLocalGISEIEL.ESTRUCTURA_ENTIDADES_NUCLEOS,lstTipos);
		Estructuras.getListaTipos();
	}


	
	
	

}
