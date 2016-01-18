package com.geopista.app.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.geopista.app.AppContext;
import com.geopista.app.eiel.ConstantesLocalGISEIEL;
import com.geopista.app.eiel.beans.filter.LCGNucleoEIEL;
import com.geopista.app.eiel.beans.indicadores.IndicadorEIEL;
import com.geopista.app.utilidades.estructuras.Estructuras;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;

public class LCGFilter {

	public static String getValue(Object valuesObj,String clave){
		
		HashMap<String, String> values=(HashMap<String, String>)valuesObj;
		//return "("+values.get(clave)+")";	
		if ((clave==null) || clave.trim().equals(""))
			return "VACIO";
		
		if ((clave.endsWith("_null")))
			return "VACIO";
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
