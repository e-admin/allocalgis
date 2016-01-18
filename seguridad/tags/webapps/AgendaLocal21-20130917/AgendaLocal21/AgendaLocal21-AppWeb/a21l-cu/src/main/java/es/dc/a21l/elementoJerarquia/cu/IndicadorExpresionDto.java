/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.elementoJerarquia.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;

@SuppressWarnings("restriction")
@XmlRootElement(name="indicadorExpresionDto")
public class IndicadorExpresionDto extends DtoBase {

	private Long idIndicador;
	//En la creacion puede tener cualquier valor, se crea la expresion al guardar el objeto
	private Long idExpresion;
	private String expresionLiteral;
	
	//Atributo oculto de expresion mapeada
	private String expresionTransformada;

	@XmlAttribute(name="idIndicador")
	public Long getIdIndicador() {
		return idIndicador;
	}

	public void setIdIndicador(Long idIndicador) {
		this.idIndicador = idIndicador;
	}

	@XmlAttribute(name="expresionLiteral")
	public String getExpresionLiteral() {
		return expresionLiteral;
	}

	public void setExpresionLiteral(String expresionLiteral) {
		this.expresionLiteral = expresionLiteral;
	}

	@XmlAttribute(name="expresionTransformada")
	public String getExpresionTransformada() {
		return expresionTransformada;
	}

	public void setExpresionTransformada(String expresionTransformada) {
		this.expresionTransformada = expresionTransformada;
	}

	@XmlAttribute(name="idExpresion")
	public Long getIdExpresion() {
		return idExpresion;
	}

	public void setIdExpresion(Long idExpresion) {
		this.idExpresion = idExpresion;
	}
	
}
