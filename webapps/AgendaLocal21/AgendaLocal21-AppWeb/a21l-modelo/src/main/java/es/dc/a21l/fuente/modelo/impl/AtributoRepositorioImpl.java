/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.fuente.modelo.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;

import es.dc.a21l.base.modelo.encapsulacionSW.EncapsuladorListSW;
import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.fuente.cu.AtributoDto;
import es.dc.a21l.fuente.cu.AtributoFuenteDatosDto;
import es.dc.a21l.fuente.cu.AtributosMapDto;
import es.dc.a21l.fuente.cu.TablaFuenteDatosDto;
import es.dc.a21l.fuente.modelo.Atributo;
import es.dc.a21l.fuente.modelo.AtributoRepositorio;
import es.dc.a21l.fuente.modelo.FuenteRepositorio;

/**
 *
 * @author Balidea Consulting & Programming
 */
public class AtributoRepositorioImpl extends RepositorioBaseImpl<Atributo> implements AtributoRepositorio{
	private static final String SELECT_ATRIBUTO_POR_ATRIBUTO_FUENTE_DATOS="select a from Atributo a where t.columna.id = :idColumna";
	private static final String SELECT_ATRIBUTOS_POR_INDICADOR="select a from Atributo a "+ 
			"inner join a.columna "+
			"inner join a.columna.tabla "+
			"where a.columna.tabla.indicador.id = :idIndicador order by a.ordenVisualizacion ";
	private static final String SELECT_ATRIBUTOS_POR_INDICADOR_FORMULA = "select b from Atributo b "+
			"inner join b.indicadorExpresion "+
			"where b.indicadorExpresion.indicador.id = :idIndicador order by b.ordenVisualizacion";	
	private static final String SELECT_ATRIBUTOS_POR_INDICADOR_RELACION = 
			"select b from Atributo b "+
			"inner join b.relacion "+
			"where b.relacion.indicador.id = :idIndicador order by b.relacion.nombreTablaRelacionada, b.relacion.nombreColumnaRelacionada";
	
	
	public Atributo cargaPorAtributoFuenteDatos(Long idAtributoFuenteDatos) {
		Atributo atributo = null;
		
		try {
			atributo = (Atributo)getEntityManager().createQuery(SELECT_ATRIBUTO_POR_ATRIBUTO_FUENTE_DATOS).setParameter("idColumna", idAtributoFuenteDatos).getSingleResult();
		} catch ( Exception ex ) {
			return new Atributo();
		}
		return atributo;
	}	
	
	public List<Atributo> cargaPorIndicador(Long idIndicador) {
		List<Atributo> atributos = new ArrayList<Atributo>();
		
		try {
			atributos = (List<Atributo>)getEntityManager().createQuery(SELECT_ATRIBUTOS_POR_INDICADOR).setParameter("idIndicador", idIndicador).getResultList();
			atributos.addAll((List<Atributo>)getEntityManager().createQuery(SELECT_ATRIBUTOS_POR_INDICADOR_FORMULA).setParameter("idIndicador", idIndicador).getResultList());
			atributos.addAll((List<Atributo>)getEntityManager().createQuery(SELECT_ATRIBUTOS_POR_INDICADOR_RELACION).setParameter("idIndicador", idIndicador).getResultList());
			Collections.sort(atributos, new Comparator<Atributo>() {
				public int compare(Atributo o1, Atributo o2) {
					if ( o1.getOrdenVisualizacion()<o2.getOrdenVisualizacion())
						return 0;
					else
						return 1;
				}
			});
		} catch ( Exception ex ) {
			return new ArrayList<Atributo>();
		}
		return atributos;
	}
}
