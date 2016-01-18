/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.visualizacion.modelo.impl;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaBarras;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectores;
import es.dc.a21l.visualizacion.modelo.EstiloVisualizacionDiagramaSectoresRepositorio;

@SuppressWarnings("unchecked")
public class EstiloVisualizacionDiagramaSectoresRepositorioImpl extends RepositorioBaseImpl<EstiloVisualizacionDiagramaSectores> implements EstiloVisualizacionDiagramaSectoresRepositorio {
	
	private static final String SELECT_POR_ID_USUARIO_E_ID_INDICADOR="select e from EstiloVisualizacionDiagramaSectores e where e.usuario.id = :idUsuario and e.indicador.id = :idIndicador";
	
	
	public EstiloVisualizacionDiagramaSectores cargaPorIdUsuarioEIdIndicador(Long idUsuario, Long idIndicador){
		try {
			return (EstiloVisualizacionDiagramaSectores)getEntityManager().createQuery(SELECT_POR_ID_USUARIO_E_ID_INDICADOR).setParameter("idUsuario", idUsuario).setParameter("idIndicador", idIndicador).getSingleResult();
		} catch (Exception ex) {
			return new EstiloVisualizacionDiagramaSectores();
		}
	}
}