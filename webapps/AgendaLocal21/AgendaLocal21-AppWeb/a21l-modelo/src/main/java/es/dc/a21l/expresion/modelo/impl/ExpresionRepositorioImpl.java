/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.modelo.impl;

import java.util.List;

import es.dc.a21l.base.modelo.impl.RepositorioBaseImpl;
import es.dc.a21l.elementoJerarquia.modelo.IndicadorExpresion;
import es.dc.a21l.expresion.modelo.Expresion;
import es.dc.a21l.expresion.modelo.ExpresionRepositorio;

public class ExpresionRepositorioImpl extends RepositorioBaseImpl<Expresion> implements ExpresionRepositorio {

	private static final String SELECT_EXPRESION_ATRIBUTO_FUENTE_DATOS="select count(e.id) from Expresion e where e.atributoFuenteDatosIzq.id= :idFuenteDatos or e.atributoFuenteDatosDch.id = :idFuenteDatos";
	
	private static final String SELECT_POR_INDICADOR="select e from Expresion e where e.atributoFuenteDatosIzq.tabla.indicador.id = :idIndicador or e.atributoFuenteDatosDch.tabla.indicador.id = :idIndicador";
	
	public Boolean cargaEsUtilizadoAtributoPorMasDeUnaExpresion(Long idFuenteDatos){
		return ((Long)getEntityManager().createQuery(SELECT_EXPRESION_ATRIBUTO_FUENTE_DATOS).setParameter("idFuenteDatos", idFuenteDatos).getSingleResult())!=1;
	}
	
	public List<Expresion> cargaPorIdIndicador(Long idIndicador) {
		return getEntityManager().createQuery(SELECT_POR_INDICADOR).setParameter("idIndicador", idIndicador).getResultList();
	}
}
