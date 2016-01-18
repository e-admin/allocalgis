/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.criterio.cu;

import java.io.Serializable;

public enum TipoOperandoCriterioEmun implements Serializable {

	
	CRITERIO("Criterio"),
	LITERAL("Literal"),
	SIN_OPERANDO("Sin operando"),
	PARAMETRO_ESPERADO("Parametro Esperado")
	;
	private String nombre;
	
	private TipoOperandoCriterioEmun(String nombre){
		this.nombre=nombre;
	}

	public String getNombre() {
		return nombre;
	}
}
