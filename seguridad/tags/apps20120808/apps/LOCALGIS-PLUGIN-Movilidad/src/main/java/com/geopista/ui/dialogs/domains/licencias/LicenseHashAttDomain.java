package com.geopista.ui.dialogs.domains.licencias;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.HashMap;

import com.geopista.app.AppContext;
import com.geopista.app.inventario.Estructuras;
import com.geopista.feature.CodedEntryDomain;
import com.geopista.feature.DateDomain;
import com.geopista.feature.Domain;
import com.geopista.feature.TreeDomain;
import com.geopista.protocol.CConstantesComando;
import com.geopista.protocol.ListaEstructuras;
import com.geopista.protocol.administrador.dominios.DomainNode;
import com.geopista.util.exception.CancelException;

public class LicenseHashAttDomain {

	public static HashMap<String, ListaEstructuras> tablaAsociaciones;
	private static String datePattern = "?[*:*]dd-MM-yyyy";
	
	static {
		CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString("geopista.conexion.servidorurl")+"licencias";
		if(!com.geopista.app.licencias.estructuras.Estructuras.isCargada()){
			com.geopista.app.licencias.estructuras.Estructuras.cargarEstructuras();
		}
//		CConstantesComando.loginLicenciasUrl = AppContext.getApplicationContext().getString("geopista.conexion.servidorurl")+"inventario";
		if(!com.geopista.app.inventario.Estructuras.isCargada()){
			try {
				com.geopista.app.inventario.Estructuras.cargarEstructuras();
			} catch (CancelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		tablaAsociaciones = new HashMap<String, ListaEstructuras>();
		tablaAsociaciones.put("idTipoObra", com.geopista.app.licencias.estructuras.Estructuras.getListaTiposObra());
		tablaAsociaciones.put("idEstadoNotif", com.geopista.app.licencias.estructuras.Estructuras.getListaEstadosNotificacion());
		tablaAsociaciones.put("tipotramitacion", com.geopista.app.licencias.estructuras.Estructuras.getListaTiposTramitacion());
		tablaAsociaciones.put("idTipoLicencia", com.geopista.app.licencias.estructuras.Estructuras.getListaTiposActividad());
		tablaAsociaciones.put("idEstado", com.geopista.app.licencias.estructuras.Estructuras.getListaEstados());
		tablaAsociaciones.put("idTipoObraMenor", com.geopista.app.licencias.estructuras.Estructuras.getListaTiposObraMenor());
		tablaAsociaciones.put("usoJuridico", com.geopista.app.inventario.Estructuras.getListaUsoJuridico());
		tablaAsociaciones.put("subTipoBienes", com.geopista.app.inventario.Estructuras.getListaSubtipoBienesPatrimonio());
		tablaAsociaciones.put("tiposBienes", com.geopista.app.inventario.Estructuras.getListaClasificacionBienesPatrimonio());
		
		
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
			ListaEstructuras listaTipos = tablaAsociaciones.get(att);
			Hashtable listaDNodes = listaTipos.getLista();
			Enumeration keysIdNode = listaDNodes.keys();
			String idNode = null;
			DomainNode domainNode = null;
			Domain domain = new TreeDomain(att, att);
			while (keysIdNode.hasMoreElements()) {
				idNode = (String) keysIdNode.nextElement();
				domainNode = (DomainNode) listaDNodes.get(idNode);
				//System.out.println("DomainNode: " + domainNode.getTerm(sLocale));
				domain.addChild(new CodedEntryDomain(domainNode.getPatron(), domainNode.getTerm(sLocale)));
			}
			
			return domain;
		}
	}
	
}
