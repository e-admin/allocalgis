/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.expresion.cu;

import java.util.ArrayList;
import java.util.List;

public enum TipoOperacionEmun {

	SUMA("+"),
	RESTA("-"),
	PRODUCTO("*"),
	DIVISION("/"),
	MODULO("%"),
	ABS("abs"),
	EXPONENTE("^")
	;
	private String representacion;
	
	private TipoOperacionEmun(String representacion){
		this.representacion=representacion;
	}

	public String getRepresentacion() {
		return representacion;
	}
	
	public static List<TipoOperacionEmun> getListaValoresEvaluacion(){
		List<TipoOperacionEmun> lista= new ArrayList<TipoOperacionEmun>();
		lista.add(ABS);
		for(TipoOperacionEmun temp:values()){
			if(!temp.equals(ABS))
				lista.add(temp);
		}
		return lista;
	}
	
	public static TipoOperacionEmun getPorRepresentacion(String respresentacion){
		for(TipoOperacionEmun temp:values()){
			if(temp.getRepresentacion().equals(respresentacion))
				return temp;
		}
		return null;
	}
	
	public static List<String> getListaValoresEvaluar(){
		List<String> result= new ArrayList<String>();
		
		for(TipoOperacionEmun temp:values()){
			if(temp.equals(ABS)){
				result.add(temp.getRepresentacion()+"(");
			}
				result.add(temp.getRepresentacion());
		}
		return result;
	}
	
	
}
