/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.modelo.RangosVisualizacionMapa;
import es.dc.a21l.visualizacion.modelo.RangosVisualizacionMapaRepositorio;

@SuppressWarnings("unchecked")
public class RangosVisualizacionMapaRepositorioImpl extends RepositorioBaseImpl<RangosVisualizacionMapa> implements RangosVisualizacionMapaRepositorio {
	
	private static final String SELECT_POR_ID_ESTILO_MAPA="select r from RangosVisualizacionMapa r where r.estiloMapa.id = :idEstiloMapa";
	
	
	public List<RangosVisualizacionMapa> cargaPorIdEstiloMapa(Long idEstiloMapa){
		try {
			return (List<RangosVisualizacionMapa>)getEntityManager().createQuery(SELECT_POR_ID_ESTILO_MAPA).setParameter("idEstiloMapa", idEstiloMapa).getResultList();
		} catch (Exception ex) {
			return new ArrayList<RangosVisualizacionMapa>();
		}
	}
}