/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.publicacion.modelo.impl;

import java.util.List;

import es.dc.a21l.base.modelo.GestionErrores.EncapsuladorErroresSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.publicacion.modelo.Publicacion;
import es.dc.a21l.publicacion.modelo.PublicacionRepositorio;

public class PublicacionRepositorioImpl extends RepositorioBaseImpl<Publicacion> implements PublicacionRepositorio{	

	public Publicacion guardar(Publicacion publicacion, EncapsuladorErroresSW errores) {
		Publicacion publiDevolver = new Publicacion();
		List<Publicacion> lista = cargaTodos();
		if ( lista.isEmpty()) {
			guarda(publicacion);
			publiDevolver = publicacion;
		} else {
			for ( Publicacion publi : lista ) {
				publi.setPublicacionWebHabilitada(publicacion.getPublicacionWebHabilitada());
				guarda(publi);
				publiDevolver = publi;
			}
		}
		return publiDevolver;
	}
}