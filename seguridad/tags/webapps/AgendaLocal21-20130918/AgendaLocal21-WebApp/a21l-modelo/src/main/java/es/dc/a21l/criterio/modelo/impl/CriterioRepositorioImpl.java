/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.modelo.impl;

import java.util.ArrayList;
import java.util.List;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.criterio.modelo.Criterio;
import es.dc.a21l.criterio.modelo.CriterioRepositorio;

public class CriterioRepositorioImpl extends RepositorioBaseImpl<Criterio> implements CriterioRepositorio {
	private static final String DELETE_CAMPOS_POR_IDINDICADOR = "delete from Criterio c inner join c.atributo inner join c.atributo.columna inner join c.atributo.columna.tabla where c.atributo.columna.tabla.indicador.id = :idIndicador";
	private static final String DELETE_FORMULAS_POR_IDINDICADOR = "delete from Criterio c inner join c.atributo inner join c.atributo.indicadorExpresion where c.atributo.indicadorExpresion.indicador.id = :idIndicador";
	private static final String SELECT_CAMPOS_POR_IDINDICADOR = "select c from Criterio c where c.atributo.columna.tabla.indicador.id = :idIndicador";
	private static final String SELECT_FORMULAS_POR_IDINDICADOR = "select c from Criterio c where c.atributo.indicadorExpresion.indicador.id = :idIndicador)";
	private static final String SELECT_BY_ATRIBUTO= "select c from Criterio c where c.atributo.id= :idAtributo";
	
	public List<Criterio> cargaPorIdIndicador(Long id) {
		List<Criterio> listaCriterios = new ArrayList<Criterio>();
	
		List<Criterio> criteriosColumna = getEntityManager().createQuery(SELECT_CAMPOS_POR_IDINDICADOR).setParameter("idIndicador", id).getResultList();
		List<Criterio> criteriosFormula = getEntityManager().createQuery(SELECT_FORMULAS_POR_IDINDICADOR).setParameter("idIndicador", id).getResultList();
		
		if (criteriosColumna != null && criteriosColumna.size() > 0)
		{
			listaCriterios.addAll(criteriosColumna);
		}
		
		if (criteriosFormula != null && criteriosFormula.size() > 0)
		{
			listaCriterios.addAll(criteriosFormula);
		}

		return listaCriterios;
	}
	
	public void borraPorIdIndicador(Long id) {
		try {
			getEntityManager().createQuery(DELETE_CAMPOS_POR_IDINDICADOR).setParameter("idIndicador", id).executeUpdate();
			getEntityManager().createQuery(DELETE_FORMULAS_POR_IDINDICADOR).setParameter("idIndicador", id).executeUpdate();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}
	
	public Criterio cargaPorAtributo(Long idAtributo){
		List<Criterio> lista= new ArrayList<Criterio>(getEntityManager().createQuery(SELECT_BY_ATRIBUTO).setParameter("idAtributo", idAtributo).getResultList());
		if(lista.isEmpty())
			return null;
		return lista.get(0);
	}
}
