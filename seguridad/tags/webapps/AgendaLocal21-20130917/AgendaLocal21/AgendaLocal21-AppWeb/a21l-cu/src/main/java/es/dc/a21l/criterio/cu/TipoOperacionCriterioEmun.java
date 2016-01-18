/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.dc.a21l.expresion.cu.TipoOperacionEmun;

public enum TipoOperacionCriterioEmun implements Serializable {

	MAYOR(">"),
	MAYOR_IGUAL(">="),
	MENOR("<"),
	MENOR_IGUAL("<="),
	IGUAL("="),
	DISTINTO("<>"),
	AND("and"),
	OR("or"),
	NOT("not"),
	LIKE("like"),
	CONTAINS("contains");
	
	private String representacion;
	
	private TipoOperacionCriterioEmun(String representacion){
		this.representacion=representacion;
	}

	public String getRepresentacion() {
		return representacion;
	}
	
	public static List<TipoOperacionCriterioEmun> getListaValoresEvaluacion(){
		List<TipoOperacionCriterioEmun> lista= new ArrayList<TipoOperacionCriterioEmun>();
		lista.add(NOT);
		for(TipoOperacionCriterioEmun temp:values()){
			if(!temp.equals(NOT))
				lista.add(temp);
		}
		return lista;
	}
	
	public static TipoOperacionCriterioEmun getPorRepresentacion(String respresentacion){
		for(TipoOperacionCriterioEmun temp:values()){
			if(temp.getRepresentacion().equals(respresentacion))
				return temp;
		}
		return null;
	}
	
	public static List<String> getListaValoresEvaluar(){
		List<String> result= new ArrayList<String>();
		
		for(TipoOperacionCriterioEmun temp:values()){
			if(temp.equals(NOT)){
				result.add(temp.getRepresentacion()+"(");
			}
				result.add(temp.getRepresentacion());
		}
		return result;
	}
	
	public static List<TipoOperacionCriterioEmun> getComparaciones(){
		ArrayList<TipoOperacionCriterioEmun> result= new ArrayList<TipoOperacionCriterioEmun>();
		result.add(MAYOR);
		result.add(MAYOR_IGUAL);
		result.add(MENOR);
		result.add(MENOR_IGUAL);
		result.add(IGUAL);
		result.add(DISTINTO);
		result.add(LIKE);
		result.add(CONTAINS);
		return result;
	}
	
	public static List<TipoOperacionCriterioEmun> getOperadoresLogicos(){
		ArrayList<TipoOperacionCriterioEmun> result= new ArrayList<TipoOperacionCriterioEmun>();
		result.add(AND);
		result.add(OR);
		result.add(NOT);
		return result;
	}
}
