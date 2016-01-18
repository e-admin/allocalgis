/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;
import es.dc.a21l.fuente.modelo.TablaFuenteDatosRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class TablaFuenteDatosRepositorioImpl extends RepositorioBaseImpl<TablaFuenteDatos> implements TablaFuenteDatosRepositorio{
	private static final String SELECT_TABLAS_POR_INDICADOR_Y_FUENTE="select t from TablaFuenteDatos t where t.indicador.id = :idindicador and t.fuente.id = :idfuente";
	private static final String SELECT_TABLAS_POR_INDICADOR_Y_FUENTE_Y_NOMBRE="select t from TablaFuenteDatos t where t.indicador.id = :idindicador and t.fuente.id = :idfuente and t.nombre = :nombre";
	private static final String SELECT_TABLAS_BY_NOMBRE_FUENTE_INDICADOR="select tf from TablaFuenteDatos tf where tf.nombre =:nombre and tf.indicador.id =:idIndicador and tf.fuente.id =:idFuente";
	
	@SuppressWarnings("unchecked")
	public List<TablaFuenteDatos> cargaTodosPorIndicadorYFuente(Long idIndicador, Long idFuente) {
		List<TablaFuenteDatos> listaTablas = null;
		
		try {
			listaTablas = (List<TablaFuenteDatos>)getEntityManager().createQuery(SELECT_TABLAS_POR_INDICADOR_Y_FUENTE).setParameter("idindicador", idIndicador).setParameter("idfuente", idFuente).getResultList();
		} catch ( Exception ex ) {
			return new ArrayList<TablaFuenteDatos>();
		}
		return listaTablas;
	}
	
	public TablaFuenteDatos cargaPorIndicadorYFuenteYTabla(Long idIndicador, Long idFuente, String nombreTabla) {
		TablaFuenteDatos tabla = null;
		
		try {
			tabla = (TablaFuenteDatos)getEntityManager().createQuery(SELECT_TABLAS_POR_INDICADOR_Y_FUENTE_Y_NOMBRE).setParameter("idindicador", idIndicador).setParameter("idfuente", idFuente).setParameter("nombre", nombreTabla).getSingleResult();
		} catch ( Exception ex ) {
			return new TablaFuenteDatos();
		}
		return tabla;
	}
	
	public TablaFuenteDatos cargaFuentePorIndicadorYNombreYFuente(Long idIndicador, Long idFuente, String nombre){
		List<TablaFuenteDatos>lista= new ArrayList<TablaFuenteDatos>(getEntityManager().createQuery(SELECT_TABLAS_BY_NOMBRE_FUENTE_INDICADOR).setParameter("idIndicador", idIndicador).setParameter("idFuente", idFuente).setParameter("nombre", nombre).getResultList());
		if(lista.isEmpty())
			return null;
		return lista.get(0);
		
	}
	
}
