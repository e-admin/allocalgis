/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.metadatos.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.metadatos.modelo.Metadatos;
import es.dc.a21l.metadatos.modelo.MetadatosRepositorio;

public class MetadatosRepositorioImpl extends RepositorioBaseImpl<Metadatos> implements MetadatosRepositorio{
	private static final String SELECT_METADATOS_POR_ID_INDICADOR="select m from Metadatos m where m.indicador.id = :idindicador";
	private static final String BORRAR_METADATOS_POR_ID_INDICADOR="delete from Metadatos m where m.indicador.id = :idindicador";
	
	
	public Metadatos cargaPorIdIndicador(Long id) {
		 List<Metadatos> lista= new ArrayList<Metadatos>(getEntityManager().createQuery(SELECT_METADATOS_POR_ID_INDICADOR).setParameter("idindicador", id).getResultList());
		 if(lista.isEmpty())
			 return null;
		 return lista.get(0);
		
	}
	
	public Metadatos borrarPorIdIndicador(Long id) {
		try {
			getEntityManager().createQuery(BORRAR_METADATOS_POR_ID_INDICADOR).setParameter("idindicador", id).executeUpdate();
		} catch ( Exception ex ) {
			return null;
		}
		return new Metadatos();
	}
	
}
