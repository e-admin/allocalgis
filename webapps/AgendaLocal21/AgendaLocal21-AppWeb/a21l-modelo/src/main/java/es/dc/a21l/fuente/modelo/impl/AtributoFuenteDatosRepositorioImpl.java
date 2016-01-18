/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.base.cu.impl.UtilidadesModelo;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatos;
import es.dc.a21l.fuente.modelo.AtributoFuenteDatosRepositorio;
import es.dc.a21l.fuente.modelo.TablaFuenteDatos;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class AtributoFuenteDatosRepositorioImpl extends RepositorioBaseImpl<AtributoFuenteDatos> implements AtributoFuenteDatosRepositorio{
	private static final String SELECT_ATRIBUTOS_POR_TABLA="select a from AtributoFuenteDatos t where t.tabla.id = :idtabla";
	private static final String SELECT_NUMERO_ATRIBUTOS_POR_TABLA="select count(a) from AtributoFuenteDatos a where a.tabla.id = :idtabla";
	private static final String SELECT_BY_IDTABLA_NOMBRE_ESFORMULA="select a from AtributoFuenteDatos a where a.nombre =:nombre and a.esFormula =:esFormula and a.tabla.id =:idTabla";
	
	@SuppressWarnings("unchecked")
	public List<AtributoFuenteDatos> cargaTodosPorTabla(Long idTabla) {
		List<AtributoFuenteDatos> listaAtributos = null;
		
		try {
			listaAtributos = (List<AtributoFuenteDatos>)getEntityManager().createQuery(SELECT_ATRIBUTOS_POR_TABLA).setParameter("idtabla", idTabla).getResultList();
		} catch ( Exception ex ) {
			return new ArrayList<AtributoFuenteDatos>();
		}
		return listaAtributos;
	}
	
	public boolean existenAtributosDeTabla(Long idTabla) {
		List<AtributoFuenteDatos> listaAtributos = null;
		listaAtributos = new ArrayList<AtributoFuenteDatos>(getEntityManager().createQuery(SELECT_NUMERO_ATRIBUTOS_POR_TABLA).setParameter("idtabla", idTabla).getResultList());
		return !listaAtributos.isEmpty();
	}
	
	public AtributoFuenteDatos cargaPorTablaYNombreYEsForumula(String nombre,Boolean esFormula, Long idTabla){
		List<AtributoFuenteDatos> lista= new ArrayList<AtributoFuenteDatos>(getEntityManager().createQuery(SELECT_BY_IDTABLA_NOMBRE_ESFORMULA).setParameter("nombre", nombre).setParameter("esFormula", UtilidadesModelo.convertBooleanToShort(esFormula)).setParameter("idTabla", idTabla).getResultList());
		return lista.isEmpty()?null:lista.get(0);
	}
	
	
	
}
