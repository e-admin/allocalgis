/*
 * El presente software ha sido desarrollado por Balidea Consulting & Programming, con control de calidad y previo diseño por Enxenio S.L., para la Diputación de A Coruña en el seno del proyecto LOURED (http://loured.es), incluído en el Plan AVANZA Local 2009-2011 del Ministerio de Industria, Energía y Turismo del Gobierno de España.  
 * Su distribución se realiza bajo las condiciones establecidas por la licencia European Public License (EUPL), versión 1.1 o posteriores (http://http://joinup.ec.europa.eu/software/page/eupl/licence-eupl)
 */
package es.dc.a21l.NEM.cu;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import es.dc.a21l.base.cu.DtoBase;

@XmlRootElement(name="atributoNEMDto")
public class AtributoNEMDto extends DtoBase {

	private String expresion;
	private String expresionXpath;

	@XmlAttribute(name="expresion")
	public String getExpresion() {
		return expresion;
	}

	public void setExpresion(String expresion) {
		this.expresion = expresion;
	}

	@XmlAttribute(name="expresionXpath")
	public String getExpresionXpath() {
		return expresionXpath;
	}

	public void setExpresionXpath(String expresionXpath) {
		this.expresionXpath = expresionXpath;
	}
	
	
}
