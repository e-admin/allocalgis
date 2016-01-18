/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.NEM.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.NEM.modelo.AtributoNEM;
import es.dc.a21l.NEM.modelo.AtributoNEMRepositorio;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;

public class AtributoNEMRepositorioImpl extends RepositorioBaseImpl<AtributoNEM> implements AtributoNEMRepositorio {

	
	public List<AtributoNEM> cargaPorListaIds(List<Long> ids){
		List<AtributoNEM> result= new ArrayList<AtributoNEM>();
		for(Long temp:ids)
			result.add(carga(temp));
		return result;
	}
}
